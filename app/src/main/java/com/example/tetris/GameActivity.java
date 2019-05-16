package com.example.tetris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    static public boolean musicboolean = OptionsActivity.musicboolean;
    static public boolean soundboolean = OptionsActivity.soundboolean;
    static public boolean turnboolean = OptionsActivity.turnboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);
        Button btn = (Button)findViewById(R.id.playgame_quit_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
            }
        });
        try {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String music = extras.getString("musicboolean");
            String sound = extras.getString(OptionsActivity.soundboolean + "");
            String turn = extras.getString(OptionsActivity.turnboolean + "");
            musicboolean = Boolean.getBoolean(music);
            soundboolean = Boolean.getBoolean(sound);
            turnboolean = Boolean.getBoolean(turn);
        }catch(Throwable t){

        }
        System.out.println("Game");
        System.out.println(musicboolean);
        System.out.println(soundboolean);
        System.out.println(turnboolean);


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
