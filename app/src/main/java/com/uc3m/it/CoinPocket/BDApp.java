package com.uc3m.it.CoinPocket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BDApp {
    private static final String TAG = "APMOV: BDApp"; // Usado en los mensajes de Log

    //Nombre de la base de datos, tablas y versión
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_GASTOS = "gastos";
    private static final String DATABASE_TABLE_INGRESOS = "ingresos";
    private static final int DATABASE_VERSION = 2;

    public static final String Fila = "fila";

    //campos de la primera tabla de la base de datos (Ingresos)
    public static final String cIngresos = "id_cIngreso";
    public static final String iCantidad = "id_nIngreso";
    public static final String iFecha = "id_calIngreso";

    //campos de la segunda tabla de la base de datos (Gastos)
    public static final String cGastos = "id_cGasto";
    public static final String gCantidad = "id_nGasto";
    public static final String gFecha = "id_calGasto";

    // Sentencia SQL para crear las tablas de las bases de datos
    private static final String CREAR_GASTOS = "create table " + DATABASE_TABLE_INGRESOS+ " (" +
            Fila + " integer primary key autoincrement, " +
            cIngresos + " text not null, " +
            iCantidad + " text not null," +
            iFecha + "text not null);";

    private static final String CREAR_INGRESOS = "create table " + DATABASE_TABLE_GASTOS + " (" +
            Fila + " integer primary key autoincrement, " +
            cGastos + " real not null, " +
            gCantidad + " real not null," +
            gFecha + "text not null);";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREAR_GASTOS);
            db.execSQL(CREAR_INGRESOS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GASTOS);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_INGRESOS);
            onCreate(db);
        }
    }

    public BDApp(Context ctx) { this.mCtx = ctx; }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public BDApp open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() { mDbHelper.close(); }

    public long guardarIngreso(String conc, String cant, String fecha) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(conc, cIngresos);
        initialValues.put(cant, iCantidad);
        initialValues.put(fecha, iFecha);

        return mDb.insert(DATABASE_TABLE_INGRESOS, null, initialValues);
    }

    public long guardarGasto(String conc, String cant, String fecha) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(conc, cGastos);
        initialValues.put(cant, gCantidad);
        initialValues.put(fecha, gFecha);

        return mDb.insert(DATABASE_TABLE_GASTOS, null, initialValues);
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchIngreso() {

        return mDb.query(DATABASE_TABLE_INGRESOS, new String[] {Fila, cIngresos, iCantidad,
                iFecha}, null, null, null, null, null);
    }

    public Cursor fetchGasto() {

        return mDb.query(DATABASE_TABLE_GASTOS, new String[] {Fila, cGastos, gCantidad,
                gFecha}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchIng(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_INGRESOS, new String[] {Fila,
                                cIngresos, iCantidad, iFecha}, Fila + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchGasto(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_GASTOS, new String[] {Fila,
                                cGastos, gCantidad, gFecha}, Fila + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param Fila id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateIng(long rowId, String CONC, String CAN, String FEC) {
        ContentValues args = new ContentValues();
        args.put(cIngresos, CONC);
        args.put(iCantidad, CAN);
        args.put(iFecha, FEC);

        return mDb.update(DATABASE_TABLE_INGRESOS, args, Fila + "=" + rowId, null) > 0;
    }

    public boolean updateGas(long rowId, String ING, String CAN, String FEC) {
        ContentValues args = new ContentValues();
        args.put(cGastos, ING);
        args.put(gCantidad, CAN);
        args.put(gFecha, FEC);

        return mDb.update(DATABASE_TABLE_GASTOS, args, Fila + "=" + rowId, null) > 0;
    }

}
