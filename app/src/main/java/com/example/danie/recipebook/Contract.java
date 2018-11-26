package com.example.danie.recipebook;

import android.net.Uri;

public class Contract {
    /**
     * these public final variables are for other classes/activities when intend to access this content provider
     */
    //config
    public static final String PROVIDER_NAME = "com.example.danie.recipebook.ContentProvider.RecipeProvider";
    public static final String PATH = "recipes";
    public static final String URL = "content://"+PROVIDER_NAME+"/"+PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String ANDROID_CURSOR_DIR = "vnd.android.cursor.dir";
}
