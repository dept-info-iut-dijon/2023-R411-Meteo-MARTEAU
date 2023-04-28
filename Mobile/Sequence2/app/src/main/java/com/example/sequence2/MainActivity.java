package com.example.sequence2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

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

    private TextView textTemp;
    private TextView textHumidity;
    private TextView textWindDirection;
    private TextView textWindSpeed;
    private TextView textPrecipitations;


    private IForecastProvider forecast;
    private LocalDateTime time;
    private Location locationSelected = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        this.textHumidity = findViewById(R.id.textHumidity);
        this.textWindSpeed = findViewById(R.id.textWinSpeed);
        this.textTemp = findViewById(R.id.textTemp3);
        this.textWindDirection = findViewById(R.id.textwindDirection);
        this.textPrecipitations = findViewById(R.id.textPrecipitation);
        this.textDate = findViewById(R.id.textDate);
        this.textSearch = findViewById(R.id.textSearch);
        this.textCooRight = findViewById(R.id.textCooRight);
        this.textCooLeft = findViewById(R.id.textCooLeft);
        this.forecast = new Forecast();
        this.time = LocalDateTime.now().atZone(ZoneId.of("Europe/Berlin")).toLocalDateTime();
        this.textDate.setText(realDay(time.getDayOfMonth(),time.getHour()));

        // Création d'un listener sur recherche
        this.buttonSearch.setOnClickListener(this::SearchCity);
        // Création d'un listener sur la location
        this.buttonLocate.setOnClickListener(this::LocationMyself);
        // Création de listener sur le changement de jour
        this.buttonNext.setOnClickListener(this::nextDay);
        this.buttonPrev.setOnClickListener(this::prevDay);
    }

    /**
     * Permet de changer la date du jour
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void nextDay(android.view.View view)
    {
        this.time = this.time.plusHours(1);
        this.textDate.setText(realDay(time.getDayOfMonth(),time.getHour()));

        // Si une position est choisi alors on montre la météo
        if (this.locationSelected != null){
            // Affiche la météo
            OpenMeteoProvider meteo = new OpenMeteoProvider();
            new Thread( ()->{
                WeatherForecast forecast1 = meteo.getForecast(locationSelected);
                Weather w = forecast1.getForecast(this.time.getHour());
                runOnUiThread( ()->{
                    showWeather(w);
                });
            }).start();
        }

    }

    /**
     * Permet de changer la date du jour
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void prevDay(android.view.View view)
    {
        this.time = this.time.minusHours(1);
        this.textDate.setText(realDay(time.getDayOfMonth(),time.getHour()));

        if (this.locationSelected != null){
            // Affiche la météo
            OpenMeteoProvider meteo = new OpenMeteoProvider();
            new Thread( ()->{
                WeatherForecast forecast1 = meteo.getForecast(locationSelected);
                Weather w = forecast1.getForecast(this.time.getHour());
                runOnUiThread( ()->{
                    showWeather(w);
                });
            }).start();
        }

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
        this.textTemp.setText(getResources().getString(R.string.temperature) + " "  + weather.getTemperature() + " °C");
        this.textHumidity.setText(getResources().getString(R.string.humidity) + " " + weather.getHumidity() + " %");
        this.textPrecipitations.setText(getResources().getString(R.string.precipitation) + "  : " + weather.getPrecipitation() + " mm");
        this.textWindDirection.setText(getResources().getString(R.string.windDirection) + " " + weather.getWindDirection());
        this.textWindSpeed.setText(getResources().getString(R.string.windSpeed) + " " + weather.getWindSpeed() + " km/h");

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull android.location.Location location)
    {
        // Récupération de la location
        this.locationSelected = new Location();
        locationSelected.setLongitude((float)location.getLongitude());
        locationSelected.setLatitude((float)location.getLatitude());

        // Affiche la location
        showLocation(locationSelected);

        // Affiche la météo
        OpenMeteoProvider meteo = new OpenMeteoProvider();
        new Thread( ()->{
            WeatherForecast forecast1 = meteo.getForecast(locationSelected);
            Weather w = forecast1.getForecast(this.time.getHour());
            runOnUiThread( ()->{
                showWeather(w);
            });
        }).start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        initLocation();
    }

    private String realDay(int day, int hour){
        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL,
                getResources().getConfiguration().locale);
        c.setTime(new Date());
        c.set(Calendar.DATE, day);
        c.set(Calendar.HOUR_OF_DAY,hour);
        return df.format(c.getTime())+String.format(" - %02d h",
                c.get(Calendar.HOUR_OF_DAY));
    }


    @SuppressLint("MissingPermission")
    private void initLocation(){
        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
    }
}