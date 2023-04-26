package com.example.sequence2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
{
    private FloatingActionButton buttonSearch;
    private FloatingActionButton buttonLocate;
    private ImageButton buttonNext;
    private ImageButton buttonPrev;
    private ImageView imageWeather;
    private TextView textWeatherDescription;
    private TextView textDate;
    private EditText textSearch;
    private TextView textCooRight;
    private TextView textCooLeft;

    private IForecastProvider forecast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des attributs
        this.buttonSearch = findViewById(R.id.buttonSearch);
        this.buttonLocate = findViewById(R.id.buttonLocate);
        this.buttonNext = findViewById(R.id.buttonNext);
        this.buttonPrev = findViewById(R.id.buttonPrev);
        this.imageWeather = findViewById(R.id.imageWeather);
        this.textWeatherDescription = findViewById(R.id.textWeatherDesc);
        this.textDate = findViewById(R.id.textDate);
        this.textSearch = findViewById(R.id.textSearch);
        this.textCooRight = findViewById(R.id.textCooRight);
        this.textCooLeft = findViewById(R.id.textCooLeft);
        this.forecast = new Forecast();

        // Création d'un listener sur recherche
        this.buttonSearch.setOnClickListener(this::SearchCity);

    }

    /**
     * Permet de chercher une ville
     * @param view
     */
    private void SearchCity(android.view.View view){
        Location l = new Location();
        l.setCity(this.textSearch.getText().toString());
        l.setLatitude(47.311f);
        l.setLongitude(5.069f);
        showLocation(l);
        showWeather(this.forecast.getForecast(l).getForecast(0));
    }

    /**
     * Permet d'affiche un weather
     * @param weather weather à afficher
     */
    private void showWeather(Weather weather)
    {
        String desc = "Température : " + weather.getTemperature()
                +"°C\nHumidité : " + weather.getHumidity() + "%\nVitesse du vent :"
        + weather.getWindSpeed() + "km/h\nDirection du vent : " + weather.getWindDirection() + "\n" +
                "Précipitations : " + weather.getPrecipitation() + "mm";
        this.textWeatherDescription.setText(desc);
    }

    /**
     * Permet d'afficher une location
     * @param location locaiton à afficher
     */
    private void showLocation(Location location)
    {
        this.textCooLeft.setText(GeoLocFormat.longitudeDMS(location.getLongitude()));
        this.textCooRight.setText(GeoLocFormat.latitudeDMS(location.getLatitude()));
    }
}