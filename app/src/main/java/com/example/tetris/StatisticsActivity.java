package com.example.tetris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StatisticsActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    public boolean musicboolean = OptionsActivity.musicboolean;
    public boolean turnboolean = OptionsActivity.turnboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_statistics);
        Button btn = (Button)findViewById(R.id.statistics_back_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, MainActivity.class));
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tetristheme);
        if (!mediaPlayer.isPlaying() && !musicboolean)
        {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }


}
