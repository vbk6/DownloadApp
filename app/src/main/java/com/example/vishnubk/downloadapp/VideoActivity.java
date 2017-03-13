package com.example.vishnubk.downloadapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    private String url;
    private VideoView videoView;
    private SeekBar seekBar;
    private int lengthOfVideo;
    String servicestring = Context.DOWNLOAD_SERVICE;
    DownloadManager downloadmanager;
    private String fileName;

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(VideoActivity.this, "Download complete.", Toast.LENGTH_SHORT).show();
            PlayVideoFile();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        url = getIntent().getExtras().getString("url");
        videoView = (VideoView) findViewById(R.id.videoView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        fileName = url.substring(url.lastIndexOf('/') + 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadVideo();
            }
        });

        if (new File(getExternalFilesDir("").getAbsolutePath() +"/Download/Video/" ,fileName).exists()) {
            fab.hide();
        }
        else
        fab.show();
        PlayVideoFile();
        seekBar.setMax(lengthOfVideo);

        final Handler mHandler = new Handler();

        VideoActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    int mCurrentPosition = videoView.getCurrentPosition();
                    seekBar.setProgress(mCurrentPosition);

                }
                mHandler.postDelayed(this, 1000);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    videoView.start();
                }
            }
        });

    }


    public void DownloadVideo() {

        downloadmanager = (DownloadManager) getSystemService(servicestring);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading video...");
        request.setDestinationInExternalFilesDir(getBaseContext(), Environment.DIRECTORY_DOWNLOADS + "/Video", fileName);

        if (!new File(getExternalFilesDir("").getAbsolutePath() +"/Download/Video/" ,fileName).exists()) {
            downloadmanager.enqueue(request);
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else
            Toast.makeText(VideoActivity.this, "File Already Exists", Toast.LENGTH_SHORT).show();
    }



    public void PlayVideoFile() {
        int timeInMillisec = 0;

        String root = this.getExternalFilesDir("").getAbsolutePath();
        File mFolder = new File(root + "/Download/Video/");
        File file=new File(mFolder,fileName);


        if (file.exists()) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //to get duration of video
            if (file.length() != 0) {
                retriever.setDataSource(getBaseContext(), Uri.fromFile(file));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                timeInMillisec = Integer.parseInt(time);

            }
            String path = this.getExternalFilesDir("/Download/Video/" + fileName).getAbsolutePath();
            seekBar.setMax(timeInMillisec);
            videoView.setVideoPath(path);
            videoView.requestFocus();
            lengthOfVideo = timeInMillisec;
            videoView.start();
        }
    }

}

