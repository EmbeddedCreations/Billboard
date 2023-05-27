package com.example.billboard;

import android.content.DialogInterface;
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
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity4 extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap,receivedBitmap;
    private Mat mat,bgr;
    private TextView result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        imageView = findViewById(R.id.image_view);
        result = findViewById(R.id.result);
        // Retrieve the file path from the intent
        String filePath = getIntent().getStringExtra("imageFilePath");

        // Load the Bitmap from the file
        receivedBitmap = BitmapFactory.decodeFile(filePath);
        if(OpenCVLoader.initDebug()) Log.d("Loaded","Success");
        else Log.d("Error","Error");
        if (receivedBitmap != null) {

            // Use the receivedBitmap as needed

            imageView = findViewById(R.id.image_view);
            imageView.setImageBitmap(receivedBitmap);
            mat = new Mat();
            Utils.bitmapToMat(receivedBitmap , mat);
            processImage();

        } else {
            // Handle the case when the intent extra is null
            // For example, display an error message or perform an alternative action
        Log.d("Error_Image","Error");
        }



    }
    public void DoProcess(View view){
        bitmap = getBitmapFromView(imageView);




    }

    private void processImage() {
        Mat hsv = new Mat();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);

// Define the lower and upper HSV color range values for red
        Scalar lowerRed1 = new Scalar(0, 100, 100); // Lower HSV values for red (range 1)
        Scalar upperRed1 = new Scalar(10, 255, 255); // Upper HSV values for red (range 1)
        Scalar lowerRed2 = new Scalar(170, 100, 100); // Lower HSV values for red (range 2)
        Scalar upperRed2 = new Scalar(180, 255, 255); // Upper HSV values for red (range 2)

// Apply color thresholding to obtain a binary mask for red color
        Mat mask1 = new Mat();
        Core.inRange(hsv, lowerRed1, upperRed1, mask1);

        Mat mask2 = new Mat();
        Core.inRange(hsv, lowerRed2, upperRed2, mask2);

        Mat redMask = new Mat();
        Core.bitwise_or(mask1, mask2, redMask);

// Perform morphological operations to enhance the red mask
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(redMask, redMask, Imgproc.MORPH_CLOSE, kernel);

// Find contours in the binary mask
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(redMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 0;
        MatOfPoint maxContour = null;

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                maxContour = contour;
            }
        }

        if (maxContour != null) {
            Rect boundingRect = Imgproc.boundingRect(maxContour);
            Imgproc.rectangle(mat, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 0), 2);

            int width = boundingRect.width;
            int height = boundingRect.height;
            result.setText("Red Strip Dimensions: " + width + " x " + height + " pixels");
        } else {
            result.setText("No red strip found.");
        }


        Utils.matToBitmap(mat, receivedBitmap);
        imageView.setImageBitmap(receivedBitmap);
    }

    //Process Billboard



    //To Get Bitmap from View
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return returnedBitmap;
    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity4.this);

        builder.setTitle("You Want to go Back")
                .setMessage("Are you sure ?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity4.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel",null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
