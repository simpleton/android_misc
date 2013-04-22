package com.example.restful_example;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.RestAdapter;
import android.os.AsyncTask;
import android.util.Log;

public class OrgClient {
	private static final String TAG = "OrgClient";
	private static final String API_URL = "https://id.b.qq.com";

	class Content {
		int code;
	    long qquin;
	    organization_node data;
	}

	class member_node {
		String d;
		int g;
		String j;
		String m;
		String n;
		String p;
		String e;
		String q;
		String u;
	}
	
	class organization_node {
		long i;
		ArrayList<member_node> m;
		String n;
		ArrayList<organization_node> o;
	}
	
	interface Organization {
		@GET("/hrtx/clientcall/mobileApi/getTree")
		Content contributors(
		);
	}
	
	static class TwitterDemoTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			String uin = params[0];
			String skey = params[1];
			initClient(uin, skey);

			return null;
		}

	}
	
	public static void initClient(final String uin, final String skey) {
		Headers headers = new Headers() {			
			@Override
			public List<Header> get() {
				List<Header> headers = new ArrayList<Header>();
				StringBuilder Cookie = new StringBuilder().append("uin=o").append(uin).append(";")
						.append("skey=").append(skey).append(";").append("ptisp=ctc");
				headers.add(new Header("Cookie", Cookie.toString()));
				//headers.add(new Header("skey", skey));
				
				return headers;
			}
		};
		
		RestAdapter restAdapter = new RestAdapter.Builder()
			.setServer(API_URL)
			.setDebug(true)
			.setHeaders(headers)
			.build();

		// Create an instance API interface.
		Organization organization = restAdapter.create(Organization.class);

		// Fetch and print a list of the contributors to this library.
		Content content = organization.contributors();
		Log.d(TAG, "code" + content.code + "qquin" + content.qquin + "data" + content.data.i + content.data.n );
		dumpOrganization(content.data);
	}
	
	public static void dumpOrganization(organization_node org_node) {
		Log.d(TAG, "name" + org_node.n);
		if (org_node.o.size() > 0) {
			for (organization_node to : org_node.o) {
				dumpOrganization(to);
			}
		}
		if (org_node.m.size() > 0) {
			for (member_node tm : org_node.m) {
				Log.d(TAG, "member name is " + tm.n);
			}
		}
	}
	
	public static void run(final String uin, final String skey) {
		new TwitterDemoTask().execute(uin, skey);
	}
}
