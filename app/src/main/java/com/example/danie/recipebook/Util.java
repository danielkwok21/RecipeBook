package com.example.danie.recipebook;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void Toast(Context c, String s){
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }
}
