package com.careeranna.careeranna.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class database extends SQLiteAssetHelper {
    private final String DB_NAME = "CARERRANN.DB";

    public database(Context context, String name, String storageDirectory, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, storageDirectory, factory, version);
    }
}
