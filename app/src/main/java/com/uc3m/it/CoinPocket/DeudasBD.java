package com.uc3m.it.CoinPocket;


/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 */

public class DeudasBD {

        //Creación de campos por Deuda contenidos en la BD
        private Integer pagardeber;
        private Integer id;
        private String nombre;
        private String importe;
        private String fecha;
        private String concepto;

        public DeudasBD(){
            this.pagardeber = pagardeber;
            this.id=id;
            this.nombre=nombre;
            this.importe = importe;
            this.fecha =fecha;
            this.concepto=concepto;
        }

    //Definición de métodos get y set para cada campo
    public Integer getPagardeber() {
        return pagardeber;
    }

    public void setPagardeber(Integer pagardeber) {
        this.pagardeber = pagardeber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
}
