package com.example.vishnubk.downloadapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

public class ImageActivity extends AppCompatActivity {

    private String URL;
    private ImageView image;
    private ProgressDialog progressDialog;
    Context context;
    String servicestring = Context.DOWNLOAD_SERVICE;
    DownloadManager downloadmanager;
    private String fileName;
    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ImageActivity.this, "Download complete.", Toast.LENGTH_SHORT).show();
            showImage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        URL=getIntent().getExtras().getString("url");
        image = (ImageView) findViewById(R.id.imageView);
        context=this;
        fileName = URL.substring(URL.lastIndexOf('/') + 1);
        showImage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadImage();
            }
        });

        if (new File(getExternalFilesDir("").getAbsolutePath() +"/Download/Image/" ,fileName).exists()) {
            fab.hide();
        }
        else
            fab.show();

    }

    private void DownloadImage() {

        downloadmanager = (DownloadManager) getSystemService(servicestring);
        Uri uri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle("Downloading image...");
        request.setDestinationInExternalFilesDir(getBaseContext(), Environment.DIRECTORY_DOWNLOADS + "/Image", fileName);

        if (!new File(getExternalFilesDir("").getAbsolutePath() +"/Download/Image/" ,fileName).exists()) {
            downloadmanager.enqueue(request);
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }else
            Toast.makeText(ImageActivity.this, "File Already Exists", Toast.LENGTH_SHORT).show();

    }


    private void showImage() {
        String fileName = URL.substring(URL.lastIndexOf('/') + 1);
        String path=this.getExternalFilesDir("/Download/Image/").getAbsolutePath();
        try{
            File f=new File(path, fileName);
            Bitmap savedBitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            image.setImageBitmap(savedBitmap);
        }
        catch(Exception e){
        }
    }

}
