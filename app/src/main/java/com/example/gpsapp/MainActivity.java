package com.example.gpsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_CODE = 100;

    TextView txtLocation;
    Button btnGetLocation;

    FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        txtLocation = findViewById(R.id.txtLocation);
        btnGetLocation = findViewById(R.id.btnGetLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnGetLocation.setOnClickListener(v -> getLocation());
    }


    private void getLocation() {
        //Comprueba si el usuario ya concedió el permiso de ubicación
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //Solicitar permiso al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
            //el metodo que acabamos de llamar provoca el callback (onRequestPermissionsResult)


        } else {
            //Obtiene la última ubicación conocida
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            showLocation(location);
                        } else {
                            txtLocation.setText("No se pudo obtener la ubicación");
                        }
                    });
        }
    }

    private void showLocation(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        txtLocation.setText(
                "Latitud: " + lat + "\nLongitud: " + lon
        );
    }


    /*
        El método onRequestPermissionsResult es un callback (método de retorno)
          que Android llama automáticamente cuando el usuario responde a una solicitud
          de permisos en tiempo de ejecución.

        Es decir:

        Tú pides un permiso → Android muestra un diálogo →
        El usuario acepta o rechaza →
        Android llama a onRequestPermissionsResult para informarte del resultado.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this,
                        "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    

}