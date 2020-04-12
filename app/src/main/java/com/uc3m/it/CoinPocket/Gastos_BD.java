package com.uc3m.it.CoinPocket;

public class Gastos_BD {

    private Integer id;
    private String concepto;
    private String cantidad;
    private String fecha;
    private String localizacion;

    public Gastos_BD(){
        this.id=id;
        this.concepto=concepto;
        this.cantidad=cantidad;

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
