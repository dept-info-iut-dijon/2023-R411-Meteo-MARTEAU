package com.example.sequence2;

import static com.example.sequence2.WeatherCodes.CLEAR_SKY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements LocationListener
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
        // Création d'un listener sur la location
        this.buttonLocate.setOnClickListener(this::LocationMyself);

    }

    /**
     * Permet de se localiser
     * @param view
     */
    private void LocationMyself(android.view.View view)
    {
        int ret = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if( ret == PackageManager.PERMISSION_GRANTED) {
            initLocation();
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},100);
        }
    }

    /**
     * Permet de chercher une ville
     * @param view
     */
    private void SearchCity(android.view.View view){
        Location l = new Location();
        l.setCity(this.textSearch.getText().toString());
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

        WeatherCodes code = weather.getWeatherCode();
        switch (code) {
            case CLEAR_SKY:
                this.imageWeather.setImageResource(R.drawable.sunny);
                break;
            case FOGGY_CLOUDED:
                this.imageWeather.setImageResource(R.drawable.cloudy);
                break;
            case SMALL_RAIN:
                this.imageWeather.setImageResource(R.drawable.small_rain);
                break;
            case SNOW:
                this.imageWeather.setImageResource(R.drawable.snow);
                break;
            case THUNDERSTORM:
                this.imageWeather.setImageResource(R.drawable.thunder);
                break;
            case HEAVY_RAIN:
                this.imageWeather.setImageResource(R.drawable.rain);
                break;
            case PARTIAL_CLOUDED:
                this.imageWeather.setImageResource(R.drawable.partial_clouded);
                break;
            default:
                this.imageWeather.setImageDrawable(null);
        }
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

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        this.textCooLeft.setText(GeoLocFormat.longitudeDMS(location.getLongitude()));
        this.textCooRight.setText(GeoLocFormat.latitudeDMS(location.getLatitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        initLocation();
    }

    @SuppressLint("MissingPermission")
    private void initLocation(){
        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
    }
}