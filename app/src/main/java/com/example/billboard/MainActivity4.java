package com.example.billboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap,receivedBitmap;
    private Mat mat,bgr;
    private TextView scale,Dimensions;
    private double scale_pixels,length,breadth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        imageView = findViewById(R.id.image_view);
        scale = findViewById(R.id.scale);
        Dimensions = findViewById(R.id.Dimensions);

        // Retrieve the file path from the intent
        String filePath = getIntent().getStringExtra("imageFilePath");
        scale_pixels = getIntent().getDoubleExtra("scale",0);
        length = getIntent().getDoubleExtra("length",0);
        breadth = getIntent().getDoubleExtra("breadth",0);
        // Load the Bitmap from the file
        receivedBitmap = BitmapFactory.decodeFile(filePath);
        if(OpenCVLoader.initDebug()) Log.d("Loaded","Success");
        else Log.d("Error","Error");
        if (receivedBitmap != null) {

            // Use the receivedBitmap as needed

            imageView = findViewById(R.id.image_view);
            imageView.setImageBitmap(receivedBitmap);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            scale.setText("Scale: 15cm = "+ decimalFormat.format(scale_pixels)+ "Pixels");
            Dimensions.setText("Dimensions(cm): " + decimalFormat.format(length)+" X "+decimalFormat.format(breadth));


        } else {
            // Handle the case when the intent extra is null
            // For example, display an error message or perform an alternative action
        Log.d("Error_Image","Error");
        }



    }



    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity4.this);

        builder.setTitle("You Want to go Back")
                .setMessage("Are you sure ?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        MainActivity4.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel",null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
