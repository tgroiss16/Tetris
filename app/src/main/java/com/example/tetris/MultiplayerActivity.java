package com.example.tetris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MultiplayerActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    public boolean musicboolean = OptionsActivity.musicboolean;
    public boolean soundboolean = OptionsActivity.soundboolean;
    public boolean turnboolean = OptionsActivity.turnboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        Button btn = (Button) findViewById(R.id.multiplayer_quit_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiplayerActivity.this, MainActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        musicboolean = OptionsActivity.musicboolean;
        soundboolean = OptionsActivity.soundboolean;
        turnboolean = OptionsActivity.turnboolean;
        if(!musicboolean){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tetristheme);
            mediaPlayer.start();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!musicboolean) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }
}
