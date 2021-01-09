package com.gabriel.hwman.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static String path;
    private static String name = "gabrielhw.db";
    private static int version;
    private static Context context;
    private static SQLiteDatabase database;


    public Database(Context context, int version) {
        super(context, name, null, version);
        this.version = version;
        this.context = context;
        this.path = context.getFilesDir().getPath().replace("files", "databases") + "/";
    }

    public static Cursor select(String query) throws SQLException {
        Database database = new Database(context, version);
        database.openDataBase();

        String[] where = {"1 = 1"};
        SQLiteDatabase sdb = database.getReadableDatabase();
        Cursor c = sdb.rawQuery(query, where);
        c.moveToFirst();
        database.close();
        return c;
    }

    public static String getData(Cursor c, String field){
        return c.getString(c.getColumnIndex(field));
    }

    public static Cursor select(String query, String[] where) throws SQLException {
        Database database = new Database(context, version);
        database.openDataBase();
        SQLiteDatabase sdb = database.getReadableDatabase();
        Cursor c = sdb.rawQuery(query, where);
        c.moveToFirst();
        database.close();
        return c;
    }

    public static long insert(String table, ContentValues valuesToInsert) {
        Database dbh = new Database(context, PropertiesManager.getDbVersionNumber());
        SQLiteDatabase db = dbh.getWritableDatabase();
        long l = db.insert(table, null, valuesToInsert);
        return l;
    }

    public static int update(String table, ContentValues columnsToUpdate, String where, String[] conditions) {
        int i = -1;
        try {
            Database dbh = new Database(context, PropertiesManager.getDbVersionNumber());
            SQLiteDatabase db = dbh.getWritableDatabase();
            i = db.update(table, columnsToUpdate, where, conditions);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return i;
    }

    public static int delete(String table, String where, String[] conditions) {
        Database dbh = new Database(context, PropertiesManager.getDbVersionNumber());
        SQLiteDatabase db = dbh.getWritableDatabase();
        int i = db.delete(table, where, conditions);
        return i;
    }

    public void createDataBase(boolean forceReset) throws IOException {

        boolean dbExist = checkDataBase();

        if (forceReset || !dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else {
            System.out.println("Database already exists!");
        }

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = path + name;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist yet!");
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open("user/gabrielhw.db");
        String outFileName = path + name;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = path + name;
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public static void clearAll() {
        // reset database
        try {
            Database database = new Database(context, PropertiesManager.getDbVersionNumber());
            database.createDataBase(true);
        } catch (IOException e) {
            System.out.println("Error resetting database!");
        }
    }

    public ArrayList<String> listAllTables() throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        Database database = new Database(context, version);
        database.openDataBase();

        String[] where = {"table"};
        Cursor c = database.select("SELECT name FROM sqlite_master WHERE type = ?", where);

        while (c.moveToNext()) {
            result.add(c.getString(c.getColumnIndex("name")));
        }
        database.close();
        return result;
    }


    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this will execute every time the database version is changed
        if (oldVersion >= 3) {
            //db.execSQL("ALTER TABLE OPTIONS ADD COLUMN AUTH INTEGER;");
        }
    }
}