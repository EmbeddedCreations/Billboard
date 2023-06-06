package com.example.billboard;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity3 extends AppCompatActivity {
    private FrameLayout fl_layout;
    private static final int STORAGE_PERMISSION = 1;
    private static final int GALLERY = 2;
    private static final int CAMERA_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 4;
    private String mCurrentPhotoPath;
    private Bitmap bitmap,bitmap_copy;
    private DrawingView drawing_view;
    private ImageView iv_imgView;
    private ImageButton rotateButton,Undo,imageButton,mImageCurrentImageBtn;
    private Button process;
    private Pair<Float, Float> coordinates;
    private double length_pixels,breadth_pixels,length,breadth,scale_length,scale;
    private ArrayList<Pair<Float, Float>> coordinatesList;
    public boolean marked = false,billboardCheck,scaleCheck;
    ProgressBar loadingProgressBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //Loading the Drawing View and Setting the brush size
        drawing_view = findViewById(R.id.drawing_view);
        drawing_view.setSizeForBrush(20f);

        fl_layout = findViewById(R.id.fl_layout);
        mImageCurrentImageBtn = (ImageButton)findViewById(R.id.imageButton);
        mImageCurrentImageBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed));

        iv_imgView = findViewById(R.id.iv_imgView);

        //For Rotating Of Image if Not Proper
        rotateButton = findViewById(R.id.rotate_button);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current bitmap from the ImageView
                Bitmap currentBitmap = ((BitmapDrawable) iv_imgView.getDrawable()).getBitmap();

                // Rotate the bitmap by 90 degrees
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentBitmap.getWidth(), currentBitmap.getHeight(), matrix, true);

                // Set the rotated bitmap to the ImageView
                iv_imgView.setImageBitmap(rotatedBitmap);
            }
        });

        //For Undo
        Undo = findViewById(R.id.Undo);
        Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawing_view.clickOnUndo();
            }
        });

        //For Getting Image From gallery
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStorageAllowed()) {
                    getCameraPermission();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                    builder.setTitle("Choose an option")
                            .setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            // Gallery option selected
                                            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pickPhotoIntent, GALLERY);
                                            break;
                                        case 1:
                                            String filename = "Billboard";
                                            File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                            try {
                                                File imageFile = File.createTempFile(filename,".jpg",StorageDir);
                                                mCurrentPhotoPath = imageFile.getAbsolutePath();
                                                Uri imageUri = FileProvider.getUriForFile(MainActivity3.this,"com.example.billboard.file-provider",imageFile);
                                                Intent myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                myIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                                startActivityForResult(myIntent,5);
                                            } catch (IOException e) {
                                                Log.d("ErrorForCam",e.toString());
                                                throw new RuntimeException(e);
                                            }
                                            //openCamera();
                                            break;
                                    }
                                }
                            })
                            .show();
                } else {
                    requestStoragePermission();
                }
            }
        });

        process = findViewById(R.id.process);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        coordinatesList = new ArrayList<>();
        //Process
        process.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //process.setVisibility(View.INVISIBLE);
                loadingProgressBar.setVisibility(View.VISIBLE);
                process.setEnabled(false);
                Intent i = new Intent(MainActivity3.this,MainActivity4.class);

                coordinates = drawing_view.coordinates;
                coordinatesList = drawing_view.coordinatesList;

                if(coordinatesList.size()<6){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                    builder.setMessage("You Have Marked Less Points Please Mark Again or Click On Help(?) Button for Help")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Code to be executed when the OK button is clicked
                                    // Add your code here
                                    drawing_view.clearPaths();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if(coordinatesList.size() > 6){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                    builder.setMessage("You Have Marked More Points than needed Please Mark Again or Click On Help(?) Button for Help")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Code to be executed when the OK button is clicked
                                    // Add your code here
                                    drawing_view.clearPaths();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if(coordinatesList.size() == 6){
                    marked = true;
                    drawing_view.setSizeForBrush(5f);
                    processBillboard(coordinatesList);
                    calculateScale(coordinatesList);
                    checkScale(coordinatesList);
                    checkBillboard(coordinatesList);

                }
                File file = new File(getExternalFilesDir(null), "image.png");
                Bitmap bitmap_new = getBitmapFromView(fl_layout);

                Log.d("Size",String.valueOf(coordinatesList.size()));
                Log.d("Coordinates", String.valueOf(coordinatesList));
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    bitmap_new.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i.putExtra("imageFilePath", file.getAbsolutePath());
                i.putExtra("length",length);
                i.putExtra("breadth",breadth);
                i.putExtra("scale",scale_length);
                if(marked && scaleCheck && billboardCheck){
                    startActivity(i);
                }
                // When the action is complete, hide the progress bar and enable the button
                loadingProgressBar.setVisibility(View.INVISIBLE);
                process.setEnabled(true);

            }
        });



    }
    private boolean checkBillboard(ArrayList<Pair<Float, Float>> coordinatesList){
        billboardCheck = true;
        Pair<Float,Float> P1 = coordinatesList.get(0);
        Pair<Float,Float> P2 = coordinatesList.get(1);
        Pair<Float,Float> P3 = coordinatesList.get(2);
        Pair<Float,Float> P4 = coordinatesList.get(3);
        if(Math.abs(P1.second - P2.second) >= 100
            || Math.abs(P2.first - P3.first) >= 100
            || Math.abs(P3.second - P4.second) >= 100
            || Math.abs(P1.first - P4.first) >= 100){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                builder.setMessage("Please Mark Appropriately or Take  photo of the Billboard Properly")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Code to be executed when the OK button is clicked
                                // Add your code here
                                drawing_view.clearPaths();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                billboardCheck =  false;
        }
        return billboardCheck;
    }
    private boolean checkScale(ArrayList<Pair<Float, Float>> coordinatesList){
        scaleCheck = true;
        Pair<Float,Float> P5 = coordinatesList.get(4);
        Pair<Float,Float> P6 = coordinatesList.get(5);
        if(Math.abs(P5.second - P6.second) <= 20 || Math.abs(P5.first - P6.first) <= 20){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
            builder.setMessage("Please Mark Scale Properly The Scale Should be displayed in the image Horizontally" +
                            "And Please Make Sure that Scale is marked After marking the Billboard")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Code to be executed when the OK button is clicked
                            // Add your code here
                            drawing_view.clearPaths();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
            scaleCheck =  false;
        }
        return scaleCheck;
    }


    

    //To Ask and check For Camera Permission
    private void getCameraPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_CODE);
        }else{
            //Do Nothing Relax
        }
    }
    //To Check if the Read Storage is Allowed for accessing the Gallery
    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    //To Request Permission from the User for gallery of his Phone
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                String.valueOf(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}))) {
            Toast.makeText(this, "NEED BACKGROUND IMAGE PERMISSION", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iv_imgView.setVisibility(View.VISIBLE);
        if (requestCode == GALLERY) {
            //
            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                int imageViewWidth = iv_imgView.getWidth();
                int imageViewHeight = iv_imgView.getHeight();

                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();

                float scaleWidth = (float) imageViewWidth / bitmapWidth;
                float scaleHeight = (float) imageViewHeight / bitmapHeight;

                float scaleFactor = Math.min(scaleWidth, scaleHeight);

                int newWidth = Math.round(bitmapWidth * scaleFactor);
                int newHeight = Math.round(bitmapHeight * scaleFactor);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                iv_imgView.setImageBitmap(resizedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(requestCode == 5){
            try {
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
//                int imageViewWidth = iv_imgView.getWidth();
//                int imageViewHeight = iv_imgView.getHeight();
//
//                int bitmapWidth = bitmap.getWidth();
//                int bitmapHeight = bitmap.getHeight();
//                float scaleWidth = (float) imageViewWidth / bitmapWidth;
//                float scaleHeight = (float) imageViewHeight / bitmapHeight;
//
//                float scaleFactor = Math.min(scaleWidth, scaleHeight);
//
//                int newWidth = Math.round(bitmapWidth * scaleFactor);
//                int newHeight = Math.round(bitmapHeight * scaleFactor);
//                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
//                iv_imgView.setImageBitmap(resizedBitmap);
                iv_imgView.setImageBitmap(bitmap);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //Creating a function to extract bitmap out of the Drawing view for passing to another Activity
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
    //Converting Bitmap to Byte Array
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public void toProcess(View view){
        Intent i = new Intent(MainActivity3.this,MainActivity4.class);
        i.putExtra("imageBytes", bitmapToByteArray(getBitmapFromView(fl_layout)));
        startActivity(i);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "PERMISSION Denied ", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == CAMERA_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "PERMISSION Denied ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void paintClicked(View view) {
        if (view != mImageCurrentImageBtn) {
            ImageButton imageButton = (ImageButton) view;

            String colorTag = imageButton.getTag().toString();
            drawing_view.setColor(colorTag);
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed));
            mImageCurrentImageBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet));
            mImageCurrentImageBtn = imageButton;
        }
    }

    //Method to calculate distance between two points
    public double calculateDist(double x1,double x2,double y1,double y2){
        double deltaX = x2-x1;
        double deltaY = y2-y1;
        double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        return distance;
    }
    public void processBillboard(ArrayList<Pair<Float,Float>> coordinatesList){
        Pair<Float,Float> P1 = coordinatesList.get(0);
        Pair<Float,Float> P2 = coordinatesList.get(1);
        Pair<Float,Float> P3 = coordinatesList.get(2);
        Pair<Float,Float> P4 = coordinatesList.get(3);
        length_pixels = calculateDist(P1.first, P2.first,P1.second, P2.second);
        breadth_pixels = calculateDist(P2.first, P3.first,P2.second, P3.second);
        Log.d("Measurement","length_pixels"+Double.toString(length_pixels));
        Log.d("Measurement","breadth_pixels"+Double.toString(breadth_pixels));
        drawing_view.drawLine(P1.first,P1.second,P2.first, P2.second);
        drawing_view.drawLine(P2.first,P2.second,P3.first, P3.second);
        drawing_view.drawLine(P3.first,P3.second,P4.first, P4.second);
        drawing_view.drawLine(P4.first,P4.second,P1.first, P1.second);

    }
    public void calculateScale(ArrayList<Pair<Float,Float>> coordinatesList){
        Pair<Float,Float> P5 = coordinatesList.get(4);
        Pair<Float,Float> P6 = coordinatesList.get(5);
        scale_length = calculateDist(P5.first, P6.first,P5.second, P6.second);
        scale = 15.3/scale_length;
        length = length_pixels*scale;
        breadth = breadth_pixels*scale;
        Log.d("Measurement","scale_length"+Double.toString(scale_length));
        Log.d("Measurement","scale"+Double.toString(scale));
        Log.d("Measurement","length"+Double.toString(length));
        Log.d("Measurement","breadth"+Double.toString(breadth));
        drawing_view.drawLine(P5.first,P5.second,P6.first, P6.second);
    }


}
