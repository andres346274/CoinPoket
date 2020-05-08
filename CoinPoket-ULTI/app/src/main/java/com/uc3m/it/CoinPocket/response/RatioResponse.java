package com.uc3m.it.CoinPocket.response;

import java.util.Map;

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
