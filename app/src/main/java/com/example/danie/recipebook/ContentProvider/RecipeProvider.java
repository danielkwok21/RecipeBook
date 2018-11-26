package com.example.danie.recipebook.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.danie.recipebook.Contract;
import com.example.danie.recipebook.Util;

import java.util.HashMap;

public class RecipeProvider extends ContentProvider {



    //fields
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String INSTRUCTIONS = "instructions";

    private static final int uriCode = 1;
    private static HashMap<String, String> values;

    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.PROVIDER_NAME, "recipes", uriCode);
    }

    //db
    private SQLiteDatabase db;
    private static final String DB_NAME = "recipesDB";
    private static final String TABLE_NAME = "recipeTable";
    private static final int DB_VERSION = 1;

    //sql
    private static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME + " TEXT NOT NULL, " +
                    INSTRUCTIONS + " TEXT" +
            ");";

    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        //if db doesn't exists already, create one
        if(db !=null){
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
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch(uriMatcher.match(uri)){
            case uriCode:
                qb.setProjectionMap(values);    //all fields intended to retrieve
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+uri);
        }

        //default sortOrder is by NAME if none specified
        if(sortOrder.isEmpty()){
            sortOrder = NAME;
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);  //set onchange listener

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch(uriMatcher.match(uri)){
            case uriCode:
                return Contract.ANDROID_CURSOR_DIR+"/"+Contract.PATH;
            default:
                throw new IllegalArgumentException("Unsupported URI: "+uri);
        }
    }
    
    @Override
    public Uri insert(Uri uri,  ContentValues values) {
        long rowID = db.insert(TABLE_NAME, null, values);


        //if new record inserted successfully
        if(rowID>0){
            Uri _uri = ContentUris.withAppendedId(Contract.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);

            return _uri;
        }else{
            Util.Toast(getContext(), "Row insertion failed");
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri,  String selection,  String[] selectionArgs) {
        int rowsDeleted = 0;

        switch(uriMatcher.match(uri)){
            case uriCode:
                rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
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
                rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context) {
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
