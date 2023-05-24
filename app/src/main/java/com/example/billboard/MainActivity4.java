package com.example.billboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

public class MainActivity4 extends AppCompatActivity {
    private ImageView imageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Retrieve the file path from the intent
        String filePath = getIntent().getStringExtra("imageFilePath");

        // Load the Bitmap from the file
        Bitmap receivedBitmap = BitmapFactory.decodeFile(filePath);

        if (receivedBitmap != null) {

            // Use the receivedBitmap as needed
//            imageView = findViewById(R.id.imageview);
//            imageView.setImageBitmap(receivedBitmap);
        } else {
            // Handle the case when the intent extra is null
            // For example, display an error message or perform an alternative action
        Log.d("Error_Image","Error");
        }


        if(OpenCVLoader.initDebug()) Log.d("Loaded","Success");
        else Log.d("Error","Error");
    }
}
