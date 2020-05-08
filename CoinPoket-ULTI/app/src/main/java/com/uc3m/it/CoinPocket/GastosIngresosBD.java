package com.uc3m.it.CoinPocket;

import java.util.Calendar;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 */

public class  GastosIngresosBD {

    private Integer gastoingreso;
    private Integer id;
    private String concepto;
    private String cantidad;
    private String fecha;
    private String localizacion;

    public GastosIngresosBD(){
        this.gastoingreso = gastoingreso;
        this.id=id;
        this.concepto=concepto;
        this.cantidad=cantidad;
        this.fecha =fecha;
        this.localizacion = localizacion;

    }

    //GastoIngreso es un booleano hecho con Integer que indica si lo insertado es un gasto (1) o si es un ingreso (0)

    public Integer getGastoingreso() {
        return gastoingreso;
    }

    public void setGastoingreso(Integer gastoIngreso) {
        this.gastoingreso = gastoIngreso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }
}

//Fuente del código https://www.youtube.com/watch?v=ml0i0hnL_WY
