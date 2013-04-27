package com.example.restful.data;

import com.example.restful.data.util.ColumnMetadata;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * 
 */

public abstract class OrgContent {

	public static final Uri CONTENT_URI = Uri.parse("content://"+ OrgProvider.AUTHORITY);

	private OrgContent() {
	}

	/**
	 * Created in version 1
	 */
	public static final class DB_Department extends OrgContent {

		private static final String TAG = "DB_Department";

		public static final String TABLE_NAME = "department";
		public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/org-department";
		public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/org-department";

		public static final Uri CONTENT_URI = Uri.parse(OrgContent.CONTENT_URI
				+ "/" + TABLE_NAME);

		public static enum Columns implements ColumnMetadata {
			_ID(BaseColumns._ID, "integer PRIMARY KEY AUTOINCREMENT"), 
			UIN("uin", "integer UNIQUE ON CONFLICT REPLACE"), 
			NAME("name", "text"), 
			TIMESTAMP("timestamp", "integer"), 
			COLLEGUE_COUNT("collegue_count", "integer"), 
			SHUANGPIN("shuangpin", "text"), 
			PINYIN("pinyin", "text"), 
			EN_NAME("en_name", "text");

			private final String mName;
			private final String mType;

			private Columns(String name, String type) {
				mName = name;
				mType = type;
			}

			@Override
			public int getIndex() {
				return ordinal();
			}

			@Override
			public String getName() {
				return mName;
			}

			@Override
			public String getType() {
				return mType;
			}
		}

		public static final String[] PROJECTION = new String[] {
				Columns._ID.getName(), Columns.UIN.getName(),
				Columns.NAME.getName(), Columns.TIMESTAMP.getName(),
				Columns.COLLEGUE_COUNT.getName(), Columns.SHUANGPIN.getName(),
				Columns.PINYIN.getName(), Columns.EN_NAME.getName() };

		private DB_Department() {
			// No private constructor
		}

		public static void createTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ Columns._ID.getName() + " " + Columns._ID.getType()
					+ ", " + Columns.UIN.getName() + " "
					+ Columns.UIN.getType() + ", " + Columns.NAME.getName()
					+ " " + Columns.NAME.getType() + ", "
					+ Columns.TIMESTAMP.getName() + " "
					+ Columns.TIMESTAMP.getType() + ", "
					+ Columns.COLLEGUE_COUNT.getName() + " "
					+ Columns.COLLEGUE_COUNT.getType() + ", "
					+ Columns.SHUANGPIN.getName() + " "
					+ Columns.SHUANGPIN.getType() + ", "
					+ Columns.PINYIN.getName() + " " + Columns.PINYIN.getType()
					+ ", " + Columns.EN_NAME.getName() + " "
					+ Columns.EN_NAME.getType() + 
					");");

			db.execSQL("CREATE INDEX department_uin on " + TABLE_NAME + "("
					+ Columns.UIN.getName() + ");");
			db.execSQL("CREATE INDEX department_name on " + TABLE_NAME + "("
					+ Columns.NAME.getName() + ");");
		}

		// Version 1 : Creation of the table
		public static void upgradeTable(SQLiteDatabase db, int oldVersion,
				int newVersion) {

			if (oldVersion < 1) {
				Log.i(TAG, "Upgrading from version " + oldVersion + " to "
						+ newVersion + ", data will be lost!");

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
				createTable(db);
				return;
			}

			if (oldVersion != newVersion) {
				throw new IllegalStateException(
						"Error upgrading the database to version " + newVersion);
			}
		}

		static String getBulkInsertString() {
			return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
					.append(" ( ")
					.append(Columns.UIN.getName()).append(", ")
					.append(Columns.NAME.getName()).append(", ")
					.append(Columns.TIMESTAMP.getName()).append(", ")
					.append(Columns.COLLEGUE_COUNT.getName()).append(", ")
					.append(Columns.SHUANGPIN.getName()).append(", ")
					.append(Columns.PINYIN.getName()).append(", ")
					.append(Columns.EN_NAME.getName())
					.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?)").toString();
		}

		static void bindValuesInBulkInsert(SQLiteStatement stmt,
				ContentValues values) {
			int i = 1;
			String value;
			Long num;
			stmt.bindLong(i++, values.getAsLong(Columns.UIN.getName()));
			value = values.getAsString(Columns.NAME.getName());
			stmt.bindString(i++, value != null ? value : "");
			num = values.getAsLong(Columns.TIMESTAMP.getName());
			stmt.bindLong(i++, num != null ? num : 0);
			num = values.getAsLong(Columns.COLLEGUE_COUNT.getName());
			stmt.bindLong(i++, num != null ? num : 0);
			value = values.getAsString(Columns.SHUANGPIN.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.PINYIN.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.EN_NAME.getName());
			stmt.bindString(i++, value != null ? value : "");
		}
	}

	/**
	 * Created in version 1
	 */
	public static final class DB_Collegue extends OrgContent {

		private static final String LOG_TAG = "DB_Collegue";

		public static final String TABLE_NAME = "collegue";
		public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/org-collegue";
		public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/org-collegue";

		public static final Uri CONTENT_URI = Uri.parse(OrgContent.CONTENT_URI
				+ "/" + TABLE_NAME);

		public static enum Columns implements ColumnMetadata {
			_ID(BaseColumns._ID, "integer PRIMARY KEY AUTOINCREMENT"), 
			UIN("uin", "integer UNIQUE ON CONFLICT REPLACE"), 
			NAME("name", "text"), 
			TIMESTAMP("timestamp", "integer"), 
			GENDER("gender", "integer"), 
			MOBILE_PHONE("mobile_phone", "text"), 
			PHONE("phone", "text"), 
			EMAIL("email", "text"), 
			DUTY("duty", "text"), 
			SHUANGPIN("shuangpin", "text"), 
			PINYIN("pinyin", "text"), 
			ENGLISH_NAME("english_name", "text");

			private final String mName;
			private final String mType;

			private Columns(String name, String type) {
				mName = name;
				mType = type;
			}

			@Override
			public int getIndex() {
				return ordinal();
			}

			@Override
			public String getName() {
				return mName;
			}

			@Override
			public String getType() {
				return mType;
			}
		}

		public static final String[] PROJECTION = new String[] {
				Columns._ID.getName(), Columns.UIN.getName(),
				Columns.NAME.getName(), Columns.TIMESTAMP.getName(),
				Columns.GENDER.getName(), Columns.MOBILE_PHONE.getName(),
				Columns.PHONE.getName(), Columns.EMAIL.getName(),
				Columns.DUTY.getName(), Columns.SHUANGPIN.getName(),
				Columns.PINYIN.getName(), Columns.ENGLISH_NAME.getName() };

		private DB_Collegue() {
			// No private constructor
		}

		public static void createTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ Columns._ID.getName() + " " + Columns._ID.getType()
					+ ", " + Columns.UIN.getName() + " "
					+ Columns.UIN.getType() + ", " + Columns.NAME.getName()
					+ " " + Columns.NAME.getType() + ", "
					+ Columns.TIMESTAMP.getName() + " "
					+ Columns.TIMESTAMP.getType() + ", "
					+ Columns.GENDER.getName() + " " + Columns.GENDER.getType()
					+ ", " + Columns.MOBILE_PHONE.getName() + " "
					+ Columns.MOBILE_PHONE.getType() + ", "
					+ Columns.PHONE.getName() + " " + Columns.PHONE.getType()
					+ ", " + Columns.EMAIL.getName() + " "
					+ Columns.EMAIL.getType() + ", " + Columns.DUTY.getName()
					+ " " + Columns.DUTY.getType() + ", "
					+ Columns.SHUANGPIN.getName() + " "
					+ Columns.SHUANGPIN.getType() + ", "
					+ Columns.PINYIN.getName() + " " + Columns.PINYIN.getType()
					+ ", " + Columns.ENGLISH_NAME.getName() + " "
					+ Columns.ENGLISH_NAME.getType() + 
					");");

			db.execSQL("CREATE INDEX collegue_uin on " + TABLE_NAME + "("
					+ Columns.UIN.getName() + ");");
			db.execSQL("CREATE INDEX collegue_name on " + TABLE_NAME + "("
					+ Columns.NAME.getName() + ");");
		}

		// Version 1 : Creation of the table
		public static void upgradeTable(SQLiteDatabase db, int oldVersion,
				int newVersion) {

			if (oldVersion < 1) {
				Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to "
						+ newVersion + ", data will be lost!");

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
				createTable(db);
				return;
			}

			if (oldVersion != newVersion) {
				throw new IllegalStateException(
						"Error upgrading the database to version " + newVersion);
			}
		}

		static String getBulkInsertString() {
			return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
					.append(" ( ")
					.append(Columns.UIN.getName()).append(", ")
					.append(Columns.NAME.getName()).append(", ")
					.append(Columns.TIMESTAMP.getName()).append(", ")
					.append(Columns.GENDER.getName()).append(", ")
					.append(Columns.MOBILE_PHONE.getName()).append(", ")
					.append(Columns.PHONE.getName()).append(", ")
					.append(Columns.EMAIL.getName()).append(", ")
					.append(Columns.DUTY.getName()).append(", ")
					.append(Columns.SHUANGPIN.getName()).append(", ")
					.append(Columns.PINYIN.getName()).append(", ")
					.append(Columns.ENGLISH_NAME.getName())
					.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
					.toString();
		}

		static void bindValuesInBulkInsert(SQLiteStatement stmt,
				ContentValues values) {
			int i = 1;
			String value;
			Long num;
			stmt.bindLong(i++, values.getAsLong(Columns.UIN.getName()));
			value = values.getAsString(Columns.NAME.getName());
			stmt.bindString(i++, value != null ? value : "");
			num = values.getAsLong(Columns.TIMESTAMP.getName());
			stmt.bindLong(i++, num != null ? num : 0);
			num = values.getAsLong(Columns.GENDER.getName());
			stmt.bindLong(i++, num != null ? num : 0);
			value = values.getAsString(Columns.MOBILE_PHONE.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.PHONE.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.EMAIL.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.DUTY.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.SHUANGPIN.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.PINYIN.getName());
			stmt.bindString(i++, value != null ? value : "");
			value = values.getAsString(Columns.ENGLISH_NAME.getName());
			stmt.bindString(i++, value != null ? value : "");
		}
	}

	/**
	 * Created in version 1
	 */
	public static final class collegue_relation extends OrgContent {

		private static final String LOG_TAG = collegue_relation.class
				.getSimpleName();

		public static final String TABLE_NAME = "collegue_relation";
		public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/org-collegue_relation";
		public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/org-collegue_relation";

		public static final Uri CONTENT_URI = Uri.parse(OrgContent.CONTENT_URI
				+ "/" + TABLE_NAME);

		public static enum Columns implements ColumnMetadata {
			COLLEGUE_UIN("collegue_uin", "integer"), 
			DEPARTMENT_UIN("department_uin", "integer");

			private final String mName;
			private final String mType;

			private Columns(String name, String type) {
				mName = name;
				mType = type;
			}

			@Override
			public int getIndex() {
				return ordinal();
			}

			@Override
			public String getName() {
				return mName;
			}

			@Override
			public String getType() {
				return mType;
			}
		}

		public static final String[] PROJECTION = new String[] {
				Columns.COLLEGUE_UIN.getName(),
				Columns.DEPARTMENT_UIN.getName() };

		private collegue_relation() {
			// No private constructor
		}

		public static void createTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ Columns.COLLEGUE_UIN.getName() + " "
					+ Columns.COLLEGUE_UIN.getType() + ", "
					+ Columns.DEPARTMENT_UIN.getName() + " "
					+ Columns.DEPARTMENT_UIN.getType() + ");");

			db.execSQL("CREATE INDEX collegue_relation_collegue_uin on "
					+ TABLE_NAME + "(" + Columns.COLLEGUE_UIN.getName() + ");");
			db.execSQL("CREATE INDEX collegue_relation_department_uin on "
					+ TABLE_NAME + "(" + Columns.DEPARTMENT_UIN.getName()
					+ ");");
		}

		// Version 1 : Creation of the table
		public static void upgradeTable(SQLiteDatabase db, int oldVersion,
				int newVersion) {

			if (oldVersion < 1) {
				Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to "
						+ newVersion + ", data will be lost!");

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
				createTable(db);
				return;
			}

			if (oldVersion != newVersion) {
				throw new IllegalStateException(
						"Error upgrading the database to version " + newVersion);
			}
		}

		static String getBulkInsertString() {
			return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
					.append(" ( ").append(Columns.COLLEGUE_UIN.getName())
					.append(", ").append(Columns.DEPARTMENT_UIN.getName())
					.append(" ) VALUES (?, ?)").toString();
		}

		static void bindValuesInBulkInsert(SQLiteStatement stmt,
				ContentValues values) {
			int i = 1;
			stmt.bindLong(i++, values.getAsLong(Columns.COLLEGUE_UIN.getName()));
			stmt.bindLong(i++,
					values.getAsLong(Columns.DEPARTMENT_UIN.getName()));
		}
	}

	/**
	 * Created in version 1
	 */
	public static final class department_relation extends OrgContent {

		private static final String LOG_TAG = department_relation.class
				.getSimpleName();

		public static final String TABLE_NAME = "department_relation";
		public static final String TYPE_ELEM_TYPE = "vnd.android.cursor.item/org-department_relation";
		public static final String TYPE_DIR_TYPE = "vnd.android.cursor.dir/org-department_relation";

		public static final Uri CONTENT_URI = Uri.parse(OrgContent.CONTENT_URI
				+ "/" + TABLE_NAME);

		public static enum Columns implements ColumnMetadata {
			DEPARTMENT_UIN("department_uin", "integer"), 
			CHILD_DEPARTMENT_UIN("child_department_uin", "integer");

			private final String mName;
			private final String mType;

			private Columns(String name, String type) {
				mName = name;
				mType = type;
			}

			@Override
			public int getIndex() {
				return ordinal();
			}

			@Override
			public String getName() {
				return mName;
			}

			@Override
			public String getType() {
				return mType;
			}
		}

		public static final String[] PROJECTION = new String[] {
				Columns.DEPARTMENT_UIN.getName(),
				Columns.CHILD_DEPARTMENT_UIN.getName() };

		private department_relation() {
			// No private constructor
		}

		public static void createTable(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
					+ Columns.DEPARTMENT_UIN.getName() + " "
					+ Columns.DEPARTMENT_UIN.getType() + ", "
					+ Columns.CHILD_DEPARTMENT_UIN.getName() + " "
					+ Columns.CHILD_DEPARTMENT_UIN.getType() + ");");

			db.execSQL("CREATE INDEX department_relation_department_uin on "
					+ TABLE_NAME + "(" + Columns.DEPARTMENT_UIN.getName()
					+ ");");
			db.execSQL("CREATE INDEX department_relation_child_department_uin on "
					+ TABLE_NAME
					+ "("
					+ Columns.CHILD_DEPARTMENT_UIN.getName()
					+ ");");
		}

		// Version 1 : Creation of the table
		public static void upgradeTable(SQLiteDatabase db, int oldVersion,
				int newVersion) {

			if (oldVersion < 1) {
				Log.i(LOG_TAG, "Upgrading from version " + oldVersion + " to "
						+ newVersion + ", data will be lost!");

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
				createTable(db);
				return;
			}

			if (oldVersion != newVersion) {
				throw new IllegalStateException(
						"Error upgrading the database to version " + newVersion);
			}
		}

		static String getBulkInsertString() {
			return new StringBuilder("INSERT INTO ").append(TABLE_NAME)
					.append(" ( ").append(Columns.DEPARTMENT_UIN.getName())
					.append(", ")
					.append(Columns.CHILD_DEPARTMENT_UIN.getName())
					.append(" ) VALUES (?, ?)").toString();
		}

		static void bindValuesInBulkInsert(SQLiteStatement stmt,
				ContentValues values) {
			int i = 1;
			stmt.bindLong(i++, values.getAsLong(Columns.DEPARTMENT_UIN.getName()));
			stmt.bindLong(i++, values.getAsLong(Columns.CHILD_DEPARTMENT_UIN.getName()));
		}
	}
}
