package com.example.billboard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity4 extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap, receivedBitmap;

    private TextView scale, Dimensions;
    private double scale_pixels, length, breadth;
    private Button uploadButton;
    private String filePath;
    private String encodedImage;
    private ProgressBar loader;
    public static final String apiurl = "http://172.20.10.4/upload.php";

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
        filePath = getIntent().getStringExtra("imageFilePath");
        scale_pixels = getIntent().getDoubleExtra("scale", 0);
        length = getIntent().getDoubleExtra("length", 0);
        breadth = getIntent().getDoubleExtra("breadth", 0);
        // Load the Bitmap from the file
        receivedBitmap = BitmapFactory.decodeFile(filePath);
        encodebitmap(receivedBitmap);

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
                            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
                            //Toast.makeText(MainActivity4.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                            upload();
                            Intent i1 = new Intent(MainActivity4.this,MainActivity.class);
                            startActivityForResult(i1, 1);
                            Intent i2 = new Intent();
                            setResult(RESULT_OK,i1);
                            finish();

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
    private void upload(){

        Double height = breadth;
        Double width = length;
        int user_id = Integer.parseInt(DisplayBillboards.current_ID);
        String name = DisplayBillboards.user_name;
        String billboardName = DisplayBillboards.current_billboard;

        // Get the current date and time
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(date);
        StringRequest request = new StringRequest(Request.Method.POST, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error",error.toString());
            }
        }){
          protected Map<String,String> getParams() throws AuthFailureError{
              Map<String,String> map = new HashMap();
              map.put("user_id", String.valueOf(user_id));
              map.put("billboardName",billboardName);
              map.put("name",name);
              map.put("uplaod",encodedImage);
              map.put("height", String.valueOf(height));
              map.put("width", String.valueOf(width));
              map.put("Date", String.valueOf(date));
              return map;
          }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

    }
    private String getRelativePath(String absolutePath) {
        // Get the current working directory as a File object
        File currentWorkingDirectory = getExternalFilesDir(null);

        // Convert the absolute path to a File object
        File absoluteFilePath = new File(absolutePath);

        // Get the canonical paths of both File objects to handle symbolic links, etc.
        String currentWorkingDirectoryPath = currentWorkingDirectory.getAbsolutePath();
        String absoluteFilePathPath = absoluteFilePath.getAbsolutePath();

        // Get the relative path using String manipulation
        if (absoluteFilePathPath.startsWith(currentWorkingDirectoryPath)) {
            String relativePath = absoluteFilePathPath.substring(currentWorkingDirectoryPath.length());
            if (relativePath.startsWith(File.separator)) {
                relativePath = relativePath.substring(1); // Remove the leading separator if present
            }
            return relativePath;
        }

        // If the absolute path is not within the current working directory, return the absolute path itself
        return absolutePath;
    }

    private void encodebitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] byteofImages = byteArrayOutputStream.toByteArray();
        encodedImage = android.util.Base64.encodeToString(byteofImages, Base64.DEFAULT);
    }


    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}
