package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView altitudeTextView;
    TextView addressTextView;
    TextView accuracyTextView;
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
    public void viewInMap(View view){
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitudeTextView = (TextView)findViewById(R.id.latitudeTextView);
        longitudeTextView= (TextView)findViewById(R.id.longitudeTextView);
        altitudeTextView = (TextView)findViewById(R.id.altitudeTextView);
        addressTextView = (TextView)findViewById(R.id.addressTextView);
        accuracyTextView = (TextView)findViewById(R.id.accuracyTextView);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitudeTextView.setText("Latitude: "+String.valueOf(Math.round(location.getLatitude()*100.0)/100.0));
                longitudeTextView.setText("Longitude: "+String.valueOf(Math.round(location.getLongitude()*100.0)/100.0));
                altitudeTextView.setText("Altitude: "+String.valueOf(location.getAltitude()));
                accuracyTextView.setText("Accuracy: "+String.valueOf(location.getAccuracy()));
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (listAddresses != null && listAddresses.size() > 0) {
                        String address = "";
                        if (listAddresses.get(0).getFeatureName() != null) {
                            address += listAddresses.get(0).getFeatureName() + ", ";
                        }
                        if (listAddresses.get(0).getSubAdminArea() != null) ;
                        {
                            address += listAddresses.get(0).getSubAdminArea() + ", ";
                        }
                        if (listAddresses.get(0).getAdminArea() != null) {
                            address += listAddresses.get(0).getAdminArea();
                        }
                      addressTextView.setText("Address: "+address);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();

                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1); //asking permission from user to access user's location
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); //setting up GPS Location if the user gives permission
        }
    }
}
