package com.uc3m.it.CoinPocket.utilidades;

public class utilidades {

    //Constantes campos tabla gastos
    public static final String TABLA_GASTOS_INGRESOS_BD="GastosIngresosBD";
    public static final String CAMPO_GASTO_INGRESO = "gastoingreso";
    public static final String CAMPO_ID_GASTO_INGRESO="id";
    public static final String CAMPO_CONCEPTO_GASTO_INGRESO="concepto";
    public static final String CAMPO_CANTIDAD_GASTO_INGRESO="cantidad";
    public static final String CAMPO_FECHA_GASTO_INGRESO="fecha";
    public static final String CAMPO_LOCALIZACION_GASTO_INGRESO="localizacion";


    public static final String CREAR_TABLA_GASTOS_INGRESOS="CREATE TABLE " +
            ""+TABLA_GASTOS_INGRESOS_BD+" ("+ CAMPO_GASTO_INGRESO+ " "+ " INTEGER, " +CAMPO_ID_GASTO_INGRESO+ " "+ " INTEGER, " +CAMPO_CONCEPTO_GASTO_INGRESO+" TEXT,"+CAMPO_CANTIDAD_GASTO_INGRESO+" TEXT,"  +CAMPO_FECHA_GASTO_INGRESO+" DATE," +CAMPO_LOCALIZACION_GASTO_INGRESO+" TEXT)";

}
