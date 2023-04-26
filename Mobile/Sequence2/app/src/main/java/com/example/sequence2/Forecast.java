package com.example.sequence2;

public class Forecast implements IForecastProvider{
    @Override
    public WeatherForecast getForecast(Location location) {
        WeatherForecast w = new WeatherForecast();
        // Création des weathers
        Weather weather1 = new Weather();
        weather1.setDay(0);
        weather1.setHour(22);
        weather1.setHumidity(2);
        weather1.setPrecipitation(29);
        weather1.setTemperature(34);
        weather1.setWindDirection("N");
        weather1.setWindSpeed(5);
        weather1.setWeatherCode(WeatherCodes.CLEAR_SKY);

        Weather weather2 = new Weather();
        weather1.setDay(0);
        weather1.setHour(23);
        weather1.setHumidity(2);
        weather1.setPrecipitation(9);
        weather1.setTemperature(13);
        weather1.setWindDirection("E");
        weather1.setWindSpeed(50);
        weather1.setWeatherCode(WeatherCodes.SMALL_RAIN);

        Weather weather3 = new Weather();
        weather1.setDay(1);
        weather1.setHour(0);
        weather1.setHumidity(20);
        weather1.setPrecipitation(129);
        weather1.setTemperature(3);
        weather1.setWindDirection("NE");
        weather1.setWindSpeed(19);
        weather1.setWeatherCode(WeatherCodes.SNOW);

        Weather weather4 = new Weather();
        weather1.setDay(1);
        weather1.setHour(1);
        weather1.setHumidity(2);
        weather1.setPrecipitation(2);
        weather1.setTemperature(-10);
        weather1.setWindDirection("W");
        weather1.setWindSpeed(5);
        weather1.setWeatherCode(WeatherCodes.THUNDERSTORM);

        w.addForecast(weather1);
        w.addForecast(weather2);
        w.addForecast(weather3);
        w.addForecast(weather4);
        return w;
    }
}
