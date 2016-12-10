package com.example.conorprunty.projectsnap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button myRater = (Button) findViewById(R.id.rate);

        //http://abhiandroid.com/ui/ratingbar
        myRater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalStars = "" + simpleRatingBar.getNumStars();
                String rating = "" + simpleRatingBar.getRating();
                addToDB(rating);
                Toast.makeText(getApplicationContext(), "Rated "+rating+" out of "+totalStars+"!", Toast.LENGTH_SHORT).show();
            }
        });

        Button getImage = (Button) findViewById(R.id.getImages);
        //Button shareTheImage = (Button) findViewById(R.id.shareImage);
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                //implicit intent
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //allows user to get image from their device
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


    }

    //http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                //setting the image on the page
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addToDB(String rating){
        TextView ratingChosen = (TextView) findViewById(R.id.ratingChosen);
        ratingChosen.setText("Rated " +rating+ "!");

    }

}
