package uk.ac.tees.p4072699.dogmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String COL_POINTS = "points";

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

    private static final String COL_ROUTE_DATE = "route_date";

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
            + COL_POINTS + " BLOB);";


    private static String CREATE_WALK_TABLE = "CREATE TABLE "
            + WALK_TABLE_NAME
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ROUTE_NAME + " TEXT,"
            + COL_ROUTE_LEN + " TEXT,"
            + COL_ROUTE_TIME + " TEXT, "
            + COL_ROUTE_COMMENT + " TEXT, "
            + COL_ROUTE_RATING + " INTEGER, "
            + COL_POINTS + " TEXT, "
            + COL_ROUTE_DATE + " TEXT);";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /* Constructs database with 3 tables using SQL strings above*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_OWNER_TABLE);
        db.execSQL(CREATE_DOG_TABLE);
        db.execSQL(CREATE_WALK_TABLE);
        Log.d("Database", "Database Created");
    }
    /*Counts the number of dogs in the database and returns the number*/
    public int getDogCount() {
        String countQuery = "SELECT  * FROM " + DOG_TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    /*Counts the number of profiles in the database and returns the number*/
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + OWNER_LOGIN_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    /*Inserts a new dog into the Dog table in the database*/
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
    /*Creates a list of dog objects that are created from database records. The list is then returned*/
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

    /*Changes the name of a dog in the database where the ID matches with dog passed into method*/
    public void edit(Dog d, String s) {
        SQLiteDatabase sq = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, s);

        sq.update(DOG_TABLE_NAME, cv, COL_ID + " = " + d.getId(), null);

        Log.d("UP", "dated");
    }

    /*Removes a dog from the database where the ID matches with the number passed into method*/
    public void removeDog(int i) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DOG_TABLE_NAME, COL_ID + "=" + i, null);
        Log.d("DATABASE", "DOG DELETED");
    }

    /*Increments the total walks and adds the distance of walk to all dogs contained within the List passed.
    * The information is updated in the database where the dogs ID matches in the database.*/
    public void addDogWalk(ArrayList<Dog> list, double dis) {
        ContentValues values = new ContentValues();
        for (Dog d : list) {
            SQLiteDatabase db = getWritableDatabase();
            d.setTotwalks(d.getTotwalks() + 1);
            d.setTotdistance(d.getTotdistance() + dis);
            values.put(COL_TOT_WALKS, d.getTotwalks());
            values.put(COL_TOT_DIS, String.valueOf(d.getTotdistance()));

            db.update(DOG_TABLE_NAME,
                    values,
                    COL_ID + " = " + d.getId(),
                    null);

            db.close();
            Log.d("Database", d.getId() + " DOG: " + d.getName() + " " + d.getTotwalks() + " " + d.getTotdistance());
        }
    }

    /*removes a walk from the database where the ID matches the number passsed to the method*/
    public void removeWalk(int i) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(WALK_TABLE_NAME, COL_ID + "=" + i, null);
        Log.d("DATABASE", "REVIEW DELETED");
    }

    /*Adds a owner/user to the User table within the database*/
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

    /*Adds a walk to the Walk table with only length,time and date.*/
    public long addW(Walk w) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ROUTE_LEN, w.getLength());
        values.put(COL_ROUTE_TIME, w.getTime());
        values.put(COL_ROUTE_DATE, w.getDate());

        long input = db.insert(WALK_TABLE_NAME, null, values);
        db.close();

        Log.d("DATABASE", "NEW WALK ADDED");

        return input;
    }

    /*Adds a walk to the walk table. This method adds all information to the database from the walk object.
    * The points Array list is converted to a JSON string so it can be inserted into the database*/
    public long add(Walk w) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        JSONObject coll = new JSONObject();

        try {
            coll.put("type", "coll");
            JSONArray collList = new JSONArray();
            for (LatLng obj : w.getPoints()) {
                JSONObject point = new JSONObject();
                JSONArray coord = new JSONArray("[" + obj.latitude + "," + obj.longitude + "]");
                point.put("Coords", coord);
                JSONObject location = new JSONObject();
                location.put("geometry", point);
                collList.put(location);
                coll.put("locations", collList);
            }
        } catch (JSONException e) {
            Log.d("JSON", "cant save JSON object: " + e.toString());
        }
        Log.d("JSON", "Location Collection" + coll.toString());

        values.put(COL_ROUTE_DATE, w.getDate());
        values.put(COL_ROUTE_NAME, w.getName());
        values.put(COL_ROUTE_LEN, w.getLength());
        values.put(COL_ROUTE_TIME, w.getTime());
        values.put(COL_ROUTE_RATING, w.getRating());
        values.put(COL_ROUTE_COMMENT, w.getComment());
        values.put(COL_POINTS, coll.toString());

        long input = db.insert(WALK_TABLE_NAME, null, values);
        db.close();

        Log.d("DATABASE", "NEW WALK ADDED");
        return input;
    }

    /*Inserts new values into the a record within the walk table where the ID of the walk matches a record ID*/
    public void editWalk(Walk w) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_ROUTE_NAME, w.getName());
        values.put(COL_ROUTE_COMMENT, w.getComment());
        values.put(COL_ROUTE_RATING, w.getRating());

        db.update(WALK_TABLE_NAME,
                values,
                COL_ID + " = " + w.getId(),
                null);
        db.close();

        Log.d("Database", "Walk Updated" + w.getId() + " : " + w.getName() + " : " + w.getComment() + " : " + w.getRating());
    }

    /*Constructs a cursor when passed a Owner object. This then calls getOneOwner which retrieves the first record that matches the cursors query and returns it*/
    public Owner getOwnerHelper(Owner o) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + OWNER_LOGIN_TABLE + " WHERE " + COL_EMAIL + "=?", new String[]{o.getEmail()});

        return getOneOwner(c);
    }

    /*Inserts new values into a record within the user table where the ID of the owner matches a record ID */
    public void editUser(Owner owner) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME, owner.getName());
        values.put(COL_EMAIL, owner.getEmail());
        values.put(COL_PASS, owner.getPassword());

        db.update(OWNER_LOGIN_TABLE,
                values,
                COL_ID + " = " + owner.getId(),
                null);
        db.close();

        Log.d("Database", "User " + owner.getName() + " has been updated");
    }


    /*Finds the first record that matches the cursor query and constructs a owner from the record the constructed owner is then returned*/
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

    /*Creates a list of Owner objects from records with in the Owner table. The list is then returned*/
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

    /*Creates a list of Walk objects from records within the Walk table. The list is then returned
    * JSON string is converted to construct the ArrayList of LatLng objects within the Walk object*/
    public List<Walk> getAllWalks() throws JSONException {
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
            int latlngIdx = cursor.getColumnIndex(COL_POINTS);
            int dateIDx = cursor.getColumnIndex(COL_ROUTE_DATE);


            do {
                ArrayList<LatLng> points = new ArrayList<LatLng>();
                JSONObject o = new JSONObject(cursor.getString(latlngIdx));
                //Log.d("JSON CHECK",o.toString());
                JSONArray loc;
                if (!o.has("locations")) {
                    break;
                } else {
                    loc = (JSONArray) o.get("locations");
                }

                Log.d("JSON", "NEW JSON....");


                for (int i = 0; i < loc.length(); i++) {
                    JSONObject geo = (JSONObject) loc.get(i);
                    //Log.d("JSON:geo",geo.toString());

                    JSONObject coords = (JSONObject) geo.get("geometry");
                    //Log.d("JSON:coords",coords.toString());

                    JSONArray coord = (JSONArray) coords.get("Coords");
                    //Log.d("JSON:coord", coord.toString());

                    double lat = Double.parseDouble(coord.get(0).toString());
                    double lng = Double.parseDouble(coord.get(1).toString());
                    points.add(new LatLng(lat, lng));
                }


                Log.d("LatLng Array: ", points.toString());
                Log.d("", "");
                Walk walk = new Walk(
                        cursor.getString(nameIdx),
                        cursor.getDouble(lengthIdx),
                        cursor.getInt(ratingIdx),
                        cursor.getString(comIdx),
                        cursor.getInt(idIdx),
                        cursor.getInt(timeIdx),
                        points,
                        cursor.getString(dateIDx)
                );
                list.add(walk);

                //points = new ArrayList<LatLng>();

                Log.d("Debug", list.toString());
            } while (cursor.moveToNext());
        }
        return list;
    }

    /*Increments the total walks and adds the distance of walk to the owner that is passed to this method
    * The information is updated in the database where the Owner ID matches in the database.*/
    public void addOwnerWalk(Owner o, double dis) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        double totdis = o.getTot_dis() + dis;
        o.setTot_dis(totdis);
        int walks = o.getTot_walks() + 1;
        o.setTot_walks(walks);

        values.put(COL_TOT_WALKS, walks);
        values.put(COL_TOT_DIS, String.valueOf(totdis));

        db.update(OWNER_LOGIN_TABLE,
                values,
                COL_ID + " = " + o.getId(),
                null);
        db.close();

        Log.d("Database", o.getId() + " Walk Added " + o.getName() + " : " + o.getTot_walks() + " : " + o.getTot_dis());
    }

    /*Returns the OWNER_LOGIN_TABLE string*/
    public String getOwnerLogintable() {
        return OWNER_LOGIN_TABLE;
    }

    /*Returns the DATABASE_NAME string*/
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    /*Returns the COL_ID string*/
    public static String getColId() {
        return COL_ID;
    }

    /*Returns the COL_EMAIL string*/
    public static String getColEmail() {
        return COL_EMAIL;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldNum, int newNum) {
        db.execSQL("DROP TABLE IF EXISTS " + OWNER_LOGIN_TABLE);
        onCreate(db);
    }

}