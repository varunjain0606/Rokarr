package com.client.rokarr.loginandregistration.helper;

/**
 * Created by Varun on 26-11-2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "ownerdata";

    // Login table name
    private static final String TABLE_USER = "localclientdata";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_TIN = "Tin";
    private static final String KEY_CST = "Cst";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "mail";
    private static final String KEY_COMPANY_NAME = "companyName";
    private static final String KEY_ADDRESS1 = "address1";
    private static final String KEY_ADDRESS2 = "address2";
    private static final String KEY_CREATION_DATE = "creation_date";
    private static final String KEY_EXPIRY_DATE = "expiry_date";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_COMPANY_NAME + " TEXT," + KEY_CONTACT +
                " TEXT," + KEY_TIN + " TEXT," + KEY_CST + " TEXT," + KEY_ADDRESS1 +
                " TEXT," + KEY_ADDRESS2 + " TEXT," + KEY_CREATION_DATE + " TEXT," + KEY_EXPIRY_DATE + " TEXT" +")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String contact, String address1, String address2,
                        String companyName, String tin, String cst, String creation_date, String expirydate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_COMPANY_NAME,companyName);
        values.put(KEY_CONTACT, contact);
        values.put(KEY_TIN,tin);
        values.put(KEY_CST,cst);
        values.put(KEY_ADDRESS1,address1);
        values.put(KEY_ADDRESS2,address2);
        values.put(KEY_CREATION_DATE,creation_date);
        values.put(KEY_EXPIRY_DATE,expirydate);
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("companyName", cursor.getString(3));
            user.put("contact",cursor.getString(4));
            user.put("tin",cursor.getString(5));
            user.put("cst",cursor.getString(6));
            user.put("address1",cursor.getString(7));
            user.put("address2",cursor.getString(8));
            user.put("creation_date",cursor.getString(9));
            user.put("expiry_date",cursor.getString(10));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
