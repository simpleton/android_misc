/**
 * 
 */
package com.example.dblocktest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author simsun
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "JUSTANAME";
	private static final String TAG = "DatabaseHelper";
	private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper helper;
    public static final String[] SESSION_COLS = new String[]
            {
                    "id",
                    "description"
            };
    
	public static DatabaseHelper getInstance(Context context) {
		synchronized (DatabaseHelper.class) {
			if (helper == null) {
				helper = new DatabaseHelper(context);
			}
		}
		return helper;
	}
	
    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        String createSessionTable =
                "CREATE TABLE `session` " +
                        "(" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`description` VARCHAR" +
                        ") ";
        db.execSQL(createSessionTable);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	 public void createSession(String desc) {
		 final SQLiteDatabase writableDatabase = getWritableDatabase();
	     final ContentValues contentValues = new ContentValues();

	     contentValues.put("description", desc);

	     writableDatabase.insertOrThrow("session", null, contentValues);
	 }
}
