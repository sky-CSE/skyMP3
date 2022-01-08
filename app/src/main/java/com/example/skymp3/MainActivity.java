package com.example.skymp3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    //this gives the path of external storage directory as a string and '/' is used to access sub files
    private final static String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";

    private ArrayList<String> songList = new ArrayList<>();
    private MusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getAllAudioFiles();
        }

    }

    private void getAllAudioFiles() {
        if (MEDIA_PATH != null) {
            File mainFile = new File(MEDIA_PATH);
            File[] fileList = mainFile.listFiles(); //with this method all files would be transferred to fileList array resp.
            if(fileList!=null)
            for (File file : fileList) {
                Log.e("Media path", file.toString());

                if (file.isDirectory())
                    scanDirectory(file);

                else {
                    String path = file.getAbsolutePath();
                    if (path.endsWith(".mp3"))
                        songList.add(path);
                }
            }
        }
    }

    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] fileList = directory.listFiles();//with this method all files would be transferred to fileList array resp.
            if(fileList!=null)
            for (File file : fileList) {
                Log.e("Media path", file.toString());

                if (file.isDirectory())
                    scanDirectory(file);

                else {
                    String path = file.getAbsolutePath();
                    if (path.endsWith(".mp3")) {
                        songList.add(path);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

        }
        adapter = new MusicAdapter(MainActivity.this, songList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //if agrees to let skyMP3 read external storage
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllAudioFiles();
        }
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Grant permission to read and show songs", Toast.LENGTH_LONG).show();
        }
    }
}