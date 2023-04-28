package com.example.sequence2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenMeteoProvider implements IForecastProvider
{
    @Override
    public WeatherForecast getForecast(Location location) {
        WeatherForecast forecast = new WeatherForecast();
        try{

            URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude="+location.getLatitude()+"&longitude="+location.getLongitude()+"&hourly=temperature_2m,relativehumidity_2m,precipitation_probability,weathercode,windspeed_10m,winddirection_10m");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String s = r.readLine();

            // On récupère les différents attributs
            JSONObject array = new JSONObject(s);
            JSONObject obj = array.getJSONObject("hourly");
            JSONArray listDates = (JSONArray) obj.get("time");
            JSONArray listTemperature = (JSONArray) obj.get("temperature_2m");
            JSONArray listHumidite = (JSONArray) obj.get("relativehumidity_2m");
            JSONArray listWeatherCode = (JSONArray) obj.get("weathercode");
            JSONArray listWindSpeed = (JSONArray) obj.get("windspeed_10m");
            JSONArray listWindDirection = (JSONArray) obj.get("winddirection_10m");
            JSONArray listPrecipitation = (JSONArray) obj.get("precipitation_probability");


            for(int i =0; i< listDates.length(); i++){
                Weather w = new Weather();
                w.setDay(Integer.parseInt(listDates.get(i).toString().split("T")[0].split("-")[2]));
                w.setHour(Integer.parseInt(listDates.get(i).toString().split("T")[1].substring(0,2)));
                w.setHumidity(listHumidite.getInt(i));
                w.setTemperature(listTemperature.getInt(i));
                w.setPrecipitation((float)listPrecipitation.getDouble(i));
                int code = (int)listWeatherCode.get(i);
                if (code > 6)
                {
                    code = 0;
                }
                w.setWeatherCode(WeatherCodes.values()[code]);
                w.setWindSpeed(listWindSpeed.getInt(i));
                w.setWindDirection(listWindDirection.getString(i));
                forecast.addForecast(w);
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return forecast;
    }
}
