package com.uc3m.it.CoinPocket;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper{


    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(utilidades.CREAR_TABLA_GASTOS);
    }

    /**
     El método upgrade verifica si existe ya una version antigua de nuestra BD
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + utilidades.TABLA_GASTOS);
        onCreate(db);
    }

    /**
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                "_id",
          utilidades.CAMPO_CONCEPTO_GASTO
        };
        String sorOrder = "_id" + "DESC";

        return db.query(
                utilidades.TABLA_GASTOS,
                projection,
                null, null, null, null,
                sorOrder
        );

        /*String query = "SELECT * FROM " + utilidades.TABLA_GASTOS;
        Cursor data = db.rawQuery(query, null);
        return data;*/
    //}
}

//Funte del codigo https://www.youtube.com/watch?v=ml0i0hnL_WY
