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



    public String getCOL_PASS(){
        return COL_PASS;
    }

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
        //add(new Owner(1,"Admin","Admin","Admin",new Date()));
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

    public Boolean checkEmail(String em){
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + OWNER_LOGIN_TABLE + " WHERE " +
                COL_EMAIL + "=?", new String[]{em});

        return mCursor.moveToFirst();

    }

    public void add(Dog d){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, d.getName());
        //values.put(COL_OWNER, d.getOwner());
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

    public List<Owner> getallOwners(){
        ArrayList<Owner> list = new ArrayList<Owner>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + OWNER_LOGIN_TABLE;

        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            int idIdx = cursor.getColumnIndex(COL_ID);
            int nameIdx = cursor.getColumnIndex(COL_NAME);
            int emailIdx = cursor.getColumnIndex(COL_EMAIL);
            int passIdx = cursor.getColumnIndex(COL_PASS);

            do{
                Owner owner = new Owner(
                        cursor.getInt(idIdx),
                        cursor.getString(nameIdx),
                        cursor.getString(emailIdx),
                        cursor.getString(passIdx),
                        new Date()
                );

                list.add(owner);
            } while(cursor.moveToNext());

        }

        return list;
    }


    public List<Dog> getAllDogs(){
        ArrayList<Dog> list = new ArrayList<Dog>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DOG_TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            int nameIdx = cursor.getColumnIndex(COL_NAME);
            int ownerIdx = cursor.getColumnIndex(COL_OWNER);

            do {
                Dog dog = new Dog(
                        cursor.getString(nameIdx),
                        cursor.getString(ownerIdx)
                        );
                list.add(dog);
            } while(cursor.moveToNext());

        }

        return list;
    }


    public List<Walk> getAllWalks(){
        ArrayList<Walk> list = new ArrayList<Walk>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + WALK_TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            int nameIdx = cursor.getColumnIndex(COL_ROUTE_NAME);
            int lengthIdx = cursor.getColumnIndex(COL_ROUTE_LEN);
            int ratingIdx = cursor.getColumnIndex(COL_ROUTE_RATING);

            do {
                Walk walk = new Walk(
                        cursor.getString(nameIdx),
                        cursor.getString(lengthIdx),
                        cursor.getInt(ratingIdx)
                );

                list.add(walk);
            } while(cursor.moveToNext());
        }

        return list;
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
    public String getOwnerLogintable(){
        return  OWNER_LOGIN_TABLE;
    }


    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getFriendTableName() {
        return FRIEND_TABLE_NAME;
    }

    public static String getDogTableName() {
        return DOG_TABLE_NAME;
    }

    public static String getWalkTableName() {
        return WALK_TABLE_NAME;
    }

    public static String getOwnerLoginTable() {
        return OWNER_LOGIN_TABLE;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static String getColName() {
        return COL_NAME;
    }

    public static String getColCategory() {
        return COL_CATEGORY;
    }

    public static String getColDatetime() {
        return COL_DATETIME;
    }

    public static String getColLocLong() {
        return COL_LOC_LONG;
    }

    public static String getColLocLat() {
        return COL_LOC_LAT;
    }

    public static String getColImage() {
        return COL_IMAGE;
    }

    public static String getColAvgDis() {
        return COL_AVG_DIS;
    }

    public static String getColTotWalks() {
        return COL_TOT_WALKS;
    }

    public static String getColTotDis() {
        return COL_TOT_DIS;
    }

    public static String getColTotTime() {
        return COL_TOT_TIME;
    }

    public static String getColOwner() {
        return COL_OWNER;
    }

    public static String getColEmail() {
        return COL_EMAIL;
    }

    public static String getColPass() {
        return COL_PASS;
    }

    public static String getColDob() {
        return COL_DOB;
    }

    public static String getColRouteName() {
        return COL_ROUTE_NAME;
    }

    public static String getColRouteLen() {
        return COL_ROUTE_LEN;
    }

    public static String getColRouteRating() {
        return COL_ROUTE_RATING;
    }

    public static String getCreateFriendsTable() {
        return CREATE_FRIENDS_TABLE;
    }

    public static void setCreateFriendsTable(String createFriendsTable) {
        CREATE_FRIENDS_TABLE = createFriendsTable;
    }

    public static String getCreateDogTable() {
        return CREATE_DOG_TABLE;
    }

    public static void setCreateDogTable(String createDogTable) {
        CREATE_DOG_TABLE = createDogTable;
    }

    public static String getCreateOwnerTable() {
        return CREATE_OWNER_TABLE;
    }

    public static void setCreateOwnerTable(String createOwnerTable) {
        CREATE_OWNER_TABLE = createOwnerTable;
    }

    public static String getCreateWalkTable() {
        return CREATE_WALK_TABLE;
    }

    public static void setCreateWalkTable(String createWalkTable) {
        CREATE_WALK_TABLE = createWalkTable;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldNum, int newNum){
        db.execSQL("DROP TABLE IF EXISTS " + OWNER_LOGIN_TABLE);
        onCreate(db);
    }

}
