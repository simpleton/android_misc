// Copyright 2012 Square, Inc.
package com.example.restful_example;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import retrofit.http.GET;
import retrofit.http.GsonConverter;
import retrofit.http.Name;
import retrofit.http.RestAdapter;
import retrofit.http.Server;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class Client {
	private static final String TAG = "Client";
 	 private static final String API_URL = "https://api.github.com";

  	class Contributor {
    	String login;
    	int contributions;
    	String id;
    	String url;
 	 }

  interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(
        @Name("owner") String owner,
        @Name("repo") String repo
    );
  }

	static class TwitterDemoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			main1(null);

			return null;
		}

	}
  public static void main1(String... args) {
    // Create a very simple REST adapter which points the GitHub API endpoint.
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setServer(API_URL)
        .build();

    // Create an instance of our GitHub API interface.
    GitHub github = restAdapter.create(GitHub.class);

    // Fetch and print a list of the contributors to this library.
    List<Contributor> contributors = github.contributors("square", "retrofit");
    for (Contributor contributor : contributors) {
      Log.d(TAG, contributor.login + " (" + contributor.contributions + ")" + contributor.id + contributor.url) ;
    }
  }
  
  public static void main() {
		new TwitterDemoTask().execute();
  }
}
