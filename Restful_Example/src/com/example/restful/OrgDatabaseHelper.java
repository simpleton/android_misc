///**
// * 
//》
//package com.example.restful;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.example.restful.OrgClient.Collegue;
//import com.example.restful.OrgClient.Department;
//
//
//public class OrgDatabaseHelper extends SQLiteOpenHelper {
//	private static final String TAG = "DatabaseHelper";
//	private static final Boolean DEBUG = true;
//	//DB name and version
//	private static final String DB_NAME = "ORG_DB";
//	private static final int DATABASE_VERSION = 3;
//	
//	// Contacts table name
//    private static final String TABLE_DEPARTMENT = "department";
//    private static final String TABLE_COLLEGUE = "collegue";
//    private static final String TABLE_COLLEGUE_RELATION = "collegue_relation";
//    private static final String TABLE_DEPARTMENT_RELATION = "department_relation";
//    
//    // Contacts Table Columns names
//    private static final String KEY_ID = "_id";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_UIN = "uin";
//    private static final String KEY_MOBILE = "mobile_phone";
//    private static final String KEY_PH_NO = "phone_number";
//    private static final String KEY_TIMESTAMP = "timestamp";
//    private static final String KEY_COLLEGUE_COUNT = "collegue_count";
//    private static final String KEY_PINYIN = "pinyin";
//    private static final String KEY_SHUANGPIN = "shuangpin";
//    private static final String KEY_EN_NAME = "english_name";
//    private static final String KEY_GENDER = "gender";
//    private static final String KEY_DUTY = "duty";
//    private static final String KEY_COLLEGUE_UIN = "collegue_uin";
//    private static final String KEY_DEPARTMENT_UIN = "department_uin";
//    private static final String KEY_CHILD_DEPARTMENT = "child_department_uin";
//    private static final String KEY_EMAIL = "email";
//	private static OrgDatabaseHelper helper;
//    
//	//URI Matcher
//	private static final int URI_ORGINFO_ALL = 1;
//	private static final int URI_DEPARTMENT = 2;
//	private static final int URI_COLLEGUE = 3;
//	private static final int URI_DEPARTMENT_ROOT = 4;
//	
//	private static final String authority = "com.tencent.eim";
//	
//	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//	static {
//		sURIMatcher.addURI(authority, "organization/#/tree", URI_ORGINFO_ALL);
//		sURIMatcher.addURI(authority, "organization/#/department/#", URI_DEPARTMENT);
//		sURIMatcher.addURI(authority, "organization/#/collegue/#", URI_COLLEGUE);
//		sURIMatcher.addURI(authority, "organization/#/root", URI_DEPARTMENT_ROOT);
//	}
//	
//	// help u calculate correct member count of target department
//	private memberCountCalculater memCounterCalc;
//	
//	public static OrgDatabaseHelper getInstance(Context context) {
//		synchronized (OrgDatabaseHelper.class) {
//			if (helper == null) {
//				helper = new OrgDatabaseHelper(context);
//			}
//		}
//		return helper;
//	}
//	
//    public OrgDatabaseHelper(Context context)
//    {
//        super(context, DB_NAME, null, DATABASE_VERSION);
//    }
//
//    public void setRootDepartment(Department root) {
//    	if (memCounterCalc == null || root != memCounterCalc.getRoot()) {
//    		memCounterCalc = new memberCountCalculater(root);
//    	}
//    }
//
//	/* (non-Javadoc)
//	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
//	 */
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		// TODO Auto-generated method stub
//        String createDepartmentTable =
//                "CREATE TABLE " + TABLE_DEPARTMENT +
//                        " (" +
//                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                        KEY_UIN + " INTEGER UNIQUE," +
//                        KEY_NAME + " TEXT NOT NULL," +
//                        KEY_TIMESTAMP + " INTEGER NOT NULL DEFAULT 0," +
//                        KEY_COLLEGUE_COUNT + " INTEGER," +
//                        KEY_SHUANGPIN + " TEXT," +
//                        KEY_PINYIN + " TEXT," +
//                        KEY_EN_NAME + " TEXT" +
//                        ") ";
//        
//        String createCollegueTable = 
//        		"CREATE TABLE " + TABLE_COLLEGUE +
//		        		"( " +
//		        		KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//		        		KEY_UIN + " INTEGER UNIQUE," +
//		        		KEY_GENDER + " INTEGER," +
//		        		KEY_NAME + " TEXT," +
//		        		KEY_MOBILE + " TEXT," +
//		        		KEY_PH_NO + " TEXT," +
//		        		KEY_EMAIL + " TEXT," +
//		        		KEY_DUTY + " TEXT," +
//		        		KEY_TIMESTAMP + " INTEGER NOT NULL DEFAULT 0," +
//		        		KEY_SHUANGPIN + " TEXT," +
//		        		KEY_PINYIN + " TEXT," +
//		        		KEY_EN_NAME + " TEXT" +
//		        		")" ;
//        String createCollegueRelationTable = 
//        		"CREATE TABLE "+ TABLE_COLLEGUE_RELATION +
//        				"(" +
//        				KEY_COLLEGUE_UIN + " INTEGER," +
//        				KEY_DEPARTMENT_UIN + " INTEGER," +
//        				" PRIMARY KEY (" + KEY_COLLEGUE_UIN + "," + KEY_DEPARTMENT_UIN + ")" +
//        				")";
//        
//        String createDepartmentRelationTable = 
//        		"CREATE TABLE " + TABLE_DEPARTMENT_RELATION + 
//        				"(" + 
//        				KEY_DEPARTMENT_UIN + " INTEGER," +
//        				KEY_CHILD_DEPARTMENT + " INTEGER," +
//        				" PRIMARY KEY (" + KEY_DEPARTMENT_UIN + "," + KEY_CHILD_DEPARTMENT + ")" +
//        				")";
//        
//        db.execSQL(createDepartmentTable);
//        db.execSQL(createCollegueTable);
//        db.execSQL(createCollegueRelationTable);
//        db.execSQL(createDepartmentRelationTable);
//	}
//
//	/* (non-Javadoc)
//	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
//	 */
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// lazy man just drop the tables.
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLEGUE);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENT);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLEGUE_RELATION);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENT_RELATION);
//		
//		onCreate(db);
//	}
//
//	 public void insertCollegue(OrgClient.Collegue collegue) {
//		 final SQLiteDatabase writableDatabase = getWritableDatabase();
//		 final ContentValues contentValues = new ContentValues();
//
//	     contentValues.put(KEY_DUTY, collegue.j);
//	     contentValues.put(KEY_UIN, collegue.q);
//	     contentValues.put(KEY_GENDER, collegue.g);
//	     contentValues.put(KEY_MOBILE, collegue.m);
//	     contentValues.put(KEY_NAME, collegue.n);
//	     contentValues.put(KEY_EN_NAME, collegue.u);
//	     contentValues.put(KEY_PH_NO, collegue.p);
//	     contentValues.put(KEY_EMAIL, collegue.e);
//	     
//	     writableDatabase.insertWithOnConflict(TABLE_COLLEGUE, null, 
//	    		 contentValues, SQLiteDatabase.CONFLICT_REPLACE);
//	 }
//	 
//	public void insertDepartment(OrgClient.Department department) {
//		final SQLiteDatabase writableDatabase = getWritableDatabase();
//		final ContentValues contentValues = new ContentValues();
//
//		contentValues.put(KEY_UIN, department.i);
//		contentValues.put(KEY_NAME, department.n);
//	
//		contentValues.put(KEY_COLLEGUE_COUNT, memCounterCalc.calcMember(department));
//
//
//		writableDatabase.insertWithOnConflict(TABLE_DEPARTMENT, null,
//				contentValues, SQLiteDatabase.CONFLICT_REPLACE);
//	}
//	
//	//FIXME: fix the standard API
//	public void bulkInsertOrgInfo(Department root, Context ctz) {
//		if (DEBUG) Log.e(TAG, "bulkInsertOrgInfo start");
//		List<ContentValues> departmentList = new ArrayList<ContentValues>();
//		List<ContentValues> collegueList = new ArrayList<ContentValues>();
//		
//		buildContentValuesList(departmentList, collegueList, root);
//		
//		final SQLiteDatabase writableDB = getWritableDatabase();
//		writableDB.beginTransaction();
//		try {
//			for (ContentValues cv : departmentList) {
//				long newId = writableDB.insertWithOnConflict(TABLE_DEPARTMENT, null, cv,
//						SQLiteDatabase.CONFLICT_REPLACE);
//				if (newId <= 0) {
//					throw new SQLException("Failed to insert row at" + newId);
//				}
//			}
//			
//			for (ContentValues cv : collegueList) {
//				long newId = writableDB.insertWithOnConflict(TABLE_COLLEGUE, null, 
//						cv, SQLiteDatabase.CONFLICT_REPLACE); 
//				if (newId <= 0) {
//					throw new SQLException("Failed to insert row at" + newId);
//				}
//			}
//			
//			writableDB.setTransactionSuccessful();
//			//TODO: if u need observer, plz look at here!	
//			//ctz.getContentResolver().notifyChange(uri, observer);
//		} finally {
//			writableDB.endTransaction();
//		}
//		if (DEBUG) Log.e(TAG, "bulkInsertOrgInfo end");
//	}
//	
//	private void buildContentValuesList(List<ContentValues> departmentList, List<ContentValues> collegueList,
//			Department root) {
//		//insert root's member
//		for (Collegue collegue : root.m) {
//			final ContentValues contentValues = new ContentValues();
//			contentValues.put(KEY_DUTY, collegue.j);
//			contentValues.put(KEY_UIN, collegue.q);
//			contentValues.put(KEY_GENDER, collegue.g);
//			contentValues.put(KEY_MOBILE, collegue.m);
//			contentValues.put(KEY_NAME, collegue.n);
//			contentValues.put(KEY_EN_NAME, collegue.u);
//			contentValues.put(KEY_PH_NO, collegue.p);
//			contentValues.put(KEY_EMAIL, collegue.e);
//			
//			collegueList.add(contentValues);
//		}
//		
//		//insert root department node
//		final ContentValues contentValues = new ContentValues();
//		contentValues.put(KEY_UIN, root.i);
//		contentValues.put(KEY_NAME, root.n);
//		contentValues.put(KEY_COLLEGUE_COUNT, memCounterCalc.calcMember(root));
//		
//		departmentList.add(contentValues);
//		
//		//recursively visit root's child department
//		for (Department department: root.o) {			
//			buildContentValuesList(departmentList, collegueList, department);
//		}
//	}
//
//
//}
//
