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


    public static final String TABLA_DEUDAS_BD="DeudasBD";
    public static final String CAMPO_PAGAR_DEBER_BD = "pagardeber";
    public static final String CAMPO_ID_DEUDA="id";
    public static final String CAMPO_NOMBRE_DEUDA="nombre";
    public static final String CAMPO_IMPORTE_DEUDA="importe";
    public static final String CAMPO_FECHA_DEUDA="fecha";
    public static final String CAMPO_CONCEPTO_DEUDA="concepto";

    public static final String CREAR_TABLA_DEUDAS="CREATE TABLE " +
            ""+TABLA_DEUDAS_BD+" ("+ CAMPO_PAGAR_DEBER_BD+ " "+ " INTEGER, " +CAMPO_ID_DEUDA+ " "+ " INTEGER, " +CAMPO_NOMBRE_DEUDA+" TEXT,"+CAMPO_IMPORTE_DEUDA+" TEXT,"  +CAMPO_FECHA_DEUDA+" DATE," +CAMPO_CONCEPTO_DEUDA+" TEXT)";

    public static final String TABLA_OBJETIVOS_BD="ObjetivosBD";
    public static final String CAMPO_GASTO_AHORRO = "gastoahorro";
    public static final String CAMPO_ID_OBJETIVO="id";
    public static final String CAMPO_CANTIDAD_OBJETIVO="cantidad";
    public static final String CAMPO_FECHA_INICIO_OBJETIVO="fechainicio";
    public static final String CAMPO_FECHA_FIN_OBJETIVO="fechafin";
    public static final String CAMPO_MOTIVO_OBJETIVO="motivo";

    public static final String CREAR_TABLA_OBJETIVOS="CREATE TABLE " +
            ""+TABLA_OBJETIVOS_BD+" ("+ CAMPO_GASTO_AHORRO+ " "+ " INTEGER, " +CAMPO_ID_OBJETIVO+ " "+ " INTEGER, " +CAMPO_CANTIDAD_OBJETIVO+" TEXT,"+CAMPO_FECHA_INICIO_OBJETIVO + " DATE," + CAMPO_FECHA_FIN_OBJETIVO + " DATE,"  +CAMPO_MOTIVO_OBJETIVO+" TEXT)";

}
