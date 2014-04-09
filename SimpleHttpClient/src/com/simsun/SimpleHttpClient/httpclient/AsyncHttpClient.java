/**
 *
 */
package com.simsun.SimpleHttpClient.httpclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import android.content.Context;
import android.os.Build;
import android.util.Pair;


public class AsyncHttpClient {
    public static final String TAG = "AsyncHttpClient";

    private static final int DEFAULT_MAX_CONNECTIONS = 6;
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 1024 * 8;
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    private static String ENCODING = "UTF-8";
    private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;
    private ThreadPoolExecutor threadPool;
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;
    private final Map<String, String> clientHeaderMap;

    /**
     * Perform a HTTP GET request and track the Android Context which initiated
     * the request with customized headers
     *
     * @param url
     *            the URL to send the request to.
     * @param headers
     *            set headers only for this request
     * @param params
     *            additional GET parameters to send with the request.
     * @param responseHandler
     *            the response handler instance that should handle the response.
     */
    public void get(Context context, String url, Header[] headers,
                    List<Pair<String, String>> params,
                    AsyncHttpResponseHandler responseHandler) {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null)
            request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, null, responseHandler, context);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated
     * the request. Set headers only for this request
     *
     * @param context  the Android Context which initiated the request.
     * @param url  the URL to send the request to.
     * @param headers  set headers only for this request
     * @param entity  a raw {@link HttpEntity} to send with the request, for
     *            example, use this to send string/json/xml payloads to a server
     *            by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType  the content type of the payload you are sending, for example
     *            application/json if sending a json payload.
     * @param responseHandler  the response handler instance that should handle the response.
     */
    public void post(Context context, String url, Header[] headers,
                     HttpEntity entity, String contentType,
                     AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if(entity != null && request != null) {
            request.setEntity(entity);
        }
        if (headers != null && request != null) {
            request.setHeaders(headers);
        }
        sendRequest(httpClient, httpContext, request, contentType, responseHandler, context);
    }

    public static String getUrlWithQueryString(String url,
                                               List<Pair<String, String>> params) {
        if (params != null) {
            List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();
            for (Pair<String, String> entry : params) {
                lparams.add(new BasicNameValuePair(entry.first, entry.second));
            }
            String str_params = URLEncodedUtils.format(lparams, ENCODING);
            if (url.indexOf("?") == -1) {
                url += "?" + str_params;
            } else {
                url += "&" + str_params;
            }
        }
        return url;
    }

    public AsyncHttpClient(){
        BasicHttpParams httpParams = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParams, socketTimeout);
        try {
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
                    new ConnPerRouteBean(maxConnections));
            ConnManagerParams.setMaxTotalConnections(httpParams,
                    DEFAULT_MAX_CONNECTIONS);
        } catch (NoSuchMethodError e) {
        }

        try {
            HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
            HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
            HttpConnectionParams.setTcpNoDelay(httpParams, true);
            HttpConnectionParams.setSocketBufferSize(httpParams,
                    DEFAULT_SOCKET_BUFFER_SIZE);

            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            // HttpProtocolParams.setUserAgent(httpParams, );
        }  catch (NoSuchMethodError e) {
        }

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        /**
         * 为了解决某些2.2的机器ssl的问题
         * http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https
         *
         * 改成JELLY_BEAN是因为
         * http://tapd.oa.com/v3/10066461/bugtrace/bugs/view?bug_id=1010066461048919896
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            KeyStore trustStore;
            try {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
                //sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
                
                sf.setHostnameVerifier(new X509HostnameVerifier() {
					private X509HostnameVerifier mVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
					@Override
					public void verify(String host, String[] cns, String[] subjectAlts)
							throws SSLException {
						if(host.equalsIgnoreCase("datamarket.accesscontrol.windows.net")){//微软的证书居然有问题
							host = "accesscontrol.windows.net";
						}
						mVerifier.verify(host, cns, subjectAlts);
					}
					
					@Override
					public void verify(String host, X509Certificate cert) throws SSLException {
						if(host.equalsIgnoreCase("datamarket.accesscontrol.windows.net")){//微软的证书居然有问题
							host = "accesscontrol.windows.net";
						}
						mVerifier.verify(host, cert);
					}
					
					@Override
					public void verify(String host, SSLSocket ssl) throws IOException {
						if(host.equalsIgnoreCase("datamarket.accesscontrol.windows.net")){//微软的证书居然有问题
							host = "accesscontrol.windows.net";
						}
						mVerifier.verify(host, ssl);
					}
					
					@Override
					public boolean verify(String host, SSLSession session) {
                        Log.i(TAG, "[AsyncHttpClient] host:" + host);
						if(host.equalsIgnoreCase("datamarket.accesscontrol.windows.net")){//微软的证书居然有问题
							host = "accesscontrol.windows.net";
						}
						return mVerifier.verify(host, session);
						//return false;
					}
				});
                
                schemeRegistry.register(new Scheme("https", sf, 443));
            }
            catch (Exception e) {
                Log.e(TAG, "accept all ssl factory error" + e);
                schemeRegistry.register(new Scheme("https", SSLSocketFactory
                        .getSocketFactory(), 443));
            }
        } else {
            schemeRegistry.register(new Scheme("https", SSLSocketFactory
                    .getSocketFactory(), 443));
        }
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
                httpParams, schemeRegistry);

        httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        httpClient = new DefaultHttpClient(cm, httpParams);
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
                for (String header : clientHeaderMap.keySet()) {
                    request.addHeader(header, clientHeaderMap.get(header));
                }
            }
        });

        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response
                                    .getEntity()));
                            break;
                        }
                    }
                }
            }
        });

        httpClient.setHttpRequestRetryHandler(new RetryHandler(
                DEFAULT_MAX_RETRIES));

        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        requestMap = new WeakHashMap<Context, List<WeakReference<Future<?>>>>();
        clientHeaderMap = new HashMap<String, String>();
    }

    public static class CustomSSLSocketFactory extends SSLSocketFactory {

        private SSLContext sslContext = SSLContext.getInstance("TLS");

        public CustomSSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {

            super(truststore);

            TrustManager tm = null;
            try {
                tm = new MyX509TrustManager();
            } catch (Exception e) {
                Log.e(TAG, "[cancel] cancel task" + e);
            }

            sslContext.init(null, new TrustManager[] { tm }, null);

        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,	port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }

    }

    public static class MyX509TrustManager implements X509TrustManager {
        X509TrustManager sunJSSEX509TrustManager;
        MyX509TrustManager() throws Exception {
            // create a "default" JSSE X509TrustManager.
            KeyStore ks = null;
            try {
                ks = KeyStore.getInstance("JKS");
            } catch (Exception e){
                // donothing
            }
            TrustManager tms [] = {};
            if (ks != null){

                ks.load(new FileInputStream("trustedCerts"), "passphrase".toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
                tmf.init(ks);
                tms = tmf.getTrustManagers();

            }else{
                TrustManagerFactory tmf = TrustManagerFactory
                        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init((KeyStore) null);
                tms =  tmf.getTrustManagers();

            }
            for (int i = 0; i < tms.length; i++) {
                if (tms[i] instanceof X509TrustManager) {
                    sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                    return;
                }
            }
            throw new Exception("Couldn't initialize");
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException excep) {
            }
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            try {
                sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException excep) {
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return sunJSSEX509TrustManager.getAcceptedIssuers();
        }
    }

    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

    protected void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest,
                               String contentType, AsyncHttpResponseHandler responseHandler, Context context) {

        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }
        Future<?> request = threadPool.submit(new AsyncHttpRequest(client,
                httpContext, uriRequest, responseHandler));

        if (context != null) {
            // Add request to request map
            List<WeakReference<Future<?>>> requestList = requestMap
                    .get(context);
            if (requestList == null) {
                requestList = new LinkedList<WeakReference<Future<?>>>();
                requestMap.put(context, requestList);
            }

            requestList.add(new WeakReference<Future<?>>(request));
        }
    }

    /**
     * Cancels any pending (or potentially active) requests associated with the
     * passed Context.
     * <p>
     * <b>Note:</b> This will only affect requests which were created with a
     * non-null android Context. This method is intended to be used in the
     * onDestroy method of your android activities to destroy all requests which
     * are no longer required.
     *
     * @param context
     *            the android Context instance associated to the request.
     * @param mayInterruptIfRunning
     *            specifies if active requests should be cancelled along with
     *            pending requests.
     */
    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        List<WeakReference<Future<?>>> requestList = requestMap.get(context);
        if (requestList != null) {
            for (WeakReference<Future<?>> requestRef : requestList) {
                Future<?> request = requestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                    Log.e(TAG, "[cancel] cancel task" + request.toString());
                }
            }
        }
        requestMap.remove(context);
    }

}
