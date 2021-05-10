package com.example.finalproject.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.finalproject.modal.Data;
import com.example.finalproject.modal.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    /** Database Version*/
    private static final int DATABASE_VERSION = 2;
    /** Database Name*/
    private static final String DATABASE_NAME = "UserManager3.db";

    /** user database information*/
    /** User table name*/
    private static final String TABLE_USER = "user";
    /** User Table Columns names*/
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    /** data table information*/
    /**Data table name */
    private static final String TABLE_DATA = "data";
    /**Data Table Columns names*/
    private static final String COLUMN_DATA_ID ="data_id";
    private static final String COLUMN_EMAIL ="email";
    private static final String COLUMN_DATA ="data";
    private static final String COLUMN_DATE ="date";

    /** create Data table sql query*/
    private String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA +"("
            + COLUMN_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_DATA + " INTEGER,"
            + COLUMN_DATE + " TEXT" + ")";

    /**drop data table sql query*/
    private String DROP_DATA_TABLE = "DROP TABLE IF EXISTS " + TABLE_DATA;

    /** create user table sql query*/
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    /** drop USER table sql query*/
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private List<String> listData;

    /** Constructor */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_DATA_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User and Data Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_DATA_TABLE);
        // Create tables again
        onCreate(db);
    }
    /** This method is to create user record */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        /** Inserting Row*/
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    /** This method adds a users glucose level to the database */
    public void addData (Data data){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, data.getEmail());
        values.put(COLUMN_DATA, data.getData());
        values.put(COLUMN_DATE, data.getDate());
        db.insert(TABLE_DATA, null, values);
        db.close();
    }
    /** This method returns the users glucose level to the textView */
    public String viewData(Data dataGetter){
        String x ="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery( "select (AVG(data)) AS results " +
                "FROM "+ TABLE_DATA

                + " WHERE " + TABLE_DATA + "." + COLUMN_EMAIL + " = " + "?"
                + " AND " + TABLE_DATA + "." + COLUMN_DATE + " = " + "?", new String[] {dataGetter.getEmail(), dataGetter.getDate()} );

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {

            if (cursor.getString(cursor.getColumnIndex("results"))==null){
                x = "No Data Available";
            }else {
            x = cursor.getString(cursor.getColumnIndex("results"));}

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return x;
    }
    /** This method is to retrieve all users and return the list of user records */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /** Here query function is used to retrieve records from the user table */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                /** Adding user record to list **/
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        /** return user list */
        return userList;
    }
    /** This method is to delete user record */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        /** delete user record by id */
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    /** This method to check user exist or not  */
    public boolean checkUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to retrieve records from user table this function works like we use sql query.*/
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    /** This method is used to check if the user and password combination exist */
    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to retrieve records from the user table */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<HashMap<String, String>> GetUsers(Data dataGetter){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery( "select data AS results " +
                "FROM "+ TABLE_DATA +
                " WHERE " + TABLE_DATA + "." + COLUMN_EMAIL + " = " + "?" +
                " AND " + TABLE_DATA + "." + COLUMN_DATE + " = " + "?", new String[] {dataGetter.getEmail(), dataGetter.getDate()} );
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("results",cursor.getString(cursor.getColumnIndex("results")));
            userList.add(user);
        }
        db.close();
        cursor.close();
        return  userList;
    }
}