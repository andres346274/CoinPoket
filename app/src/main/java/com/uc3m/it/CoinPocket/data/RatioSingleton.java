package com.uc3m.it.CoinPocket.data;

import com.uc3m.it.CoinPocket.response.Ratio;
import com.uc3m.it.CoinPocket.response.RatioResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *singleton para conversión moneda
 */
public class RatioSingleton {

    private static RatioSingleton INSTANCE;
    public RatioResponse ratio;

    private RatioSingleton() {

    }

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

    public static RatioSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RatioSingleton();
        }
        return INSTANCE;
    }
}
