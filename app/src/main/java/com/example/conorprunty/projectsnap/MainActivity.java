package com.example.conorprunty.projectsnap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button to open camera page
        Button goToCamera = (Button) findViewById(R.id.cameraButton);
        goToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this starts the intent to open another page
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
        //button to open gallery page
        Button goToGallery = (Button) findViewById(R.id.galleryButton);
        goToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts intent to open another page
                Intent i = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(i);
            }
        });
    }
}
