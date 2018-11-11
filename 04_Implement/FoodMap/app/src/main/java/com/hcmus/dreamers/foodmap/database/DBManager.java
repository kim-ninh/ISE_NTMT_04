package com.hcmus.dreamers.foodmap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import com.hcmus.dreamers.foodmap.Model.Catalog;
import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.Model.Dish;
import com.hcmus.dreamers.foodmap.Model.Restaurant;

import org.osmdroid.util.GeoPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager extends SQLiteOpenHelper {
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "FOODMAP_DATABASE";

    // Table name
    private static final String TABLE_RESTAURANT = "RESTAURANT";
    private static final String TABLE_LOCATION = "LOCATION";
    private static final String TABLE_DISH = "DISH";
    private static final String TABLE_CATALOGS = "CATALOGS";
    private static final String TABLE_COMMENTS = "COMMENTS";
    private static final String TABLE_RANK = "RANK";

    // Common column names
    private static final String KEY_NAME = "NAME";
    private static final String KEY_ID = "ID";
    private static final String KEY_ID_REST = "ID_REST";
    private static final String KEY_URL_IMAGE = "URL_IMAGE";

    // RESTAURANT Table - column names
    //private static final String KEY_ID = "ID";
    private static final String KEY_ID_USER = "ID_USER";
    //private static final String KEY_NAME = "NAME";
    private static final String KEY_ADDRESS = "ADDRESS";
    private static final String KEY_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String KEY_DESCRIBE_TEXT = "DESCRIBE_TEXT";
    //private static final String KEY_URL_IMAGE = "URL_IMAGE";
    private static final String KEY_TIME_OPEN = "TIME_OPEN";
    private static final String KEY_TIME_CLOSE = "TIME_CLOSE";

    // LOCATION Table - column names
    //private static final String ID_REST = "ID_REST";
    private static final String KEY_LAT = "LAT";
    private static final String KEY_LON = "LON";

    // DISH Table - column names
    //private static final String KEY_NAME = "NAME";
    private static final String KEY_PRICE = "PRICE";
    //private static final String KEY_URL_IMAGE = "URL_IMAGE";
    private static final String KEY_ID_CATALOG = "ID_CATALOG";

    // CATALOGS Table - column names
    //private static final String KEY_ID = "KEY_ID";
    //private static final String KEY_NAME = "KEY_NAME";

    // COMMENTS Table - column names
    private static final String KEY_DATE_TIME = "DATE_TIME";
    //private static final String KEY_ID_REST = "ID_REST";
    private static final String KEY_GUEST_EMAIL = "GUEST_EMAIL";
    private static final String KEY_OWNER_EMAIL = "OWNER_EMAIL";
    private static final String KEY_COMMENT = "COMMENT";

    // RANK Table - column names
    //private static final String KEY_ID_REST = "ID_REST";
    private static final String KEY_EMAIL_GUEST = "EMAIL_GUEST";
    private static final String KEY_STAR = "START";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_RESTAURANT = "CREATE TABLE " + TABLE_RESTAURANT + "("
                + KEY_ID + " INT PRIMARY KEY,"
                + KEY_ID_USER + " VARCHAR(20),"
                + KEY_NAME + " VARCHAR(100),"
                + KEY_ADDRESS + " VARCHAR(50),"
                + KEY_PHONE_NUMBER + " VARCHAR(11) CHECK(LENGTH(" + KEY_PHONE_NUMBER + ") IN (10, 11)),"
                + KEY_DESCRIBE_TEXT + " TEXT,"
                + KEY_URL_IMAGE + " VARCHAR(200),"
                + KEY_TIME_OPEN + " CHAR(5),"       // hh:mm
                + KEY_TIME_CLOSE + " CHAR(5));";

        final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + "("
                + KEY_ID_REST + " INT NOT NULL,"
                + KEY_LAT + " DOUBLE NOT NULL,"
                + KEY_LON + " DOUBLE NOT NULL,"
                + "PRIMARY KEY (" + KEY_LAT + ", " + KEY_LON + "), "
                + "FOREIGN KEY (" + KEY_ID_REST + ") REFERENCES " + TABLE_RESTAURANT + "(" + KEY_ID + "));";


        final String CREATE_TABLE_DISH = "CREATE TABLE " + TABLE_DISH + "("
                + KEY_NAME + " VARCHAR(50) NOT NULL,"
                + KEY_ID_REST + " INT,"
                + KEY_PRICE + " INT,"
                + KEY_URL_IMAGE + " VARCHAR(200),"
                + KEY_ID_CATALOG + " INT,"
                + "PRIMARY KEY (" + KEY_NAME + ", " + KEY_ID_REST + "), "
                + "FOREIGN KEY (" + KEY_ID_REST + ") REFERENCES " + TABLE_RESTAURANT + "(" + KEY_ID + "), "
                + "FOREIGN KEY (" + KEY_ID_CATALOG + ") REFERENCES " + TABLE_CATALOGS + "(" + KEY_ID + "));";

        final String CREATE_TABLE_CATALOGS = "CREATE TABLE " + TABLE_CATALOGS + "("
                + KEY_ID + " INT PRIMARY KEY,"
                + KEY_NAME + " VARCHAR(30) UNIQUE NOT NULL" + ")";

        final String CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS + "("
                + KEY_DATE_TIME + " DATE NOT NULL,"     // ngày comment (dd-MM-yyy HH:mm:ss)
                + KEY_ID_REST + " INT NOT NULL,"
                + KEY_GUEST_EMAIL + " VARCHAR(30),"
                + KEY_OWNER_EMAIL + " VARCHAR(30),"
                + KEY_COMMENT + " VARCHAR(200) NOT NULL,"
                + "PRIMARY KEY (" + KEY_DATE_TIME + ", " + KEY_ID_REST + "), "
                + "FOREIGN KEY (" + KEY_ID_REST + ") REFERENCES " + TABLE_RESTAURANT + "(" + KEY_ID + "));";

        final String CREATE_TABLE_RANK = "CREATE TABLE " + TABLE_RANK + "("
                + KEY_ID_REST + " INT NOT NULL,"
                + KEY_EMAIL_GUEST + " VARCHAR(30),"
                + KEY_STAR + " INT NOT NULL CHECK(" + KEY_STAR + " >= 1 AND " + KEY_STAR + " <= 5),"
                + "PRIMARY KEY(" + KEY_ID_REST + ", " + KEY_EMAIL_GUEST + "), "
                + "FOREIGN KEY (" + KEY_ID_REST + ") REFERENCES " + TABLE_RESTAURANT + "(" + KEY_ID + "));";

        // Create table(s)
        db.execSQL(CREATE_TABLE_RESTAURANT);
        db.execSQL(CREATE_TABLE_CATALOGS);
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_DISH);
        db.execSQL(CREATE_TABLE_COMMENTS);
        db.execSQL(CREATE_TABLE_RANK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addCatalog(Catalog catalog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(KEY_ID, catalog.getId());
        value.put(KEY_NAME, catalog.getName());

        db.insert(TABLE_CATALOGS, null, value);
        db.close();
    }

    public void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        value.put(KEY_DATE_TIME, df.format(comment.getDateTime()));
        value.put(KEY_NAME, comment.getComment());
        value.put(KEY_GUEST_EMAIL, comment.getEmailGuest());
        value.put(KEY_OWNER_EMAIL, comment.getEmailOwner());

        db.insert(TABLE_COMMENTS, null, value);
        db.close();
    }

    public void addDish(Dish dish) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(KEY_NAME, dish.getName());
        value.put(KEY_PRICE, dish.getPrice());
        value.put(KEY_URL_IMAGE, dish.getUrlImage());
        value.put(KEY_ID_CATALOG, dish.getCatalog().getId());

        db.insert(TABLE_DISH, null, value);
        db.close();
    }

    public void addLocation(int id_rest, GeoPoint location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(KEY_ID_REST, id_rest);
        value.put(KEY_LAT, location.getLatitude());
        value.put(KEY_LAT, location.getLongitude());

        db.insert(TABLE_LOCATION, null, value);
        db.close();
    }

    public void addRank(int id_rest, String email_guest, int star) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(KEY_ID_REST, id_rest);
        value.put(KEY_EMAIL_GUEST, email_guest);
        value.put(KEY_STAR, star);

        db.insert(TABLE_CATALOGS, null, value);
        db.close();
    }

    public void addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        // add common data
        value.put(KEY_ID, restaurant.getId());
        value.put(KEY_NAME, restaurant.getName());
        value.put(KEY_ADDRESS, restaurant.getAddress());
        value.put(KEY_PHONE_NUMBER, restaurant.getPhoneNumber());
        value.put(KEY_DESCRIBE_TEXT, restaurant.getDescription());
        value.put(KEY_URL_IMAGE, restaurant.getUrlImage());
        value.put(KEY_TIME_OPEN, restaurant.getTimeOpen().toString());
        value.put(KEY_TIME_CLOSE, restaurant.getTimeClose().toString());

        db.insert(TABLE_CATALOGS, null, value);
        db.close();

        // add Location data
        GeoPoint location = restaurant.getLocation();
        addLocation(restaurant.getId(), location);

        // add Dishes data
        List<Dish> list_dish = restaurant.getDishes();
        for (Dish dish : list_dish) {
            this.addDish(dish);
        }

        // add Comments
        List<Comment> list_comment = restaurant.getComments();
        for (Comment comment : list_comment) {
            this.addComment(comment);
        }

        // add Ranks
        HashMap<String, Integer> ranks = restaurant.getRanks();
        for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
            addRank(restaurant.getId(), entry.getKey(), entry.getValue());
        }
    }

    public Catalog getCatalog(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CATALOGS
                    + " WHERE " + KEY_ID + " = " + String.valueOf(id);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();

        return new Catalog(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)));
    }

    public List<Comment> getComments(int id_rest) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Comment> comments = new ArrayList<Comment>();

        String query = "SELECT * FROM " + TABLE_COMMENTS
                + " WHERE " + KEY_ID_REST + " = " + String.valueOf(id_rest)
                + " ORDER BY " + KEY_DATE_TIME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

            do {
                comments.add(new Comment(df.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME))),
                        cursor.getString(cursor.getColumnIndex(KEY_COMMENT)),
                        cursor.getString(cursor.getColumnIndex(KEY_GUEST_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(KEY_OWNER_EMAIL))));
            } while (cursor.moveToNext());
        }

        return comments;
    }

    public List<Dish> getDishes(int id_rest) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Dish> dishes = new ArrayList<Dish>();

        String query = "SELECT * FROM " + TABLE_DISH
                + " WHERE " + KEY_ID_REST + " = " + String.valueOf(id_rest)
                + " ORDER BY " + KEY_PRICE;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do {
                dishes.add(new Dish(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PRICE)),
                        cursor.getString(cursor.getColumnIndex(KEY_URL_IMAGE)),
                        getCatalog(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATALOG)))));
            } while (cursor.moveToNext());
        }

        return dishes;
    }

    public GeoPoint getLocation(int id_rest) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_LOCATION
                + " WHERE " + KEY_ID_REST + " = " + String.valueOf(id_rest);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null)
            return null;
        cursor.moveToFirst();

        return new GeoPoint(cursor.getDouble(cursor.getColumnIndex(KEY_LAT)),
                cursor.getDouble(cursor.getColumnIndex(KEY_LON)));
    }

    public HashMap<String, Integer> getRanks(int id_rest) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String , Integer> ranks = new HashMap<>();

        String query = "SELECT * FROM " + TABLE_RANK
                + " WHERE " + KEY_ID_REST + " = " + String.valueOf(id_rest);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do{
                ranks.put(cursor.getString(cursor.getColumnIndex(KEY_EMAIL_GUEST)),
                        cursor.getInt(cursor.getColumnIndex(KEY_STAR)));
            } while (cursor.moveToNext());
        }

        return ranks;
    }

    public Restaurant getRestaurant(int id_rest) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_RESTAURANT
                + " WHERE " + KEY_ID + " = " + String.valueOf(id_rest);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null)
            return null;

        DateFormat df = new SimpleDateFormat("HH:mm");

        Restaurant restaurant = new Restaurant(id_rest,
                                                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                                                cursor.getString(cursor.getColumnIndex(KEY_ID_USER)),
                                                cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                                                cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)),
                                                cursor.getString(cursor.getColumnIndex(KEY_DESCRIBE_TEXT)),
                                                cursor.getString(cursor.getColumnIndex(KEY_URL_IMAGE)),
                                                df.parse(cursor.getString(cursor.getColumnIndex(KEY_TIME_OPEN))),
                                                df.parse(cursor.getString(cursor.getColumnIndex(KEY_TIME_CLOSE))),
                                                getLocation(id_rest));

        restaurant.setDishes(getDishes(id_rest));
        restaurant.setComments(getComments(id_rest));
        restaurant.setRanks(getRanks(id_rest));

        return restaurant;
    }

    public List<Restaurant> getAllRestaurant() throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Restaurant> restaurants = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_RESTAURANT
                    + " ORDER BY " + KEY_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            DateFormat df = new SimpleDateFormat("HH:mm");

            do {
                Restaurant restaurant = new Restaurant(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_ID_USER)),
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIBE_TEXT)),
                        cursor.getString(cursor.getColumnIndex(KEY_URL_IMAGE)),
                        df.parse(cursor.getString(cursor.getColumnIndex(KEY_TIME_OPEN))),
                        df.parse(cursor.getString(cursor.getColumnIndex(KEY_TIME_CLOSE))),
                        getLocation(cursor.getInt(cursor.getColumnIndex(KEY_ID))));

                restaurant.setDishes(getDishes(restaurant.getId()));
                restaurant.setComments(getComments(restaurant.getId()));
                restaurant.setRanks(getRanks(restaurant.getId()));

                restaurants.add(restaurant);
            } while (cursor.moveToNext());
        }

        return restaurants;
    }
}

