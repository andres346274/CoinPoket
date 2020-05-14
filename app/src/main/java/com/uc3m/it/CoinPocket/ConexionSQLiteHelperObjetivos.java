package com.uc3m.it.CoinPocket;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

public class ConexionSQLiteHelperObjetivos extends SQLiteOpenHelper{


    //Asignación del constructor
    public ConexionSQLiteHelperObjetivos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(utilidades.CREAR_TABLA_OBJETIVOS);
    }

    /**
     Verificación de si existe ya una version antigua de nuestra BD
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + utilidades.TABLA_OBJETIVOS_BD);
        onCreate(db);
    }
}
