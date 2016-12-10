package com.example.conorprunty.projectsnap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private ImageView ivThummbnail;
    String xCoord = "53.3506500";
    String yCoord = "-6.2536200";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //starts the connection to the Google API for getting the location services
            if (mGoogleApiClient == null) {
                //this is connecting successfully
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 6000)        // 60 seconds
                    .setFastestInterval(1 * 1000); // 1 second
            //checks if not empty before connecting
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Unable to get permissions for location services!", Toast.LENGTH_LONG).show();
        }

        Button bCamera = (Button) findViewById(R.id.takePicture);
        ivThummbnail = (ImageView) findViewById(R.id.imageDisplay);
        //on click method to take photo
        bCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        Button bSendCoords = (Button) findViewById(R.id.coordButton);
        //this sends the image via a share intent
        bSendCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://stackoverflow.com/questions/21084866/how-to-share-image-of-imageview/21095826
                ImageView imageBeingSent = (ImageView) findViewById(R.id.imageDisplay);
                imageBeingSent.setDrawingCacheEnabled(true);
                //need to compress the image first, and use the images path to send it
                Bitmap bitmap = imageBeingSent.getDrawingCache();
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                try {
                    cachePath.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(cachePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //start the intent for sharing the image
                Intent iShare = new Intent(Intent.ACTION_SEND);
                //set the type to only find images, nothing else
                iShare.setType("image/*");
                //send the text coordinates of the users location along with the image
                iShare.putExtra(Intent.EXTRA_TEXT, "Hey, I took this great picture at " + xCoord + " " + yCoord);
                iShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                startActivity(Intent.createChooser(iShare, "Choose app to share photo with!"));
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //test to make sure its connected
        Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
        //get last location
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if ((location == null) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            myLocation = location;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //set the image on the image view
            ivThummbnail.setImageBitmap(imageBitmap);
            MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "projectSnapImage!", "Sent from Project Snap");
        }
    }

    private void cameraIntentCode() {
        //permission granted by user so this method is called
        //used to take the picture itself via intents
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takePicture() {
        //check permissions for picture
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            //permission ok - call the camera intent method
            cameraIntentCode();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    cameraIntentCode();
                } else {
                    // permission deniedDisable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        Toast.makeText(getApplicationContext(), "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }
}