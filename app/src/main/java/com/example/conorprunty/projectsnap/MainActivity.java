package com.example.conorprunty.projectsnap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instantiate database class
        DBHandler db = new DBHandler(this);
        //add a board
        db.addRating(new Rating(3, "5.0", "5.0"));

        final Rating highestRating = db.getRating(3);
        TextView ratingView = (TextView) findViewById(R.id.highestRating);
        //print the rating
        ratingView.setText("Highest photo rating this week is: " + highestRating.getTotal() + "!");
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
