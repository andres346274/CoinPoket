package com.uc3m.it.CoinPocket.response;

  /*
  Llega de Foreign Exchange Rates API un objeto mapa con formato clave-valor (ver RatioResponse),
  lo que se busca es obtener un objeto (ratio) que tenga de atributos un nombre y un valor.
   */
public class Ratio {
    private String name;
    private Float value;

    public Ratio(String name) {
        this.name = name;
        this.value = 1f;
    }

    public Ratio(String name, Float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
