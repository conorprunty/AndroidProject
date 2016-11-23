package com.example.conorprunty.projectsnap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private ImageView ivThummbnail;
    String xCoord = "1234.8W";
    String yCoord = "-43543T";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button bCamera = (Button) findViewById(R.id.takePicture);
        ivThummbnail = (ImageView) findViewById(R.id.imageDisplay);
        bCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        Button bSendCoords = (Button) findViewById(R.id.coordButton);

        bSendCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //http://stackoverflow.com/questions/21084866/how-to-share-image-of-imageview/21095826
                ImageView imageBeingSent = (ImageView) findViewById(R.id.imageDisplay);

                imageBeingSent.setDrawingCacheEnabled(true);

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


                Intent iShare = new Intent(Intent.ACTION_SEND);
                iShare.setType("image/*");
                iShare.putExtra(Intent.EXTRA_TEXT, "Hey, I took this great picture at "+xCoord+" "+yCoord);
                iShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                startActivity(Intent.createChooser(iShare, "Choose app to share photo with!"));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivThummbnail.setImageBitmap(imageBitmap);
            MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "projectSnapImage!" , "Sent from Project Snap");
        }
    }

    private void cameraIntentCode() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
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
}
