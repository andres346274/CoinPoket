package com.uc3m.it.CoinPocket.data;

import com.uc3m.it.CoinPocket.response.Ratio;
import com.uc3m.it.CoinPocket.response.RatioResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

   /*
   Se ha hecho un Singleton para que el objeto no se guarde, no hay necesidad de tener un histórico de
   precios de la moneda, sólo de la actual.
   Código basado en: https://jarroba.com/patron-singleton-en-java-con-ejemplos/
    */
public class RatioSingleton {

    private static RatioSingleton INSTANCE;
    public RatioResponse ratio;

    /*
    El constructor es privado, no permite que se genere un constructor por defecto, es propio del
    implemento de Singleton.
      */
    private RatioSingleton() {

    }

    /*
     Con el siguiente método se recorre el mapa que llega de la llamada a la API y se guarda cada
     clave y cada valor en un objeto ratio. (Consultad ratioResponse y ratio)
     */
    public static List<Ratio> getRatios() {
        List<Ratio> strings = new ArrayList<>();
        strings.add(new Ratio("EUR"));
        RatioResponse ratio = RatioSingleton.getInstance().ratio;
        if (ratio != null){
            for (Map.Entry<String, Float> entry : ratio.getRates().entrySet()){
                strings.add(new Ratio(entry.getKey(), entry.getValue()));
            }
        }
        return strings;
    }

    /*
    Método encargado de crear una instancia de esta clase si no se ha creado todavía, es propio del
    implemento de Singleton.
     */
    public static RatioSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RatioSingleton();
        }
        return INSTANCE;
    }
}
