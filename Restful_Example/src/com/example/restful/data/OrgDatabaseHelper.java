/**
 * 
 */
package com.example.restful.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.restful.data.OrgClient.Department;
import com.example.restful.data.OrgContent.DB_Collegue;
import com.example.restful.data.OrgContent.DB_Department;
import com.example.restful.data.OrgContent.collegue_relation;
import com.example.restful.data.OrgContent.department_relation;

/**
 * @author simsun
 *
 */
public class OrgDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	private static final Boolean DEBUG = true;
	
	private static final String PREF_DB_NAME = "Orgnazition_";
	private static OrgDatabaseHelper helper;
	// help u calculate correct member count of target department
	private memberCountCalculater memCounterCalc;
	private static String mDBName;
	@SuppressLint("NewApi")
	public static OrgDatabaseHelper getInstance(Context context, String account_uin) {
		String db_name = PREF_DB_NAME + account_uin;
		synchronized (OrgDatabaseHelper.class) {
			if (helper == null) {
				mDBName = db_name;
				helper = new OrgDatabaseHelper(context, db_name);
			} else {
				//android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH
				if (android.os.Build.VERSION.SDK_INT >= 14) {
					if(helper.getDatabaseName() != db_name) {
						helper.close();
						helper = new OrgDatabaseHelper(context, db_name);

					}
				} else {
					if (mDBName != db_name) {
						helper.close();
						helper = new OrgDatabaseHelper(context, db_name);
					}
				}
			}
		}
		return helper;
	}
	
    public OrgDatabaseHelper(Context context, String db_name)
    {
   		super(context, db_name, null, OrgProvider.DATABASE_VERSION);				
    }

    public void setRootDepartment(Department root) {
    	if (memCounterCalc == null || root != memCounterCalc.getRoot()) {
    		memCounterCalc = new memberCountCalculater(root);
    	}
    }

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Creating OrgProvider database");

        // Create all tables here; each class has its own method
        if (DEBUG) {
            Log.d(TAG, "Department | createTable start");
        }
        DB_Department.createTable(db);
        if (DEBUG) {
            Log.d(TAG, "Department | createTable end");
        }
        if (DEBUG) {
            Log.d(TAG, "Collegue | createTable start");
        }
        DB_Collegue.createTable(db);
        if (DEBUG) {
            Log.d(TAG, "Collegue | createTable end");
        }
        if (DEBUG) {
            Log.d(TAG, "collegue_relation | createTable start");
        }
        collegue_relation.createTable(db);
        if (DEBUG) {
            Log.d(TAG, "collegue_relation | createTable end");
        }
        if (DEBUG) {
            Log.d(TAG, "department_relation | createTable start");
        }
        department_relation.createTable(db);
        if (DEBUG) {
            Log.d(TAG, "department_relation | createTable end");
        }
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 // Upgrade all tables here; each class has its own method
        if (DEBUG) {
            Log.d(TAG, "Department | upgradeTable start");
        }
        DB_Department.upgradeTable(db, oldVersion, newVersion);
        if (DEBUG) {
            Log.d(TAG, "Department | upgradeTable end");
        }
        if (DEBUG) {
            Log.d(TAG, "Collegue | upgradeTable start");
        }
        DB_Collegue.upgradeTable(db, oldVersion, newVersion);
        if (DEBUG) {
            Log.d(TAG, "Collegue | upgradeTable end");
        }
        if (DEBUG) {
            Log.d(TAG, "collegue_relation | upgradeTable start");
        }
        collegue_relation.upgradeTable(db, oldVersion, newVersion);
        if (DEBUG) {
            Log.d(TAG, "collegue_relation | upgradeTable end");
        }
        if (DEBUG) {
            Log.d(TAG, "department_relation | upgradeTable start");
        }
        department_relation.upgradeTable(db, oldVersion, newVersion);
        if (DEBUG) {
            Log.d(TAG, "department_relation | upgradeTable end");
        }
	}
}
