package com.example.restful;

import com.example.restful.data.OrgModelHelper;

import oicq.wlogin_sdk.request.WUserSigInfo;
import oicq.wlogin_sdk.request.WloginLastLoginInfo;
import oicq.wlogin_sdk.request.WtloginHelper;
import oicq.wlogin_sdk.request.WtloginListener;
import oicq.wlogin_sdk.tools.util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static String mSkey;
	private static String mAccount;
	
	private EditText account;
	private EditText password;
	private Button btn_login;
	
	private WtloginHelper mLoginHelper;
	
	Handler mHandler = new Handler();
	private static final long mAppid = 3000401L;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLoginHelper = new LoginHelper.Builder()
							.setContext(getApplicationContext())
							.setHostname("121.14.80.26")
							.setTimeoutTime(0)
							.setListener(mLoginListener).build();
		
		account = (EditText) findViewById(R.id.editTextAccount);
		password = (EditText) findViewById(R.id.editTextPwd);
		account.setText("2355200810");
		password.setText("123@hrtx");
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login(account.getText().toString(), password.getText().toString());
			}
		});
		
		Button btn_query = (Button) findViewById(R.id.btn_query);
		btn_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrgModelHelper modelHelper = new OrgModelHelper(MainActivity.this);
				
			}
		});
				
	}
	
	private void login(String account, String password) {
		WUserSigInfo signInfo = new WUserSigInfo();
		WloginLastLoginInfo info = mLoginHelper.GetLastLoginInfo();
		mLoginHelper.SetTkTimeOut(0); 
		if (mLoginHelper.IsNeedLoginWithPasswd(account, mAppid)) {
			signInfo._userPasswdSig = mLoginHelper.GetA1ByAccount(account, mAppid);
			if (info != null && signInfo._userPasswdSig != null) {
				mLoginHelper.GetStWithPasswd(account, mAppid, "", signInfo, 0);
			} else {
				mLoginHelper.GetStWithPasswd(account, mAppid, password, signInfo, 0);
			}
		} else {
			mLoginHelper.GetStWithoutPasswd(account, mAppid, mAppid, signInfo, 0);
		}
			
	}
	 private WtloginListener mLoginListener = new WtloginListener(){
	    	
			public void OnGetStWithPasswd(String userAccount, long dwSrcAppid, int dwMainSigMap, 
					long dwSubDstAppid,  String userPasswd, WUserSigInfo userSigInfo, int ret){
				if(ret == util.S_GET_IMAGE){

				} else if(ret == util.S_SUCCESS){
					mSkey = new String(userSigInfo._sKey);
					mAccount = userAccount;
					//loginSuccess(userAccount);
					Log.d(TAG, "login success");
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							onLoginSuccess();
							
						}
					});
					
				} else if(ret == util.E_NO_RET){
					showDialog(MainActivity.this, "登录超时");
				} else{
					showDialog(MainActivity.this, mLoginHelper.GetLastErrMsg());
				}
			}
			
			public void OnGetStWithoutPasswd(String userAccount, long dwSrcAppid, long dwDstAppid,int dwMainSigMap,long dwSubDstAppid, WUserSigInfo userSigInfo, int ret)
			{ 
			
				if (ret == util.S_SUCCESS) {
					mSkey = new String(userSigInfo._sKey);
					mAccount = userAccount;
					Log.d(TAG, "login success");
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							onLoginSuccess();
							
						}
					});
					
					
				} else if(ret == util.E_NO_RET){
					showDialog(MainActivity.this, "登录超时");
				} else {
					showDialog(MainActivity.this, mLoginHelper.GetLastErrMsg());
				}			 
			}		
			 
		};
		
		private void onLoginSuccess() {
			OrgInfoFetchService.startService(MainActivity.this, mAccount, mSkey);
			//OrganizationFetchService.startService(MainActivity.this, mAccount, mSkey);
		}
		
		private void showDialog(Context context, String strMsg) {  
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);   
	        builder.setTitle("提示");  
	        builder.setMessage(strMsg);  
	        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int whichButton) {  
	                         dialog.dismiss();
	                    }      
	                }); 
	        
	        builder.show();      
	    }
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static String getSkey() {
		return mSkey;
	}
	
	public static String getAccount() {
		return mAccount;
	}
	
	
}
