package com.example.restful.data;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.RestAdapter;
import retrofit.http.RetrofitError;

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
			.build();
		return restAdapter;
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
