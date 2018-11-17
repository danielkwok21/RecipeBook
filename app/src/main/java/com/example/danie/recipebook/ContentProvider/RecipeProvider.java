package com.example.danie.recipebook.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.danie.recipebook.Util;

import java.util.HashMap;

public class RecipeProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.example.danie.recipebook.ContentProvider.RecipeProvider";
    private static final String PATH = "rprecipes";
    private static final String URL = "content://"+PROVIDER_NAME+"/"+PATH;
    private static final Uri CONTENT_URL = Uri.parse(URL);
    private static final String ANDROID_CURSOR_DIR = "vnd.android.cursor.dir";

    //fields
    private static final String id = "id";
    private static final String name = "name";
    private static final String instructions = "instructions";

    private static final int uriCode = 1;
    private static HashMap<String, String> values;
    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "PATH", uriCode);
    }

    //db
    private SQLiteDatabase recipeDB;
    private static final String DB_NAME = "recipesDB";
    private static final String TABLE_NAME = "recipeTable";
    private static final int DB_VERSION = 1;

    //sql
    private static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME + "(" +
                id+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                name+ " TEXT NOT NULL, " +
                instructions + " TEXT" +
            ");";

    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        recipeDB = dbHelper.getWritableDatabase();

        if(recipeDB!=null){
            return true;
        }
        return false;
    }
    /*
     * uri = unique uri of the content provider
     * projection = an array of fields intended to retrieve with query
     * selection = WHERE XXX IS YYY
     * selectionArgs = an array of YYY, intended to be used with selection
     * */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_NAME);

        switch(uriMatcher.match(uri)){
            case uriCode:
                sqLiteQueryBuilder.setProjectionMap(values);    //all fields intended to retrieve
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+uri);
        }

        Cursor cursor = sqLiteQueryBuilder.query(recipeDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);  //set onchange listener

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch(uriMatcher.match(uri)){
            case uriCode:
                return ANDROID_CURSOR_DIR+"/"+PATH;
            default:
                throw new IllegalArgumentException("Unsupported URI: "+uri);
        }
    }

    
    @Override
    public Uri insert(Uri uri,  ContentValues values) {
        long rowID = recipeDB.insert(TABLE_NAME, null, values);

        if(rowID>0){
            //new record inserted
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }else{
            Util.Toast(getContext(), "Row insertion failed");
        }
        return null;
    }

    @Override
    public int delete(Uri uri,  String selection,  String[] selectionArgs) {
        int rowsDeleted = 0;

        switch(uriMatcher.match(uri)){
            case uriCode:
                rowsDeleted = recipeDB.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int rowsUpdated = 0;

        switch(uriMatcher.match(uri)){
            case uriCode:
                rowsUpdated = recipeDB.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}
