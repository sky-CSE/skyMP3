package com.example.skymp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    SeekBar volumeBar, timeBar;
    TextView songTitle, totalLength, lengthCovered;
    Button playBack, playPause, playForward;

    String title, path;
    int position;
    ArrayList<String> list;
    MediaPlayer mediaPlayer;

    Runnable runnable;
    Handler handler;
    int totalTime;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        volumeBar = findViewById(R.id.volume_bar);
        timeBar = findViewById(R.id.time_bar);

        songTitle = findViewById(R.id.song_title);
        lengthCovered = findViewById(R.id.time_covered);
        totalLength = findViewById(R.id.time_length);
        playBack = findViewById(R.id.button_playback);
        playForward = findViewById(R.id.button_playforward);
        playPause = findViewById(R.id.button_play_pause);

        animation = AnimationUtils.loadAnimation(MusicActivity.this,R.anim.translate_animation);
        songTitle.startAnimation(animation);

        title = getIntent().getStringExtra("title");
        path = getIntent().getStringExtra("filepath");
        position = getIntent().getIntExtra("position", 0);
        list = getIntent().getStringArrayListExtra("list");

        songTitle.setText(title);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                playPause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    playPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        playBack.setOnClickListener(v -> {
            mediaPlayer.reset();
            if (position == 0) {
                position = list.size() - 1; //last element
            } else {
                position--;
            }
            String newpath = list.get(position);
            String newtitle = newpath.substring(newpath.lastIndexOf("/") + 1);
            try {
                mediaPlayer.setDataSource(newpath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                songTitle.setText(newtitle);
            } catch (IOException e) {
                e.printStackTrace();
            }
            songTitle.clearAnimation();
            songTitle.startAnimation(animation);
        });

        playForward.setOnClickListener(v -> {
            mediaPlayer.reset();
            if (position == list.size() - 1) {
                position = 0;
            } else {
                position++;
            }
            String newpath = list.get(position);
            String newtitle = newpath.substring(newpath.lastIndexOf("/") + 1);
            try {
                mediaPlayer.setDataSource(newpath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                songTitle.setText(newtitle);
            } catch (IOException e) {
                e.printStackTrace();
            }
            songTitle.clearAnimation();
            songTitle.startAnimation(animation);
        });

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    volumeBar.setProgress(progress);
                    float volumeLevel = progress / 100f;
                    mediaPlayer.setVolume(volumeLevel, volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    timeBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                totalTime = mediaPlayer.getDuration();
                timeBar.setMax(totalTime);

                int currentPosition = mediaPlayer.getCurrentPosition();
                timeBar.setProgress(currentPosition);
                handler.postDelayed(runnable, 1000); //run runnable every 1000ms

                String elapsedtime = createTimeLabel(currentPosition);
                String totaltime = createTimeLabel(totalTime);

                lengthCovered.setText(elapsedtime);
                totalLength.setText(totaltime);

                if (elapsedtime.equals(totaltime)) {
                    mediaPlayer.reset();
                    if (position == list.size() - 1) {
                        position = 0;
                    } else {
                        position++;
                    }
                    String newpath = list.get(position);
                    String newtitle = newpath.substring(newpath.lastIndexOf("/") + 1);
                    try {
                        mediaPlayer.setDataSource(newpath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        playPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                        songTitle.setText(newtitle);
                        songTitle.clearAnimation();
                        songTitle.startAnimation(animation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        handler.post(runnable);
    }

    public String createTimeLabel(int currentPosition) {
        String timeLabel;
        int minute, second;
        //1 min = 60 sec
        //1 sec = 1000 millisecond
        minute = currentPosition / 1000 / 60;
        second = currentPosition / 1000 % 60;

        if (second < 10) {
            timeLabel = minute + ":0" + second;
        } else {
            timeLabel = minute + ":" + second;
        }
        return timeLabel;
    }

}