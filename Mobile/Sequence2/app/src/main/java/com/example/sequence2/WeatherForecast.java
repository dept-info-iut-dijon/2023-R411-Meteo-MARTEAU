package com.example.sequence2;

import java.util.ArrayList;

/**
 * Composes forecast on several hours/days
 */
public class WeatherForecast {
    private ArrayList<Weather> datas;

    public WeatherForecast(){
        datas = new ArrayList<>();
    }

    /**
     * Add a forecast at the list
     * @param w the forecast to add
     */
    public void addForecast(Weather w){
        datas.add(w);
    }

    /**
     * Get a forecast
     * @param i the position of forecast (0=first)
     * @return the weather
     */
    public Weather getForecast(int i){
        return datas.get(i);
    }

    /**
     * Gets the size (number of weather)
     * @return the size
     */
    public int getSize(){
        return datas.size();
    }
}
