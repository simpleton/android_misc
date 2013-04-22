/**
 * 
 */
package com.example.restful_example;

import android.content.Context;
import oicq.wlogin_sdk.request.WtloginHelper;
import oicq.wlogin_sdk.request.WtloginListener;

/**
 * @author simsun
 *
 */
public class LoginHelper {
	public static class Builder {
		private WtloginListener listener;
		private int Timeout_time;
		private String hostname ;
		private Context mContext;
		
		public Builder setContext(Context context) {
			if (context == null) throw new NullPointerException("context");
			this.mContext = context;
			return this;
		}
		
		public Builder setListener(WtloginListener listen) {
			if (listen == null) throw new NullPointerException("listen");
			this.listener = listen;
			return this;
		}
		
		public Builder setTimeoutTime(int time) {
			this.Timeout_time = time;
			return this;
		}
		
		public Builder setHostname(String hostname) {
			if (hostname == null) throw new NullPointerException("hostname");
			this.hostname = hostname;
			return this;
		}
		
		public WtloginHelper build() {
			WtloginHelper mLoginHelper = new WtloginHelper(mContext);
			mLoginHelper.SetListener(this.listener);
			mLoginHelper.SetTestHost(1, this.hostname);
			mLoginHelper.SetTkTimeOut(this.Timeout_time);
			return mLoginHelper;
		} 
	}
}
