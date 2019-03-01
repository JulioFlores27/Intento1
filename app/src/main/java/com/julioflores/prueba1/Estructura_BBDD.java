package com.julioflores.prueba1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Estructura_BBDD extends SQLiteOpenHelper {
    public Estructura_BBDD(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table almacensqlite(id INTEGER PRIMARY KEY AUTOINCREMENT, matpria1 TEXT, nolotea1 TEXT, cantia1 TEXT," +
                "racka1 TEXT, fila1 TEXT, cola1 TEXT, fechahora1 TEXT, usuara1 TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists almacensqlite");
        db.execSQL("create table almacensqlite(id INTEGER PRIMARY KEY AUTOINCREMENT, matpria1 TEXT, nolotea1 TEXT, cantia1 TEXT," +
                "racka1 TEXT, fila1 TEXT, cola1 TEXT, fechahora1 TEXT, usuara1 TEXT)");
    }
}
