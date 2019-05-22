package com.example.tetris;

import android.content.Intent;
import android.graphics.Region;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
     MediaPlayer mediaPlayer ;
     public boolean musicboolean = OptionsActivity.musicboolean;
     public boolean soundboolean = OptionsActivity.soundboolean;
     public boolean turnboolean = OptionsActivity.turnboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button btn = (Button)findViewById(R.id.main_playgame_button);
        Button btn1 = (Button) findViewById(R.id.main_multiplayer_button);
        Button btn2 = (Button)findViewById(R.id.main_statistics_button);
        Button btn3 = (Button)findViewById(R.id.main_button_options);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MultiplayerActivity.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OptionsActivity.class));
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
