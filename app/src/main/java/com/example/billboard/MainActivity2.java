package com.example.billboard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity2 extends Activity {

    private VideoView videoView;
    private Button btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        videoView = findViewById(R.id.videoView);
        btnGoBack = findViewById(R.id.btnGoBack);

        // Set the path of the video file
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.finalgif;

        // Alternatively, you can set the video path from a URL
        // String videoPath = "https://example.com/video.mp4";

        Uri videoUri = Uri.parse(videoPath);

        // Set media controller to enable video playback controls
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set the video URI and start playing
        videoView.setVideoURI(videoUri);
        videoView.start();

        // Set click listener for the Go Back button
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the homepage (MainActivity)
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
