package uk.ac.tees.p4072699.dogmapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    private static final String COL_TOT_WALKS = "tot_walks";

    private static final String COL_TOT_DIS = "tot_dis";

    private static final String COL_TOT_TIME = "tot_time";

    private static final String COL_OWNER = "owner";

    private static final String COL_EMAIL = "email";

    private static final String COL_PASS = "password";

    private static final String COL_DOB = "DOB";

    private static final String COL_ROUTE_NAME = "route_name";

    private static final String COL_ROUTE_LEN = "route_length";

    private static final String COL_ROUTE_RATING = "route_rating";


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
            +" (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + COL_OWNER + " TEXT, "
            + COL_IMAGE + " IMAGE, "
            + COL_AVG_DIS + " TEXT,"
            + COL_TOT_WALKS + " INTEGER, "
            + COL_TOT_DIS + "TEXT,"
            + COL_TOT_TIME + "DATETIME"
            + "FOREIGN KEY (COL_OWNER) REFERENCES" +  OWNER_LOGIN_TABLE + "(COL_ID));";

    private static String CREATE_OWNER_TABLE = "CREATE TABLE "
            + OWNER_LOGIN_TABLE
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + COL_EMAIL +" TEXT, "
            + COL_PASS + " TEXT, "
            + COL_DOB +" DATE, "
            + COL_TOT_WALKS + " INTEGER,"
            + COL_TOT_DIS + " TEXT, "
            + COL_AVG_DIS + " TEXT, "
            + COL_TOT_TIME + " TEXT);";
            /*+ "image IMAGE*/


    private static String CREATE_WALK_TABLE = "CREATE TABLE "
            + WALK_TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ROUTE_NAME + " TEXT, "
            + COL_ROUTE_LEN + " TEXT,"
            + COL_ROUTE_RATING + " INTEGER);";
    //this needs things adding to the table.


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_OWNER_TABLE);
        //db.execSQL(CREATE_DOG_TABLE);
        db.execSQL(CREATE_WALK_TABLE);
        //db.execSQL(CREATE_FRIENDS_TABLE);
        add(new Owner(1,"Admin","Admin","Admin",new Date()));
        Log.d("Database", "Database Created");

    }

    public int getProfilesCount(){
        String countQuery = "SELECT  * FROM " + OWNER_LOGIN_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    /*public Boolean checkEmail(String em){
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + OWNER_LOGIN_TABLE + " WHERE " +
                COL_EMAIL + "=?", new String[]{em});

        if(mCursor.moveToFirst()){
            return true;
        }

        return false;
    }*/

    public String getSinlgeEntry_log(String email)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(OWNER_LOGIN_TABLE,null,COL_EMAIL + "=?",new String[]{email},
                null,null,null);
        if(cursor.getCount() > 0) // UserName Not Exist
        {
            cursor.moveToFirst();
            String password= cursor.getString(cursor.getColumnIndex(COL_PASS));
            cursor.close();
            return password;
        }

        cursor.close();
        return "";
    }

    public String getSinlgeEntry_sign(String email)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(OWNER_LOGIN_TABLE,null,COL_EMAIL + "=?",new String[]{email},
                null,null,null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.moveToFirst();
            String password= cursor.getString(cursor.getColumnIndex(COL_PASS));
            cursor.close();
            return "NOT EXIST";
        }

        cursor.close();
        return "EXIST";
    }


    public void add(Dog d){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, d.getName());
        values.put(COL_OWNER, d.getOwner());
        values.put(COL_AVG_DIS, d.getTotwalks()/d.getTotdistance());
        values.put(COL_TOT_WALKS, d.getTotwalks());
        values.put(COL_TOT_DIS, d.getTotdistance());

        db.insert(DOG_TABLE_NAME,null,values);
        db.close();

        Log.d("Database", "NEW ENTRY ADDED");
    }

    /*public long add(Walk w){
        return null;
    }

    public long add(Friend f){
        return null;
    }*/

    public long add(Owner o){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        //Log.d("Database", "NEW ENTRY ADDED");

        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
        values.put(COL_NAME, o.getName());
        values.put(COL_EMAIL, o.getEmail());
        values.put(COL_PASS, o.getPassword());
        values.put(COL_DOB, df.format(o.getDob()));
        values.put(COL_TOT_WALKS, 2);
        values.put(COL_TOT_DIS, "nil");
        values.put(COL_AVG_DIS, "nil");
        values.put(COL_TOT_TIME, "nil");
        //values.put("image","nil");

        long input = db.insert(OWNER_LOGIN_TABLE, null, values);
        db.close();

        Log.d("DATABASE", "NEW OWNER ADDED");

        return input;

    }

    public long add(Walk w){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ROUTE_NAME, w.getName());
        values.put(COL_ROUTE_LEN,w.getLength());
        values.put(COL_ROUTE_RATING, w.getRating());

        long input = db.insert(WALK_TABLE_NAME, null, values);
        db.close();

        Log.d("DATABASE", "NEW WALK ADDED");

        return input;
    }

    /*public List<Object> getAll(){
        ArrayList<Object> list = new ArrayList<Object>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            int idIdx = cursor.getColumnIndex(COL_ID);
            int noteIdx = cursor.getColumnIndex(COL_NOTE);
            int cateIdx = cursor.getColumnIndex(COL_CATEGORY);

            do{
                Entry entry = new Entry(
                        cursor.getInt(idIdx),
                        cursor.getString(noteIdx),
                        cursor.getString(cateIdx)
                );

                list.add(entry);
            } while(cursor.moveToNext());
        }

        return list;
    }

    public void deleteEntry (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String newint = String.valueOf(id-1);
        db.delete(
                TABLE_NAME,
                COL_ID + " = ?",
                new String[] {String.valueOf(id) }
        );

        db.close();

        Log.d("Database", " Journal entry with id: " + id + " has been deleted");
    }



    public void removeAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    /**public void updateLecturer(int id, String name, String phone){
     SQLiteDatabase db = this.getWritableDatabase();

     String where = "_ID = " + id;

     ContentValues values = new ContentValues();
     values.put(COL_NAME, name);
     values.put(COL_PHONE, phone);

     db.update(
     TABLE_NAME,
     values,
     where,
     null
     );
     db.close();

     Log.d("Database", "Database updated");
     }

     public Lecturer getRecord(int id) {
     SQLiteDatabase db = this.getReadableDatabase();

     String where = "_ID = " + id;

     Cursor c = db.query(true, TABLE_NAME, null, where, null, null, null, null, null);

     int idIdx = c.getColumnIndex(COL_ID);
     int nameIdx = c.getColumnIndex(COL_NAME);
     int phoneIdx = c.getColumnIndex(COL_PHONE);
     Lecturer l = null;
     if (c.moveToFirst()) {
     do {
     l = new Lecturer(
     c.getInt(idIdx),
     c.getString(nameIdx),
     c.getString(phoneIdx)
     );

     } while (c.moveToNext());
     }
     return l;
     }*/


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldNum, int newNum){
        db.execSQL("DROP TABLE IF EXISTS " + OWNER_LOGIN_TABLE);
        onCreate(db);
    }

}
