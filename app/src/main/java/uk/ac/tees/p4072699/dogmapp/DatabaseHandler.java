package uk.ac.tees.p4072699.dogmapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by p4061644 on 07/03/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DogMapp.db";

    private static final String FRIEND_TABLE_NAME = "Friends";

    private static final String DOG_TABLE_NAME = "Dogs";

    private static final String WALK_TABLE_NAME = "Walks";

    private static final String OWNER_LOGIN_TABLE = "owner";

    private static final String COL_ID = "_id";

    private static final String COL_NAME = "name";

    private static final String COL_CATEGORY = "category";

    private static final String COL_DATETIME = "datetime";

    private static final String COL_LOC_LONG = "loc_long";

    private static final String COL_LOC_LAT = "loc_lat";

    private static final String COL_IMAGE = "image";

    private static final String COL_AVG_DIS = "avg_dis";

    private static final String COL_NO_WALKS = "tot_walks";

    private static final String COL_TOT_DIS = "tot_dis";

    private static final String COL_TOT_TIME = "tot_time";

    private static final String COL_OWNER = "owner";

    private static String CREATE_FRIENDS_TABLE = "CREATE TABLE "
            + FRIEND_TABLE_NAME
            +" (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + COL_CATEGORY + " TEXT, "
            + COL_DATETIME + " TEXT, "
            + COL_LOC_LONG + " REAL, "
            + COL_LOC_LAT + " REAl" +");";

    private static String CREATE_DOG_TABLE = "CREATE TABLE "
            + DOG_TABLE_NAME
            +" (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT, "
            + COL_OWNER + " TEXT, "
            + COL_AVG_DIS + " INTEGER, "
            + COL_NO_WALKS + " INTEGER, "
            + COL_TOT_DIS + " INTEGER);";

    private static String CREATE_OWNER_TABLE = "CREATE TABLE "
            + OWNER_LOGIN_TABLE
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + "email TEXT, "
            + "password TEXT, "
            + "DOB DATE, "
            + "tot_walks INTEGER,"
            + "tot_dis TEXT, "
            + "avg_dis TEXT, "
            + "tot_time TEXT);";
            /*+ "image IMAGE*/


    private static String CREATE_WALK_TABLE = "CREATE TABLE "
            + WALK_TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "route_name TEXT, "
            + "route_length TEXT,"
            + "route_rating INTEGER);";
    //this needs things adding to the table.




    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_OWNER_TABLE);
        db.execSQL(CREATE_DOG_TABLE);
        db.execSQL(CREATE_WALK_TABLE);
        //db.execSQL(CREATE_FRIENDS_TABLE);
        Log.d("Database", "Database Created");

    }

    public int getProfilesCount(){
        String countQuery = "SELECT  * FROM " + OWNER_LOGIN_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public Boolean checkEmail(String em){
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + OWNER_LOGIN_TABLE + " WHERE " +
        "email=?", new String[]{em});

        if(mCursor.moveToFirst()){
            return true;
        }

        return false;
    }


    public void add(Dog d){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, d.getName());
        values.put(COL_OWNER, d.getOwner());
        values.put(COL_AVG_DIS, d.getTotwalks()/d.getTotdistance());
        values.put(COL_NO_WALKS, d.getTotwalks());
        values.put(COL_TOT_DIS, d.getTotdistance());

        db.insert(DOG_TABLE_NAME,null,values);
        db.close();

        Log.d("Database", "NEW ENTRY ADDED");
    }

    public long add(Owner o){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
        values.put(COL_NAME, o.getName());
        values.put("email", o.getEmail());
        values.put("password", o.getPassword());
        values.put("DOB", df.format(o.getDob()));
        values.put("tot_walks", 2);
        values.put("tot_dis", "nil");
        values.put("avg_dis", "nil");
        values.put("tot_time", "nil");

        long input = db.insert(OWNER_LOGIN_TABLE, null, values);
        db.close();

        Log.d("DATABASE", "NEW OWNER ADDED");

        return input;
    }

    public long add(Walk w){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("route_name", w.getName());
        values.put("route_length",w.getLength());
        values.put("route_rating", w.getRating());

        long input = db.insert(WALK_TABLE_NAME, null, values);
        db.close();

        Log.d("DATABASE", "NEW WALK ADDED");

        return input;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldNum, int newNum){
        db.execSQL("DROP TABLE IF EXISTS " + OWNER_LOGIN_TABLE);
        onCreate(db);
    }

}
