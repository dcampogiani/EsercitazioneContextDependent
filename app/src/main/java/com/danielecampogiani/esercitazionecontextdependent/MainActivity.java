package com.danielecampogiani.esercitazionecontextdependent;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    TextView label;
    private LocationClient locationClient;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        label = (TextView)findViewById(R.id.textLabel);
        label.setText(R.string.connecting);
        locationClient = new LocationClient(this, this, this);
        geocoder = new Geocoder(this, Locale.getDefault());
        locationClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        Location current = locationClient.getLastLocation();
        try {
            List<Address> streets = geocoder.getFromLocation(current.getLatitude(),current.getLongitude(),10);
            if (streets.size()>0){
                String addressString = "";
                Address address = streets.get(1);
                for (int i =0;i< address.getMaxAddressLineIndex();i++){
                    addressString+= " "+ address.getAddressLine(i);
                }

                if (addressString.contains("40123")){//Saragozza
                    label.setText(R.string.neear_home);
                }

                else {
                    label.setText("You are near "+addressString);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        }

    }
}
