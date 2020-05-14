package com.uc3m.it.CoinPocket;

public class ObjetivosBD {

    //Creación de campos por Deuda contenidos en la BD
    private Integer gastoahorro;
    private Integer id;
    private String cantidad;
    private String fechainicio;
    private String fechafin;
    private String motivo;

    public ObjetivosBD(){
        this.gastoahorro = gastoahorro;
        this.id=id;
        this.cantidad=cantidad;
        this.fechainicio =fechainicio;
        this.fechafin =fechafin;
        this.motivo=motivo;
    }

    //Definición de métodos get y set para cada campo

    public Integer getGastoahorro() {
        return gastoahorro;
    }

    public void setGastoahorro(Integer gastoahorro) {
        this.gastoahorro = gastoahorro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
