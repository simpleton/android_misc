package com.example.restful.data;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.net.http.AndroidHttpClient;

import retrofit.http.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.RestAdapter;
import retrofit.http.RetrofitError;
import retrofit.http.client.ApacheClient;
import retrofit.http.client.Client;

public class OrgClient {
	@SuppressWarnings("unused")
	private static final String TAG = "OrgClient";
	private static final String API_URL = "https://id.b.qq.com";

	public class Content {
		int code;
	    long qquin;
	    public Department data;
	}

	public class Collegue {
		String d;
		int g;
		String j;
		String m;
		String n;
		String p;
		String e;
		long q;
		String u;
	}
	
	public class Department {
		long i;
		ArrayList<Collegue> m;
		String n;
		ArrayList<Department> o;
	}
	
	interface Organization {
		@GET("/hrtx/clientcall/mobileApi/getTree")
		void getTree(Callback<Content> callback);
		
		@GET("/hrtx/clientcall/mobileApi/getTree")
		Content getTree();
	}
	
	private static RestAdapter initClient(final String uin, final String skey) {
		Headers headers = new Headers() {			
			@Override
			public List<Header> get() {
				List<Header> headers = new ArrayList<Header>();
				StringBuilder Cookie = new StringBuilder().append("uin=o").append(uin).append(";")
						.append("skey=").append(skey).append(";").append("ptisp=ctc");
				headers.add(new Header("Cookie", Cookie.toString()));
				
				return headers;
			}
		};
		
		RestAdapter restAdapter = new RestAdapter.Builder()
			.setServer(API_URL)
			.setDebug(true)
			.setHeaders(headers)
			//.setClient(new HttpsClient())
			.build();
		return restAdapter;
	}
		
	public static class CustomSSLSocketFactory extends SSLSocketFactory {

		private SSLContext sslContext = SSLContext.getInstance("TLS");

		public CustomSSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {

			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};

			sslContext.init(null, new TrustManager[] { tm }, null);

		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}

	}

	public static HttpClient getHttpClient() {
		final int TIME_OUT = 10000;
		DefaultHttpClient client = null;

		try {

			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// Setting up parameters
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "utf-8");
			params.setBooleanParameter("http.protocol.expect-continue", false);

			// Setting timeout
			HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, TIME_OUT);

			// Registering schemes for both HTTP and HTTPS
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			// Creating thread safe client connection manager
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			// Creating HTTP client
			client = new DefaultHttpClient(ccm, params);

		} catch (Exception e) {
			client = new DefaultHttpClient();
		}

		return client;
	}
	
	public final static class HttpsClient extends ApacheClient {
		public HttpsClient() {
			super(getHttpClient());
		}
	}
	
	/**
	 * GET Organization information <b>asynchronously</b>, 
	 * @param uin uin info should feed in cookies
	 * @param skey as same as uin, feeded in cookies
	 * @param callback fetch complete callback info, u can save the info in this callback
	 */
	public static void GET(final String uin, final String skey, Callback<Content> callback) {
		RestAdapter restAdapter = initClient(uin, skey);

		// Create an instance API interface.
		Organization organization = restAdapter.create(Organization.class);

		// Fetch and print a list of the contributors to this library.
		organization.getTree(callback);
	}

	/**
	 * GET organization information <b>synchronously</b>
	 * @param uin uin info should feed in cookies
	 * @param skey as same as uin, feeded in cookies
	 * @throws RetrofitError Thrown if any error occurs during the HTTP request
	 */
	public static Content GET(final String uin, final String skey) {
		Content content = null;
		RestAdapter restAdapter = initClient(uin, skey);

		// Create an instance API interface.
		Organization organization = restAdapter.create(Organization.class);

		// Fetch and print a list of the contributors to this library.
		try {
			content = organization.getTree();	
		} catch (RetrofitError e) {
			throw e;
		}
		
		return content;
	}
}
