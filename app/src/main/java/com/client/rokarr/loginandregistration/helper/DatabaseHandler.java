package com.client.rokarr.loginandregistration.helper;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // for our logs
    public static final String TAG = "DatabaseHandler.java";

    // database version
    private static final int DATABASE_VERSION = 4;

    // database name
    protected static final String DATABASE_NAME = "localdata";

    // table details
    private static final String tableName = "product";
    private static final String tableName1 = "billDetails";
    private static final String tableName2 = "clientNames";
    private static final String tableName3 = "balanceSheet";
    private static final String tableName4 = "billPurchaseDetails";
    private static final String tableName5 = "purchaseClients";
    private static final String tableName6 = "purBalanceSheet";
    private static final String Column1 = "billNo";
    private static final String Column3 = "total_amount";
    private static final String Column5 = "balance";
    private static final String Column4 = "bill_date";
    private static final String Column6 = "tax";
    private static final String Column7 = "paid";
    private static final String Column8 = "Qty";
    private static final String Column9 = "paymode";
    private static final String Column10 = "transactionID";
    private static final String fieldObjectId = "id";
    private static final String fieldObjectName = "product_details";
    private static final String fieldObjectName1 = "client_names";
    private static final String fieldObjectName2 = "client_address";
    Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    // constructor
    public DatabaseHandler(Context context) {
            super(context, Environment.getExternalStorageDirectory()
                    + File.separator + "Rokarr"
                    + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        String path = Environment.getExternalStorageDirectory()
                + File.separator + "Rokarr"
                + File.separator + DATABASE_NAME;
        MediaScannerConnection.scanFile(context, new String[]{path.toString()}, null, null);
    }

    // creating table
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "";

        sql += "CREATE TABLE IF NOT EXISTS " + tableName;
        sql += " ( ";
        sql += fieldObjectName + " TEXT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += fieldObjectName2 + " TEXT ";
        sql += " ) ";
        db.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableName1;
        sql += " ( ";
        sql += Column1 + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += Column3 + " DOUBLE PRECISION, ";
        sql += Column7 + " DOUBLE PRECISION, ";
        sql += Column4 + " DATE, ";
        sql += Column9 + " INTEGER, ";
        sql += Column10 + " TEXT ";
        sql += " ) ";
        db.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableName2;
        sql += " ( ";
        sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += fieldObjectName + " TEXT, ";
        sql += Column8 + " TEXT, ";
        sql += Column6 + " DOUBLE PRECISION, ";
        sql += Column3 + " DOUBLE PRECISION, ";
        sql += Column1 + " INTEGER ";
        sql += " ) ";
        db.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableName3;
        sql += " ( ";
        sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += Column5 + " DOUBLE PRECISION ";
        sql += " ) ";
        db.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableName4;
        sql += " ( ";
        sql += Column1 + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += Column3 + " DOUBLE PRECISION, ";
        sql += Column7 + " DOUBLE PRECISION, ";
        sql += Column4 + " DATE, ";
        sql += Column9 + " INTEGER, ";
        sql += Column10 + " TEXT ";
        sql += " ) ";
        db.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableName5;
        sql += " ( ";
        sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += fieldObjectName + " TEXT, ";
        sql += Column6 + " DOUBLE PRECISION, ";
        sql += Column8 + " TEXT, ";
        sql += Column3 + " DOUBLE PRECISION, ";
        sql += Column1 + " INTEGER ";
        sql += " ) ";
        db.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS " + tableName6;
        sql += " ( ";
        sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectName1 + " TEXT, ";
        sql += Column5 + " DOUBLE PRECISION ";
        sql += " ) ";
        db.execSQL(sql);
    }

    public String getID(String objectName, int id)
    {
        String value = null;
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        if(id ==1) {
            cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName3 + " WHERE " + fieldObjectName1 + " = '"
                    + objectName + "'", null);
        }
        else
        {
            cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName6 + " WHERE " + fieldObjectName1 + " = '"
                    + objectName + "'", null);
        }
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()){
                do{
                    value = cursor.getString(cursor.getColumnIndex(fieldObjectId));
                    // do what ever you want here
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        //db.close();
        return value;
    }

    public String getIDfromClientNames(String objectName, String objectName1, String billNo, int id)
    {
        String value = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        if (id == 1) {
            cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName2 + " WHERE " + fieldObjectName1 + " = '"
                    + objectName + "'" + " AND " + fieldObjectName + " = '" + objectName1 + "'"
                    + " AND " + Column1 +" = '" + billNo + "'", null);
        }
        else if (id == 2)
        {
            cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName5 + " WHERE " + fieldObjectName1 + " = '"
                    + objectName + "'" + " AND " + fieldObjectName + " = '" + objectName1 + "'"
                    + " AND " + Column1 + " = '" + billNo + "'", null);
        }
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()){
                    do{
                        value = cursor.getString(cursor.getColumnIndex(fieldObjectId));
                        // do what ever you want here
                    }while(cursor.moveToNext());
                }
            }
            cursor.close();
            //db.close();
        return value;
    }

    // When upgrading the database, it will drop the current table and recreate.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS " + tableName1;
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS " + tableName2;
        db.execSQL(sql);

        sql = "DROP TABLE IF EXISTS " + tableName3;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + tableName4;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + tableName5;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + tableName6;
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addProdDetails(String name, String product, String amount, String qty, String tax,  String billNo , int id)
    {
        boolean flag = false;
        SQLiteDatabase db = null;
        ContentValues values = new ContentValues();
        values.put(fieldObjectName1, name);
        values.put(fieldObjectName, product);
        values.put(Column3, amount);
        values.put(Column1, billNo);
        values.put(Column6, tax);
        values.put(Column8, qty);
        if(id == 1) {

            if(!checkIfExists1(product, name, billNo, 1)) {
                db = this.getWritableDatabase();
                flag = db.insert(tableName2, null, values) > 0;
            }
            else {
                db = this.getWritableDatabase();
                flag = db.update(tableName2, values, fieldObjectId + " = " + getIDfromClientNames(name, product, billNo, 1), null) > 0;
            }

        }
        else if (id == 2)
        {


            if(!checkIfExists1(product, name, billNo, 2)) {
                db = this.getWritableDatabase();
                flag = db.insert(tableName5, null, values) > 0;
            }
            else {
                db = this.getWritableDatabase();
                flag = db.update(tableName5, values, fieldObjectId + " = " + getIDfromClientNames(name, product, billNo, 2), null) > 0;
            }
        }
            db.close();
        return flag;
    }

    public boolean addBalance(String name, String balance,int id)
    {
        boolean flag = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(fieldObjectName1, name);
        values.put(Column5, balance);
        if(id == 1) {
            if (!checkIfExists(name, 5)) {
                if (!db.isOpen())
                    db = this.getWritableDatabase();
                flag = db.insert(tableName3, null, values) > 0;
                db.close();
            } else {
                if (!db.isOpen())
                    db = this.getWritableDatabase();
                flag = db.update(tableName3, values, fieldObjectId + " = " + getID(name,1), null) > 0;
                db.close();
            }
        }
        else if (id == 2)
        {
            if (!checkIfExists(name, 8)) {
                if (!db.isOpen())
                    db = this.getWritableDatabase();
                flag = db.insert(tableName6, null, values) > 0;
                db.close();
            } else {
                if (!db.isOpen())
                    db = this.getWritableDatabase();
                flag = db.update(tableName6, values, fieldObjectId + " = " + getID(name,2), null) > 0;
                db.close();
            }
        }
        return flag;
    }
    /**
     * Storing user details in database
     * */
    public boolean addUser(String billNo, String name, String amount, String date, String paid, String paymode, String transID,int id) {
        boolean flag = false;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate = null;
        try {
            java.util.Date datejava = dateFormatter.parse(date);
            startDate = new java.sql.Date(datejava.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = this.getWritableDatabase();

        if(id == 1) {
            if (!checkIfExists(billNo, 4)) {
                ContentValues values = new ContentValues();
                values.put(Column1, billNo); // billNo
                values.put(fieldObjectName1, name); // name
                values.put(Column3, amount); // amount
                values.put(Column7, paid); //paid amount
                values.put(Column4, String.valueOf(startDate));//date
                values.put(Column9,paymode);//0 or 1
                values.put(Column10,transID);//Cheque no.

                // Inserting Row
                if (!db.isOpen())
                    db = this.getWritableDatabase();
                flag = db.insert(tableName1, null, values) > 0;
                db.close();
            }
        }
        else if(id == 2) {
            if (!checkIfExists(billNo, 6)) {
                ContentValues values = new ContentValues();
                values.put(Column1, billNo); // billNo
                values.put(fieldObjectName1, name); // name
                values.put(Column3, amount); // amount
                values.put(Column7,paid); //paid amount
                values.put(Column4, String.valueOf(startDate));//date
                values.put(Column9,paymode);//0 or 1
                values.put(Column10,transID);//Cheque no.

                // Inserting Row
                if (!db.isOpen())
                    db = this.getWritableDatabase();
                flag = db.insert(tableName4, null, values) > 0;
                db.close();
            }
        }
        if(db.isOpen())
        db.close(); // Closing database connection

        if(flag)
        Log.d(TAG, "New user inserted into sqlite: " + flag);
        return flag;
    }

    public boolean create(String value, int id) {

        boolean createSuccessful = false;

        if(id == 1) {
            if (!checkIfExists(value, 1)) {

                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(fieldObjectName, value);
                createSuccessful = db.insert(tableName, null, values) > 0;

                db.close();

                if (createSuccessful) {
                    Log.e(TAG, value + " created.");
                }
            }
        }

        else if (id == 2)
        {
            if (!checkIfExists(value, 2)) {

                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(fieldObjectName1, value);
                createSuccessful = db.insert(tableName, null, values) > 0;

                db.close();

                if (createSuccessful) {
                    Log.e(TAG, value + " created.");
                }
            }
        }

        else if(id == 3)
        {
            if (!checkIfExists(value, 3)) {

                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(fieldObjectName2, value);
                createSuccessful = db.insert(tableName, null, values) > 0;

                db.close();

                if (createSuccessful) {
                    Log.e(TAG, value + " created.");
                }
            }
        }
        return createSuccessful;
    }

    public boolean checkIfExists1(String objectName, String objectName1, String billno, int id) {

        boolean recordExists = false;
        Cursor cursor = null;

        SQLiteDatabase db = this.getWritableDatabase();

        if (id == 1) {
            cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName2 + " WHERE " + fieldObjectName + " = '"
                    + objectName + "'" + " AND " + fieldObjectName1 + " = '" + objectName1 + "'" + " AND "
                    + Column1 + " = '" + billno + "'", null);
        }
        else if (id == 2)
        {
            cursor = db.rawQuery("SELECT " + fieldObjectId + " FROM " + tableName5 + " WHERE " + fieldObjectName + " = '"
                    + objectName + "'" + " AND " + fieldObjectName1 + " = '" + objectName1 + "'" + " AND "
                    + Column1 + " = '" + billno + "'", null);
        }
        if (cursor != null) {

            if (cursor.getCount() > 0) {
                recordExists = true;
            }
        }
        if(!cursor.isClosed()) {
            cursor.close();
        }
        db.close();

        return recordExists;
    }

    // check if a record exists so it won't insert the next time you run this code
    public boolean checkIfExists(String objectName, int id) {

        boolean recordExists = false;
        Cursor cursor = null;

        SQLiteDatabase db = this.getWritableDatabase();
        if(id==1) {
            cursor = db.rawQuery("SELECT " + fieldObjectName + " FROM " + tableName + " WHERE " + fieldObjectName + " = '" + objectName + "'", null);
        }
        else if(id==2)
        {
            cursor = db.rawQuery("SELECT " + fieldObjectName + " FROM " + tableName + " WHERE " + fieldObjectName1 + " = '" + objectName + "'", null);
        }
        else if(id==3)
        {
            cursor = db.rawQuery("SELECT " + fieldObjectName + " FROM " + tableName + " WHERE " + fieldObjectName2 + " = '" + objectName + "'", null);
        }
        else if(id == 4)
        {
            cursor = db.rawQuery("SELECT " + Column1 + " FROM " + tableName1 + " WHERE " + Column1 + " = '" + objectName + "'", null);
        }
        else if (id == 5)
        {
            cursor = db.rawQuery("SELECT " + Column5 + " FROM " + tableName3 + " WHERE " + fieldObjectName1 + " = '" + objectName + "'", null);
        }

        else if (id == 6)
        {
            cursor = db.rawQuery("SELECT " + Column1 + " FROM " + tableName4 + " WHERE " + Column1 + " = '" + objectName + "'", null);
        }
        else if (id == 7)
        {
            cursor = db.rawQuery("SELECT " + Column5 + " FROM " + tableName5 + " WHERE " + fieldObjectName1 + " = '" + objectName + "'", null);
        }
        else if (id == 8)
        {
            cursor = db.rawQuery("SELECT " + Column5 + " FROM " + tableName6 + " WHERE " + fieldObjectName1 + " = '" + objectName + "'", null);
        }
        if (cursor != null) {

            if (cursor.getCount() > 0) {
                recordExists = true;
            }
        }
       if(!cursor.isClosed()) {
            cursor.close();
        }
        db.close();

        return recordExists;
    }
        // Read records related to the search term
        public List<String> read1(String searchTerm, int id) {

            List<String> recordsList = new ArrayList<String>();
            String sql = "";
            searchTerm = searchTerm.replace("'", "");

            // select query
            if(id == 1) {
                sql += "SELECT * FROM " + tableName;
                sql += " WHERE " + fieldObjectName + " LIKE '" + "%" + searchTerm + "%" + "'";
                sql += " LIMIT 0,5";
            }
            else if (id == 2)
            {
                sql += "SELECT * FROM " + tableName;
                sql += " WHERE " + fieldObjectName1 + " LIKE '" + "%" + searchTerm + "%" + "'";
                sql += " LIMIT 0,5";
            }
            else if (id == 3)
            {
                sql += "SELECT * FROM " + tableName;
                sql += " WHERE " + fieldObjectName2 + " LIKE '" + "%" + searchTerm + "%" + "'";
                sql += " LIMIT 0,5";
            }
            SQLiteDatabase db = this.getWritableDatabase();

            // execute the query
            //Cursor cursor = db.query(tableName,new String[]{fieldObjectName1,},fieldObjectName1 + " LIKE ?",new String[]{ "%" +
          //  searchTerm + "%" })
            Cursor cursor = db.rawQuery(sql, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    if(id == 1) {
                        String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                        recordsList.add(objectName);
                    }
                    else if (id == 2)
                    {
                        String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName1));
                        recordsList.add(objectName);
                    }
                    else if(id == 3)
                    {
                        String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName2));
                        recordsList.add(objectName);
                    }


                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            // return the list of records
            return recordsList;
        }

    public int getBillCount(int id) {

        String countQuery = null, countQuery1 = null;
        SQLiteDatabase db = this.getReadableDatabase();
        int a = 0;
        if (id == 1) {
            countQuery1 = "SELECT  * FROM " + tableName1;
            Cursor cursor1 =db.rawQuery(countQuery1, null);
            if(cursor1.getCount() == 0)
            {
                addUser("101","","","","","0","",1);
                return 101;
            }
            else {
                countQuery = "SELECT * FROM " + tableName1 + " WHERE " + Column1 + " = ( SELECT MAX(" + Column1 + ") FROM " + tableName1 + ")";
            }
            //countQuery = "SELECT  * FROM " + tableName1;
        }
        else if(id == 2)
        {
            countQuery1 = "SELECT  * FROM " + tableName4;
            Cursor cursor1 =db.rawQuery(countQuery1, null);
            if(cursor1.getCount() == 0)
            {
                addUser("101","","","","","0","",2);
                return 101;
            }
            else {
                countQuery = "SELECT * FROM " + tableName4 + " WHERE " + Column1 + " = ( SELECT MAX(" + Column1 + ") FROM " + tableName4 + ")";
            }
            //countQuery = "SELECT  * FROM " + tableName4;
        }
        Cursor cursor = db.rawQuery(countQuery, null);
       // int a = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                String objectName = cursor.getString(cursor.getColumnIndex(Column1));
                a= Integer.parseInt(objectName);
            }while (cursor.moveToNext());
        }
        cursor.close();
        // return count
        return a;
    }


    public ArrayList getData1(String name, String product, int id, boolean flag)
    {
        ArrayList recordsList = new ArrayList<String>();
        String sql = "";
        // select query
        if(flag == true) {
            sql += "SELECT * FROM " + tableName2;
            sql += " WHERE " + fieldObjectName1 + " LIKE '%" + name + "%'";
            sql += " AND " + fieldObjectName + " LIKE '%" + product + "%'";
            sql += " ORDER BY " + fieldObjectId + " DESC";
            sql += " LIMIT 0,30";
        }
        else
        {
            sql += "SELECT * FROM " + tableName5;
            sql += " WHERE " + fieldObjectName1 + " LIKE '%" + name + "%'";
            sql += " AND " + fieldObjectName + " LIKE '%" + product + "%'";
            sql += " ORDER BY " + fieldObjectId + " DESC";
            sql += " LIMIT 0,30";
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                if(id == 1) {
                    String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                    recordsList.add(objectName);
                }
                else if (id == 2)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column3));
                    recordsList.add(objectName);
                }
                else if(id == 3)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column1));
                    recordsList.add(objectName);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return the list of records
        return recordsList;

    }

    public ArrayList getDatawithinDates(String name, String from, String to, int id)
    {
        ArrayList recordsList = new ArrayList<String>();
        String sql = "";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate = null,endDate = null;
        try {
            java.util.Date datejava = dateFormatter.parse(from);
            startDate = new java.sql.Date(datejava.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            java.util.Date datejava = dateFormatter.parse(to);
            endDate = new java.sql.Date(datejava.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sql = "SELECT * FROM " + tableName2 + " WHERE " + Column1 + " IN " + "(SELECT " +
                Column1 + " FROM " + tableName1 + " WHERE " + Column4 + " BETWEEN '" + startDate + "' AND '"
                + endDate + "' AND " + fieldObjectName1 + " = '" + name + "')";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                if(id == 1) {
                    String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                    recordsList.add(objectName);
                }
                else if (id == 2)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column3));
                    recordsList.add(objectName);
                }
                else if(id == 3)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column1));
                    recordsList.add(objectName);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }

    public ArrayList getDataFromDate(String date, int id, boolean flag)
    {
        ArrayList recordsList = new ArrayList<String>();
        String sql = "";
        String sql1 = "";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate = null;
        try {
            java.util.Date datejava = dateFormatter.parse(date);
            startDate = new java.sql.Date(datejava.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(flag == true) {
            sql1 = "SELECT * FROM " + tableName1 + " WHERE " + Column4 + " = '" + startDate + "'";
        }
        else
        {
            sql1 = "SELECT * FROM " + tableName4 + " WHERE " + Column4 + " = '" + startDate + "'";
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor2 = db.rawQuery(sql1,null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {
            do {
                if(id == 1) {
                    String objectName = cursor2.getString(cursor2.getColumnIndex(fieldObjectName1));
                    recordsList.add(objectName);
                }
                else if (id == 2)
                {
                    String objectName = cursor2.getString(cursor2.getColumnIndex(Column7));
                    recordsList.add(objectName);
                }
            } while (cursor2.moveToNext());
        }
        cursor2.close();
        db.close();
        // return the list of records
        return recordsList;
    }

    public ArrayList getDatawithinDates1(String from, String to, int id)
    {
        ArrayList recordsList = new ArrayList<String>();
        String sql = "";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate = null,endDate = null;
        try {
            java.util.Date datejava = dateFormatter.parse(from);
            startDate = new java.sql.Date(datejava.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            java.util.Date datejava = dateFormatter.parse(to);
            endDate = new java.sql.Date(datejava.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sql = "SELECT * FROM " + tableName2 + " WHERE " + Column1 + " IN " + "(SELECT " +
                Column1 + " FROM " + tableName1 + " WHERE " + Column4 + " BETWEEN '" + startDate + "' AND '"
                + endDate + "') LIMIT 0,500";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                if(id == 1) {
                    String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                    recordsList.add(objectName);
                }
                else if (id == 2)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column3));
                    recordsList.add(objectName);
                }
                else if(id == 3)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName1));
                    recordsList.add(objectName);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }

    public ArrayList getData(String searchTerm, int id, boolean flag) {

        ArrayList recordsList = new ArrayList<String>();
        String sql = "";

        // select query
        if(flag == true) {
            sql += "SELECT * FROM " + tableName1;
            sql += " WHERE (" + fieldObjectName1 + " = '" + searchTerm + "' AND " + Column3 + " != '0')";
            sql += " ORDER BY " + Column1 + " DESC";
            sql += " LIMIT 0,1000";
        }
        else {
            sql += "SELECT * FROM " + tableName4;
            sql += " WHERE (" + fieldObjectName1 + " = '" + searchTerm + "' AND " + Column3 + " != '0')";
            sql += " ORDER BY " + Column1 + " DESC";
            sql += " LIMIT 0,1000";
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                if(id == 1) {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column1));
                    recordsList.add(objectName);
                }
                else if (id == 2)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column3));
                    recordsList.add(objectName);
                }
                else if(id == 3)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column4));
                    recordsList.add(objectName);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }

    public ArrayList getProDetailsBill(String billno, int id, boolean flag)
    {
        Cursor cursor = null;
        String sql = "";
        ArrayList recordsList = new ArrayList<String>();
        if (flag == true) {
            sql = "SELECT * FROM " + tableName2 + " WHERE " + Column1 + " IN " + "(SELECT " +
                    Column1 + " FROM " + tableName1 + " WHERE " + Column1 + " = '" + billno + "')";
        }
        else
        {
            sql = "SELECT * FROM " + tableName5 + " WHERE " + Column1 + " IN " + "(SELECT " +
                    Column1 + " FROM " + tableName5 + " WHERE " + Column1 + " = '" + billno + "')";
        }
        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        cursor = db.rawQuery(sql, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                if(id == 1) {
                    String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                    recordsList.add(objectName);
                }
                else if (id == 2)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column8));
                    recordsList.add(objectName);
                }
                else if(id == 3)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column6));
                    recordsList.add(objectName);
                }
                else if(id == 4)
                {
                    String objectName = cursor.getString(cursor.getColumnIndex(Column3));
                    recordsList.add(objectName);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ArrayList getPaymentID(String name, boolean flag, int id)
    {
        Cursor cursor = null;
        ArrayList recordsList = new ArrayList<String>();
        String sql = "";
        if(flag == true)
        {
            sql = "SELECT * FROM " + tableName1 + " WHERE " + fieldObjectName1 + " = '" + name + "' AND "
            + Column9 + " = '1' ";
        }
        else
        {
            sql = "SELECT * FROM " + tableName4 + " WHERE " + fieldObjectName1 + " = '" + name + "' AND "
                    + Column9 + " = '1' ";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                if(id == 1)
                {
                    String value = cursor.getString(cursor.getColumnIndex(Column1));
                    recordsList.add(value);
                }
                else if (id == 2)
                {
                    String value = cursor.getString(cursor.getColumnIndex(Column10));
                    recordsList.add(value);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ArrayList getbalanceNames(boolean flag, int id) {

        Cursor cursor = null;
        ArrayList recordsList = new ArrayList<String>();
        String sql = "";
        if (flag == true) {
            sql = "SELECT * FROM " + tableName3 + " WHERE " + Column5 + " != '0' OR " + Column5 +
                    " != '0.0' ";
        } else {
            sql = "SELECT * FROM " + tableName6 + " WHERE " + Column5 + " != '0' OR " + Column5 +
                    " != '0.0' ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                if(id == 1)
                {
                    String value = cursor.getString(cursor.getColumnIndex(fieldObjectName1));
                    recordsList.add(value);
                }
                else if (id == 2)
                {
                    String value = cursor.getString(cursor.getColumnIndex(Column5));
                    recordsList.add(value);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return the list of records
        return recordsList;
    }

    public String getPaid(String bill, boolean flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String value = null;
        Cursor cursor;
        if(flag == true)
        {
            cursor = db.rawQuery("SELECT * FROM " + tableName1 + " WHERE " + Column1 + " = '" + bill + "'", null);
        }
        else
        {
            cursor = db.rawQuery("SELECT * FROM " + tableName4 + " WHERE " + Column1 + " = '" + bill + "'", null);
        }
        if(cursor.moveToFirst())
        {
            do {
                value = cursor.getString(cursor.getColumnIndex(Column7));

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return value;
    }

    public String getBalance(String name, int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String value = null;
        Cursor cursor = null;
        if (id == 1) {
            cursor = db.rawQuery("SELECT * FROM " + tableName3 + " WHERE " + fieldObjectName1 + " = '" + name + "'", null);
        }
        else if(id == 2)
        {
            cursor = db.rawQuery("SELECT * FROM " + tableName6 + " WHERE " + fieldObjectName1 + " = '" + name + "'", null);
        }
        if(cursor.moveToFirst())
        {
            do {
                value = cursor.getString(cursor.getColumnIndex(Column5));

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return value;
    }

    public boolean deleteProdBill(String billno, String product, boolean flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        if (flag == true)
        {
            result =  db.delete(tableName2, Column1 + " = ? AND " + fieldObjectName + " = ?",
                    new String[]{billno,product})>0;
        }
        else
        {
            result = db.delete(tableName5, Column1 + " = ? AND " + fieldObjectName + " = ?",
                    new String[]{billno,product})>0;
        }
        db.close();
        return result;
    }

    public boolean updateProdDetails(String billno, String product, String qty, String tax, String amt, boolean flag)
    {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column8,qty);
        values.put(Column6,tax);
        values.put(Column3, amt);
        if(flag == true)
        {
            result = db.update(tableName2, values,Column1 + " = ? AND " + fieldObjectName + " = ?",
                    new String[]{billno,product})>0;
        }
        else
        {
            result = db.update(tableName5, values,Column1 + " = ? AND " + fieldObjectName + " = ?",
                    new String[]{billno,product})>0;
        }
        db.close();
        return result;
    }

    public boolean updateProdBill(String billno,String amount, boolean flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column3, amount);
        boolean result = false;
        if(flag == true)
        {
            result = db.update(tableName1, values, Column1 + " = " + billno, null) > 0;
        }
        else
        {
            result = db.update(tableName4, values, Column1 + " = " + billno, null) > 0;
        }
        db.close();
        return result;
    }
    public boolean deleteBill(String billno, boolean flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        if (flag == true) {
            result = db.delete(tableName2, Column1 + "=" + billno, null) > 0;
        }
        else
            result = db.delete(tableName5, Column1 + "=" + billno,null) > 0;
        db.close();
        return result;
    }

    public boolean deleteBill1(String billno, boolean flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = false;
        if (flag == true) {
            result = db.delete(tableName1, Column1 + "=" + billno, null) > 0;
        }
        else
            result = db.delete(tableName4, Column1 + "=" + billno,null) > 0;
        db.close();
        return result;
    }


    }