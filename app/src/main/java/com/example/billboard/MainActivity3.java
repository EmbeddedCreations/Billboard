package com.example.billboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity3 extends AppCompatActivity {
    private FrameLayout fl_layout;
    private static final int STORAGE_PERMISSION = 1;
    private static final int GALLERY = 2;
    private Bitmap bitmap;
    private DrawingView drawing_view;
    private ImageView iv_imgView;
    private ImageButton rotateButton,Undo,imageButton,mImageCurrentImageBtn;
    private Button process;

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
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhotoIntent, GALLERY);
                } else {
                    requestStoragePermission();
                }
            }
        });

        //For Saving Purpose Dont need it now
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isReadStorageAllowed()) {
//                    new BitmapAsyncTask(getBitmapFromView(fl_layout)).execute();
//                } else {
//                    requestStoragePermission();
//                }
//            }
//        });

        //process
        process = findViewById(R.id.process);
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity3.this,MainActivity4.class);
                File file = new File(getExternalFilesDir(null), "image.png");
                Bitmap bitmap_new = getBitmapFromView(fl_layout);
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    bitmap_new.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i.putExtra("imageFilePath", file.getAbsolutePath());
                startActivity(i);
            }
        });



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

        if (requestCode == GALLERY) {
            //
            try {
                iv_imgView.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                iv_imgView.setImageBitmap(bitmap);

            } catch (Exception e) {
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


}
