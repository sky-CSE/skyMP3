package com.example.skymp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MusicActivity extends AppCompatActivity {
    ProgressBar volumeBar,timeBar;
    TextView totalLength,lengthCovered;
    Button playBack,playPause,playForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        volumeBar=findViewById(R.id.volume_bar);
        timeBar= findViewById(R.id.time_bar);

        lengthCovered=findViewById(R.id.time_covered);
        totalLength = findViewById(R.id.time_length);
        playBack=findViewById(R.id.button_playback);
        playForward=findViewById(R.id.button_playforward);
        playPause=findViewById(R.id.button_play_pause);
    }
}