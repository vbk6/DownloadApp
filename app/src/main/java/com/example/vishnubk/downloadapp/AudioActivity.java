package com.example.vishnubk.downloadapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    private String url;
    private ProgressDialog progressDialog;
    private SeekBar seekBar;
    private MediaPlayer mp;
    private int duration;
    String servicestring = Context.DOWNLOAD_SERVICE;
    DownloadManager downloadmanager;
    private String fileName;

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(AudioActivity.this, "Download complete.", Toast.LENGTH_SHORT).show();
            PlayAudio();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        url = getIntent().getExtras().getString("url");
        seekBar=(SeekBar)findViewById(R.id.audioSeekBar);
        mp = new MediaPlayer();
        fileName = url.substring(url.lastIndexOf('/') + 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadAudio();
            }
        });

         final Handler mHandler = new Handler();
         AudioActivity.this.runOnUiThread(new Runnable() {

             @Override
             public void run() {
                 if (mp != null) {
                     int mCurrentPosition = mp.getCurrentPosition();
                     seekBar.setProgress(mCurrentPosition);
                 }
                 mHandler.postDelayed(this, 1000);
             }
         });


        if (new File(getExternalFilesDir("").getAbsolutePath() +"/Download/Audio/" ,fileName).exists()) {
            fab.hide();
        }
        else
            fab.show();

        PlayAudio();

        duration=mp.getDuration();
        seekBar.setMax(duration);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mp != null && fromUser){
                    mp.seekTo(progress);
                }
            }
        });

    }

    private void DownloadAudio() {

        downloadmanager = (DownloadManager) getSystemService(servicestring);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading audio...");
        request.setDestinationInExternalFilesDir(getBaseContext(), Environment.DIRECTORY_DOWNLOADS + "/Audio", fileName);

        if (!new File(getExternalFilesDir("").getAbsolutePath() +"/Download/Audio/" ,fileName).exists()) {
            downloadmanager.enqueue(request);
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else
            Toast.makeText(AudioActivity.this, "File Already Exists", Toast.LENGTH_SHORT).show();
    }


    public void PlayAudio(){

        String root = this.getExternalFilesDir("").getAbsolutePath();
        File mFolder = new File(root + "/Download/Audio/");
        File file=new File(mFolder,fileName);

        if (file.exists()) {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            String path = this.getExternalFilesDir("/Download/Audio/" + fileName).getAbsolutePath();
            Log.d("hhh", path + "");

            try {

                mp.setDataSource(path);
                mp.prepare();
                duration=mp.getDuration();
                seekBar.setMax(duration);
                mp.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   /* @Override
    public void onBackPressed() {
        mp.stop();
        Intent i=new Intent(AudioActivity.this,MainActivity.class);
        startActivity(i);
    }*/

}
