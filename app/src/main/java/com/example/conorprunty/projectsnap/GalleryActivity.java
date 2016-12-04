package com.example.conorprunty.projectsnap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GalleryActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Button getImage = (Button) findViewById(R.id.getImages);
        Button shareTheImage = (Button) findViewById(R.id.shareImage);


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

        shareTheImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //http://stackoverflow.com/questions/21084866/how-to-share-image-of-imageview/21095826
                ImageView imageBeingSent = (ImageView) findViewById(R.id.imageView);

                imageBeingSent.setDrawingCacheEnabled(true);

                Bitmap bitmap = imageBeingSent.getDrawingCache();
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
                try {
                    cachePath.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(cachePath);
                    //same as the camera activity, need to compress the image
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Intent iShare = new Intent(Intent.ACTION_SEND);
                iShare.setType("image/*");
                //iShare.putExtra(Intent.EXTRA_TEXT, "Hello");
                iShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
                //share the image chosen from gallery
                startActivity(Intent.createChooser(iShare, "Choose app to share photo with!"));
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
}
