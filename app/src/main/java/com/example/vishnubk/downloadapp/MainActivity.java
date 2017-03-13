package com.example.vishnubk.downloadapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button imageButton;
    Button videoButton;
    Button audioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton=(Button)findViewById(R.id.image_button);
        videoButton=(Button)findViewById(R.id.video_button);
        audioButton=(Button)findViewById(R.id.audio_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://media.zigcdn.com/media/wallpaper/2015/May/mercedes-benz-amg-gt-wallpaper-pic-image-photo-zigwheels-29052015_640x480.jpg"
;                Intent i=new Intent(MainActivity.this, ImageActivity.class);
                i.putExtra("url",url);
                startActivity(i);
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://archive.org/download/1mbFile/1mb.mp4";
                Intent i=new Intent(MainActivity.this,VideoActivity.class);
                i.putExtra("url",url);
                startActivity(i);
            }
        });

        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="http://www.kozco.com/tech/32.mp3";
               Intent i=new Intent(MainActivity.this,AudioActivity.class);
                i.putExtra("url",url);
                startActivity(i);
            }
        });
    }
}
