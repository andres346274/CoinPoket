package com.uc3m.it.CoinPocket.utilidades;

public class utilidades {

    //Constantes campos tabla gastos
    public static final String TABLA_GASTOS="Gastos_BD";
    public static final String CAMPO_ID_GASTO="id";
    public static final String CAMPO_CONCEPTO_GASTO="concepto";
    public static final String CAMPO_CANTIDAD_GASTO="cantidad";
    public static final String CAMPO_FECHA_GASTO="fecha_gasto";
    public static final String CAMPO_LOCALIZACION_GASTO="localizacion_gasto";


    public static final String CREAR_TABLA_GASTOS="CREATE TABLE " +
            ""+TABLA_GASTOS+" ("+ CAMPO_ID_GASTO + " "+ " INTEGER, " +CAMPO_CONCEPTO_GASTO+" TEXT,"+CAMPO_CANTIDAD_GASTO+" TEXT)";

}
