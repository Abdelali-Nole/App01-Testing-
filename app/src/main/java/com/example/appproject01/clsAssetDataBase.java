package com.example.appproject01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class clsAssetDataBase extends SQLiteAssetHelper {


    public static final String table_Name="Cars";

    public static final String DB_NAME="vehicles.db";
    public static final int DB_VERSION =1;

    private Context context;

    public clsAssetDataBase(Context context){

        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }
}
