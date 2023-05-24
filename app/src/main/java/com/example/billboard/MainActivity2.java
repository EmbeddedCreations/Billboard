package com.example.billboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
    public void CornerMarking(View view){
        Intent i=new Intent(MainActivity2.this,MainActivity3.class);
        startActivity(i);
    }
    public void Process(View view){
        Intent i=new Intent(MainActivity2.this,MainActivity4.class);
        startActivity(i);
    }
}
