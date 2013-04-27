package com.example.restful;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.restful.data.OrgClient;
import com.example.restful.data.OrgClient.Content;
import com.example.restful.data.OrgClient.Department;
import com.example.restful.data.OrgModelHelper;

public class OrgInfoFetchService extends IntentService {
	private static final String TAG = "OrganizationFetchService";
	
	public static final String UIN = "uin";
	public static final String SKEY = "skey";
	
	public String AccountUin;
	public OrgInfoFetchService() {
		super(TAG);
		Log.d(TAG, "IntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent");
		final String uin = intent.getStringExtra(UIN);
		final String skey = intent.getStringExtra(SKEY);
		if (uin != null && uin.length() > 0 && skey != null
				&& skey.length() > 0) {
			AccountUin = uin;
			Content orgContent = OrgClient.GET(uin, skey);
			
			Department corp = orgContent.data;
			saveDepartment(corp);						
		}
	}
	
	/**
	 * bulk insert, fast
	 * @param corp
	 */
	public void saveDepartment(Department corp) {
		OrgModelHelper model = new OrgModelHelper(OrgInfoFetchService.this, corp);
		model.bulkInsertOrgInfo(corp, AccountUin);
	}

	
	public static void startService(Context ctz, String uin, String skey) {
		Intent intent = new Intent(ctz, OrgInfoFetchService.class);
		intent.putExtra(UIN, uin);
		intent.putExtra(SKEY, skey);
		ctz.startService(intent);
	}
}
