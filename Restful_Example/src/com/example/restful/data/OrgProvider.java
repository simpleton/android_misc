package com.example.restful.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import com.example.restful.data.OrgContent.DB_Collegue;
import com.example.restful.data.OrgContent.DB_Department;
import com.example.restful.data.OrgContent.collegue_relation;
import com.example.restful.data.OrgContent.department_relation;

/**
 *
 */
public final class OrgProvider {

    private static final String TAG = "OrgProvider";

    private static final boolean DEBUG = true;

    protected static final String DATABASE_NAME = "OrgProvider.db";

    public static final String AUTHORITY = "com.example.restful.provider.OrgProvider";

    static {
        Uri.parse("content://" + AUTHORITY + "/integrityCheck");
    }

    // Version 1 : Creation of the database
    public static final int DATABASE_VERSION = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private enum UriType {
        DEPARTMENT( "#/" + DB_Department.TABLE_NAME , DB_Department.TABLE_NAME, DB_Department.TYPE_ELEM_TYPE),
        COLLEGUE("#/" + DB_Collegue.TABLE_NAME, DB_Collegue.TABLE_NAME, DB_Collegue.TYPE_ELEM_TYPE),
        COLLEGUE_RELATION("#/" + collegue_relation.TABLE_NAME, collegue_relation.TABLE_NAME, collegue_relation.TYPE_ELEM_TYPE),
        DEPARTMENT_RELATION("#/" + department_relation.TABLE_NAME, department_relation.TABLE_NAME, department_relation.TYPE_ELEM_TYPE);

        private String mTableName;
        private String mType;

        UriType(String matchPath, String tableName, String type) {
            mTableName = tableName;
            mType = type;
            sUriMatcher.addURI(AUTHORITY, matchPath, ordinal());
        }

        String getTableName() {
            return mTableName;
        }

        String getType() {
            return mType;
        }
    }

    static {
        // Ensures UriType is initialized
        UriType.values();
    }

    private static UriType matchUri(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match < 0) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return UriType.class.getEnumConstants()[match];
    }
 
    public static int delete(Uri uri, String selection, String[] selectionArgs, Context ct) {

        UriType uriType = matchUri(uri);
        for (String seg: uri.getPathSegments())
        	Log.d(TAG, seg);
        String account_uin = uri.getPathSegments().get(0);
        // Pick the correct database for this operation
        SQLiteDatabase db = OrgDatabaseHelper.getInstance(ct, account_uin).getWritableDatabase();

        if (DEBUG) {
            Log.d(TAG, "delete: uri=" + uri + ", match is " + uriType.name());
        }

        int result = -1;

        switch (uriType) {
            case DEPARTMENT:
            case COLLEGUE:
            case COLLEGUE_RELATION:
            case DEPARTMENT_RELATION:
                result = db.delete(uriType.getTableName(), selection, selectionArgs);
                break;
        }

        //getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    public String getType(Uri uri) {
        return matchUri(uri).getType();
    }

    public static Uri insert(Uri uri, ContentValues values, Context ct) {

        UriType uriType = matchUri(uri);

        // Pick the correct database for this operation
        String account_uin = uri.getPathSegments().get(0);
        // Pick the correct database for this operation
        SQLiteDatabase db = OrgDatabaseHelper.getInstance(ct, account_uin).getWritableDatabase();
        long id;

        if (DEBUG) {
            Log.d(TAG, "insert: uri=" + uri + ", match is " + uriType.name());
        }

        Uri resultUri;

        switch (uriType) {
            case DEPARTMENT:
            case COLLEGUE:
            case COLLEGUE_RELATION:
            case DEPARTMENT_RELATION:
                id = db.insert(uriType.getTableName(), "foo", values);
                resultUri = id == -1 ? null : ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Notify with the base uri, not the new uri (nobody is watching a new
        // record)
        //getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }
    /*
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) 
            throws OperationApplicationException {
    	String account_uin = uri.getPathSegments().get(1);
        // Pick the correct database for this operation
        SQLiteDatabase db = OrgDatabaseHelper.getInstance(ct, account_uin).getWritableDatabase();
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
                db.yieldIfContendedSafely();
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }
	*/
    
    public static int bulkInsert(Uri uri, ContentValues[] values, Context ct) {

        UriType uriType = matchUri(uri);

        // Pick the correct database for this operation
        String account_uin = uri.getPathSegments().get(0);
        // Pick the correct database for this operation
        SQLiteDatabase db = OrgDatabaseHelper.getInstance(ct, account_uin).getWritableDatabase();

        if (DEBUG) {
            Log.d(TAG, "bulkInsert: uri=" + uri + ", match is " + uriType.name());
        }

        int numberInserted = 0;
        SQLiteStatement insertStmt;

        db.beginTransaction();
        try {
            switch (uriType) {
                case DEPARTMENT:
                    insertStmt = db.compileStatement(DB_Department.getBulkInsertString());
                    for (ContentValues value : values) {
                        DB_Department.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (DEBUG) {
                        Log.d(TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                case COLLEGUE:
                    insertStmt = db.compileStatement(DB_Collegue.getBulkInsertString());
                    for (ContentValues value : values) {
                        DB_Collegue.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (DEBUG) {
                        Log.d(TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                case COLLEGUE_RELATION:
                    insertStmt = db.compileStatement(collegue_relation.getBulkInsertString());
                    for (ContentValues value : values) {
                        collegue_relation.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (DEBUG) {
                        Log.d(TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                case DEPARTMENT_RELATION:
                    insertStmt = db.compileStatement(department_relation.getBulkInsertString());
                    for (ContentValues value : values) {
                        department_relation.bindValuesInBulkInsert(insertStmt, value);
                        insertStmt.execute();
                        insertStmt.clearBindings();
                    }
                    insertStmt.close();
                    db.setTransactionSuccessful();
                    numberInserted = values.length;

                    if (DEBUG) {
                        Log.d(TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }
        } finally {
            db.endTransaction();
        }

        // Notify with the base uri, not the new uri (nobody is watching a new
        // record)
        //context.getContentResolver().notifyChange(uri, null);
        return numberInserted;
    }


    public static Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, 
            String sortOrder, Context ct) {

        Cursor c = null;
        UriType uriType = matchUri(uri);
        // Pick the correct database for this operation
        String account_uin = uri.getPathSegments().get(0);
        // Pick the correct database for this operation
        SQLiteDatabase db = OrgDatabaseHelper.getInstance(ct, account_uin).getReadableDatabase();

        if (DEBUG) {
            Log.d(TAG, "query: uri=" + uri + ", match is " + uriType.name());
        }

        switch (uriType) {
            case DEPARTMENT:
            case COLLEGUE:
            case COLLEGUE_RELATION:
            case DEPARTMENT_RELATION:
                c = db.query(uriType.getTableName(), projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
        }

//        if ((c != null) && !isTemporary()) {
//            c.setNotificationUri(getContext().getContentResolver(), uri);
//        }
        return c;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs, Context ct) {

        UriType uriType = matchUri(uri);

        String account_uin = uri.getPathSegments().get(0);
        SQLiteDatabase db = OrgDatabaseHelper.getInstance(ct, account_uin).getWritableDatabase();

        if (DEBUG) {
            Log.d(TAG, "update: uri=" + uri + ", match is " + uriType.name());
        }

        int result = -1;

        switch (uriType) {
            case DEPARTMENT:
            case COLLEGUE:
            case COLLEGUE_RELATION:
            case DEPARTMENT_RELATION:
                result = db.update(uriType.getTableName(), values, selection, selectionArgs);
                break;
        }

        //getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}
