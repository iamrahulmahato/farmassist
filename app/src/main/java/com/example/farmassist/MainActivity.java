package com.example.farmassist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.farmassist.models.WeatherData;
import com.example.farmassist.services.WeatherService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private MaterialTextView textWeatherResult, textWeatherDesc;
    private MaterialCardView cardWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton buttonWeather = findViewById(R.id.buttonWeather);
        MaterialButton buttonLocationWeather = findViewById(R.id.buttonLocationWeather);
        MaterialButton buttonSchemes = findViewById(R.id.buttonSchemes);
        cardWeather = findViewById(R.id.cardWeather);
        textWeatherResult = findViewById(R.id.textWeatherResult);
        textWeatherDesc = findViewById(R.id.textWeatherDesc);
        TextInputEditText editTextCity = findViewById(R.id.editTextCity);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        buttonWeather.setOnClickListener(v -> {
            String city = editTextCity.getText() != null ? editTextCity.getText().toString().trim() : "";
            if (city.isEmpty()) {
                Toast.makeText(this, R.string.enter_city_name, Toast.LENGTH_SHORT).show();
                return;
            }
            cardWeather.setVisibility(View.VISIBLE);
            textWeatherResult.setText(R.string.weather_loading);
            textWeatherDesc.setText("");

            new Thread(() -> {
                try {
                    WeatherService weatherService = new WeatherService();
                    WeatherData data = weatherService.getWeatherByCity(city);
                    runOnUiThread(() -> {
                        textWeatherResult.setText(getString(R.string.weather_result,
                                data.getLocation(), data.getTemperature()));
                        textWeatherDesc.setText(data.getConditions());
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        textWeatherResult.setText(getString(R.string.weather_error_city, city));
                        textWeatherDesc.setText("");
                    });
                }
            }).start();
        });

        buttonLocationWeather.setOnClickListener(v -> {
            if (checkLocationPermission()) {
                getWeatherByLocation();
            } else {
                requestLocationPermission();
            }
        });

        buttonSchemes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SchemesListActivity.class));
        });
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getWeatherByLocation() {
        cardWeather.setVisibility(View.VISIBLE);
        textWeatherResult.setText(R.string.weather_location_loading);
        textWeatherDesc.setText("");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        fetchWeatherForLocation(location);
                    } else {
                        textWeatherResult.setText(R.string.location_error);
                    }
                })
                .addOnFailureListener(e -> textWeatherResult.setText(R.string.location_error));
    }

    private void fetchWeatherForLocation(Location location) {
        textWeatherResult.setText(R.string.weather_loading);
        textWeatherDesc.setText("");
        new Thread(() -> {
            try {
                WeatherService weatherService = new WeatherService();
                WeatherData data = weatherService.getWeatherByLocation(location.getLatitude(), location.getLongitude());
                runOnUiThread(() -> {
                    textWeatherResult.setText(getString(R.string.weather_result,
                            data.getLocation(), data.getTemperature()));
                    textWeatherDesc.setText(data.getConditions());
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    textWeatherResult.setText(R.string.weather_error_location);
                    textWeatherDesc.setText("");
                });
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherByLocation();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
