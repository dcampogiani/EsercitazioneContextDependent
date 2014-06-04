package com.danielecampogiani.esercitazionecontextdependent;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    TextView labelLocation;
    TextView labelSong;
    ImageView imageView;
    Geocoder geocoder;
    MediaPlayer mediaPlayer;
    Button playPauseButton;
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        labelLocation = (TextView) findViewById(R.id.locationLabel);
        imageView = (ImageView) findViewById(R.id.imageView);
        labelSong = (TextView) findViewById(R.id.song_details);
        labelLocation.setText(R.string.connecting);
        locationClient = new LocationClient(this, this, this);
        geocoder = new Geocoder(this, Locale.getDefault());
        mediaPlayer = new MediaPlayer();
        playPauseButton = (Button) findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setText(R.string.play_button_text);
                } else {
                    mediaPlayer.start();
                    playPauseButton.setText(R.string.pause_button_text);
                }
            }
        });
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
                    Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.quentin);
                    imageView.setImageURI(imageUri);
                    labelLocation.setText(R.string.neear_home);
                    labelSong.setText("I wanna be - Quentin Hannappe");
                    Uri songUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.quentin_hannappe_i_wanna_be);
                    mediaPlayer.setDataSource(this, songUri);
                    mediaPlayer.prepare();
                    playPauseButton.setText(R.string.pause_button_text);
                    mediaPlayer.start();
                }

                else {
                    Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.tamara);
                    imageView.setImageURI(imageUri);
                    labelLocation.setText("You are near " + addressString);
                    labelSong.setText("Tamara Laurel - Sweet");
                    Uri songUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tamara_laurel_sweet);
                    mediaPlayer.setDataSource(this, songUri);
                    mediaPlayer.prepare();
                    playPauseButton.setText(R.string.pause_button_text);
                    mediaPlayer.start();
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
