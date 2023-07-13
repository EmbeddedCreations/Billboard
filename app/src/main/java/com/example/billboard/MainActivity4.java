package com.example.billboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity4 extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap, receivedBitmap;
    private TextView scale, Dimensions;
    private double scale_pixels, length, breadth;
    private Button uploadButton;
    private ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        imageView = findViewById(R.id.image_view);
        scale = findViewById(R.id.scale);
        Dimensions = findViewById(R.id.Dimensions);
        uploadButton = findViewById(R.id.upload_button);
        loader = findViewById(R.id.loader);

        // Retrieve the file path from the intent
        String filePath = getIntent().getStringExtra("imageFilePath");
        scale_pixels = getIntent().getDoubleExtra("scale", 0);
        length = getIntent().getDoubleExtra("length", 0);
        breadth = getIntent().getDoubleExtra("breadth", 0);
        // Load the Bitmap from the file
        receivedBitmap = BitmapFactory.decodeFile(filePath);

        if (receivedBitmap != null) {
            // Use the receivedBitmap as needed
            imageView.setImageBitmap(receivedBitmap);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            scale.setText("Scale: 15cm = " + decimalFormat.format(scale_pixels) + " Pixels");
            Dimensions.setText("Dimensions(cm): " + decimalFormat.format(length) + " X " + decimalFormat.format(breadth));

            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show the loader
                    loader.setVisibility(View.VISIBLE);

                    // Perform upload to cloud functionality here (Simulating with delay)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Hide the loader
                            loader.setVisibility(View.GONE);

                            // Display "Uploaded successfully" message
                            Toast.makeText(MainActivity4.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000); // Delay of 2000 milliseconds (2 seconds)
                }
            });

        } else {
            // Handle the case when the intent extra is null
            // For example, display an error message or perform an alternative action
            Log.d("Error_Image", "Error");
        }

    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}
