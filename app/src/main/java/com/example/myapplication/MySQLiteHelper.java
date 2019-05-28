package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SETTINGDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE settings ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "power TEXT, "+
                "harga TEXT )";

        // create settings table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older settings table if existed
        db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh settings table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all settings + delete all settings
     */

    // Books table name
    private static final String TABLE_BOOKS = "settings";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_POWER = "power";
    private static final String KEY_HARGA = "harga";

    private static final String[] COLUMNS = {KEY_POWER, KEY_HARGA};

    public String getSetting(int i){
        int id=1;
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_BOOKS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
//        Book book = new Book();
//        book.setId(Integer.parseInt(cursor.getString(0)));
//        book.setTitle(cursor.getString(1));
//        book.setAuthor(cursor.getString(2));

        //Log.d("getSetting("+id+")", book.toString());

        // 5. return book
        String data= cursor.getString(i);
        return data.toString();
    }

    // Updating single book
    public int updateBook(String harga, String power) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("harga", harga); // get title
        values.put("power", power); // get author

        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(1) }); //selection args

        // 4. close
        db.close();

        return i;

    }
}