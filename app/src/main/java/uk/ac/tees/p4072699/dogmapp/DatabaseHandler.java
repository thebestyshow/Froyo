package uk.ac.tees.p4072699.dogmapp;


import android.app.LoaderManager;
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

    private static final String COL_ROUTE_LEN = "route_length";

    private static final String COL_ROUTE_RATING = "route_rating";

    private static final String COL_ROUTE_TIME = "route_time";

    private static final String COL_ROUTE_COMMENT = "route_comment";

    private static final String COL_ROUTE_NAME = "route_name";


    public String getCOL_PASS() {
        return COL_PASS;
    }

    private static String CREATE_FRIENDS_TABLE = "CREATE TABLE "
            + FRIEND_TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + COL_CATEGORY + " TEXT, "
            + COL_DATETIME + " TEXT, "
            + COL_LOC_LONG + " REAL, "
            + COL_LOC_LAT + " REAl" + ");";

    private static String CREATE_DOG_TABLE = "CREATE TABLE "
            + DOG_TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + COL_OWNER + " TEXT, "
            + COL_IMAGE + " IMAGE, "
            + COL_AVG_DIS + " TEXT,"
            + COL_TOT_WALKS + " INTEGER, "
            + COL_TOT_DIS + " TEXT,"
            + COL_TOT_TIME + " DATETIME);";

    private static String CREATE_OWNER_TABLE = "CREATE TABLE "
            + OWNER_LOGIN_TABLE
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT, "
            + COL_EMAIL + " TEXT, "
            + COL_PASS + " TEXT, "
            + COL_DOB + " DATE, "
            + COL_TOT_WALKS + " INTEGER,"
            + COL_TOT_DIS + " TEXT, "
            + COL_AVG_DIS + " TEXT, "
            + COL_TOT_TIME + " TEXT, "
            + COL_IMAGE + " BLOB);";


    private static String CREATE_WALK_TABLE = "CREATE TABLE "
            + WALK_TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ROUTE_NAME + " TEXT,"
            + COL_ROUTE_LEN + " TEXT,"
            + COL_ROUTE_TIME + " TEXT, "
            + COL_ROUTE_COMMENT + " TEXT, "
            + COL_ROUTE_RATING + " INTEGER,"
            + COL_IMAGE + " BLOB);";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_OWNER_TABLE);
        db.execSQL(CREATE_DOG_TABLE);
        db.execSQL(CREATE_WALK_TABLE);
        Log.d("Database", "Database Created");
    }

    public int getDogCount() {
        String countQuery = "SELECT  * FROM " + DOG_TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + OWNER_LOGIN_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void add(Dog d) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, d.getName());
        values.put(COL_OWNER, d.getOwnerID());
        values.put(COL_AVG_DIS, "nil");
        values.put(COL_TOT_WALKS, d.getTotwalks());
        values.put(COL_TOT_DIS, d.getTotdistance());

        db.insert(DOG_TABLE_NAME, null, values);
        db.close();

        Log.d("Database", "NEW ENTRY ADDED");
    }

    public List<Dog> getAllDogs(int owner) {
        List<Dog> list = new ArrayList<Dog>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DOG_TABLE_NAME + " WHERE " + COL_OWNER + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(owner)});

        if (cursor.moveToFirst()) {
            int totwalkIdx = cursor.getColumnIndex(COL_TOT_WALKS);
            int totDisIdx = cursor.getColumnIndex(COL_TOT_DIS);
            int ownerIdx = cursor.getColumnIndex(COL_OWNER);
            int nameIdx = cursor.getColumnIndex(COL_NAME);
            int idIdx = cursor.getColumnIndex(COL_ID);
            do {
                Dog dg = new Dog(
                        cursor.getInt(idIdx),
                        cursor.getString(nameIdx),
                        cursor.getInt(ownerIdx),
                        cursor.getInt(totwalkIdx),
                        cursor.getDouble(totDisIdx)
                );
                list.add(dg);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void edit(Dog d, String s) {
        SQLiteDatabase sq = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, s);

        sq.update(DOG_TABLE_NAME, cv, COL_ID + " = " + d.getId(), null);

        Log.d("UP", "dated");
    }

    public void removeDog(int i) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DOG_TABLE_NAME, COL_ID + "=" + i, null);
        Log.d("DATABASE", "DOG DELETED");
    }

    public void addDogWalk(ArrayList<Dog> list,double dis) {


        ContentValues values = new ContentValues();

        for (Dog d : list) {

            SQLiteDatabase db = getWritableDatabase();

            d.setTotwalks(d.getTotwalks()+1);

            d.setTotdistance(d.getTotdistance() + dis);


            values.put(COL_TOT_WALKS, d.getTotwalks());
            values.put(COL_TOT_DIS, String.valueOf(d.getTotdistance()));

            db.update(DOG_TABLE_NAME,
                    values,
                    COL_ID + " = " + d.getId(),
                    null);

            db.close();
            Log.d("Database", d.getId() +" DOG: " + d.getName() + " " + d.getTotwalks() + " " + d.getTotdistance());
        }
    }

    public void removeWalk(int i) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(WALK_TABLE_NAME, COL_ID + "=" + i, null);
        Log.d("DATABASE", "REVIEW DELETED");
    }

    public long add(Owner o) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
        values.put(COL_NAME, o.getName());
        values.put(COL_EMAIL, o.getEmail());
        values.put(COL_PASS, o.getPassword());
        values.put(COL_DOB, df.format(o.getDob()));
        values.put(COL_TOT_WALKS, 1);
        values.put(COL_TOT_DIS, 102.0);
        values.put(COL_AVG_DIS, "nil");
        values.put(COL_TOT_TIME, "nil");

        long input = db.insert(OWNER_LOGIN_TABLE, null, values);
        db.close();

        Log.d("DATABASE", "NEW OWNER ADDED");

        return input;

    }

    public long addW(Walk w) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ROUTE_LEN, w.getLength());
        values.put(COL_ROUTE_TIME, w.getTime());

        long input = db.insert(WALK_TABLE_NAME, null, values);
        db.close();

        Log.d("DATABASE", "NEW WALK ADDED");

        return input;
    }

    public long add(Walk w) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ROUTE_NAME,w.getName());
        values.put(COL_ROUTE_LEN, w.getLength());
        values.put(COL_ROUTE_TIME, w.getTime());
        values.put(COL_ROUTE_RATING, w.getRating());
        values.put(COL_ROUTE_COMMENT, w.getComment());
        values.put(COL_IMAGE,w.getImage());

        long input = db.insert(WALK_TABLE_NAME, null, values);
        db.close();

        Log.d("DATABASE", "NEW WALK ADDED");

        return input;
    }

    public void editWalk(Walk w){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_ROUTE_NAME,w.getName());
        values.put(COL_ROUTE_COMMENT,w.getComment());
        values.put(COL_ROUTE_RATING,w.getRating());

        db.update(WALK_TABLE_NAME,
                values,
                COL_ID + " = " + w.getId(),
                null);

        db.close();

        Log.d("Database","Walk Updated" + w.getId() + " : " + w.getName() + " : " + w.getComment() + " : " + w.getRating());

    }

    public Owner getOwnerHelper(Owner o){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + OWNER_LOGIN_TABLE + " WHERE " + COL_EMAIL + "=?",new String[]{o.getEmail()});

        return getOneOwner(c);
    }

    public Dog getDogHelper(Dog d){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DOG_TABLE_NAME + " WHERE " + COL_ID + "=?",new String[]{d.getName()});

        return getOneDog(c);
    }


    public Dog getOneDog(Cursor c) {

        if (c.moveToFirst()) {
            int idIdx = c.getColumnIndex(COL_ID);
            int nameIdx = c.getColumnIndex(COL_NAME);
            int OwnerIdx = c.getColumnIndex(COL_OWNER);
            int totWalks = c.getColumnIndex(COL_TOT_WALKS);
            int totDis = c.getColumnIndex(COL_TOT_DIS);
            do {
                Dog dog = new Dog(
                        c.getInt(idIdx),
                        c.getString(nameIdx),
                        c.getInt(OwnerIdx),
                        c.getInt(totWalks),
                        c.getDouble(totDis)
                );
                return dog;
            } while (c.moveToNext());
        }
        return null;
    }

    public void editUser(Owner owner){
        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(COL_NAME,owner.getName());
        values.put(COL_EMAIL,owner.getEmail());
        values.put(COL_PASS,owner.getPassword());

        db.update(OWNER_LOGIN_TABLE,
                values,
                COL_ID + " = " + owner.getId(),
                null);
        db.close();

        Log.d("Database", "User " + owner.getName() + " has been updated");
    }


    public Owner getOneOwner(Cursor c) {

        if (c.moveToFirst()) {
            int idIdx = c.getColumnIndex(COL_ID);
            int nameIdx = c.getColumnIndex(COL_NAME);
            int emailIdx = c.getColumnIndex(COL_EMAIL);
            int passIdx = c.getColumnIndex(COL_PASS);
            int totWalks = c.getColumnIndex(COL_TOT_WALKS);
            int totDis = c.getColumnIndex(COL_TOT_DIS);
            do {
                Owner owner = new Owner(
                        c.getInt(idIdx),
                        c.getString(nameIdx),
                        c.getString(emailIdx),
                        c.getString(passIdx),
                        new Date(),
                        c.getInt(totWalks),
                        c.getDouble(totDis)
                );
                return owner;
            } while (c.moveToNext());
        }
        return null;
    }

    public List<Owner> getallOwners() {
        ArrayList<Owner> list = new ArrayList<Owner>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + OWNER_LOGIN_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COL_ID);
            int nameIdx = cursor.getColumnIndex(COL_NAME);
            int emailIdx = cursor.getColumnIndex(COL_EMAIL);
            int passIdx = cursor.getColumnIndex(COL_PASS);
            do {
                Owner owner = new Owner(
                        cursor.getInt(idIdx),
                        cursor.getString(nameIdx),
                        cursor.getString(emailIdx),
                        cursor.getString(passIdx),
                        new Date()
                );
                list.add(owner);
            } while (cursor.moveToNext());

        }
        return list;
    }

    public List<Walk> getAllWalks() {
        ArrayList<Walk> list = new ArrayList<Walk>();

        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * FROM " + WALK_TABLE_NAME + " WHERE " + COL_ROUTE_COMMENT + " IS NOT NULL";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COL_ID);
            int nameIdx = cursor.getColumnIndex(COL_ROUTE_NAME);
            int lengthIdx = cursor.getColumnIndex(COL_ROUTE_LEN);
            int ratingIdx = cursor.getColumnIndex(COL_ROUTE_RATING);
            int comIdx = cursor.getColumnIndex(COL_ROUTE_COMMENT);
            int timeIdx = cursor.getColumnIndex(COL_ROUTE_TIME);
            int imgIdx = cursor.getColumnIndex(COL_IMAGE);
            do {
                Walk walk = new Walk(
                        cursor.getString(nameIdx),
                        cursor.getDouble(lengthIdx),
                        cursor.getInt(ratingIdx),
                        cursor.getString(comIdx),
                        cursor.getInt(idIdx),
                        cursor.getInt(timeIdx),
                        cursor.getBlob(imgIdx)
                );
                list.add(walk);
            } while (cursor.moveToNext());
        }

        return list;
    }


    public void addOwnerWalk(Owner o,double dis){
        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();

        double totdis = o.getTot_dis() + dis;
        o.setTot_dis(totdis);
        int walks = o.getTot_walks()+1;
        o.setTot_walks(walks);

        values.put(COL_TOT_WALKS,walks);
        values.put(COL_TOT_DIS,String.valueOf(totdis));

        db.update(OWNER_LOGIN_TABLE,
                values,
                COL_ID + " = " + o.getId(),
                null);

        db.close();

        Log.d("Database", o.getId() + " Walk Added " + o.getName() + " : " + o.getTot_walks() + " : " + o.getTot_dis());
    }





    public String getOwnerLogintable() {
        return OWNER_LOGIN_TABLE;
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
    public void onUpgrade(SQLiteDatabase db, int oldNum, int newNum) {
        db.execSQL("DROP TABLE IF EXISTS " + OWNER_LOGIN_TABLE);
        onCreate(db);
    }

}