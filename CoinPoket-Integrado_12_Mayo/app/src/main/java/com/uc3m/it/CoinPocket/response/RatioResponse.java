package com.uc3m.it.CoinPocket.response;

import java.util.Map;


 /*
  Llega de Foreign Exchange Rates API un objeto mapa que tienen formato de clave-valor.
  En este caso lo que llega de la API es una clave de tipo string, y un valor de tipo float.
  */

public class RatioResponse {
    private String base, date;
    private Map<String, Float> rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Float> getRates() {
        return rates;
    }
}
