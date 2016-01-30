package com.example.zhenmin.min_note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhenmin on 2015/11/4.
 */
public class OpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE_NOTE = "create table Note ("
            +"id integer primary key AUTOINCREMENT,"
            +"title text ,"
            +"img_path text,"
            +"vedio text,"
            +"buildTime text ,"
            +"content text )";


    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 3:db.execSQL(CREATE_TABLE_NOTE);
        }

    }
}
