package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class OptionsActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = MainActivity.mediaPlayer;
    static public boolean musicboolean;
    static public boolean turnboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ToggleButton musicbutton = (ToggleButton)findViewById(R.id.options_music_button);
        if(musicboolean){
            musicbutton.setChecked(musicboolean);
        }
        musicbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMusicboolean(isChecked);
                if (!mediaPlayer.isPlaying())
                {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
                else if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });



        ToggleButton turnbutton = (ToggleButton)findViewById(R.id.options_turn_button);
        if(turnboolean){
            turnbutton.setChecked(turnboolean);
        }
        turnbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTurnboolean(isChecked);
            }
        });




        Button back = (Button)findViewById(R.id.options_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this, MainActivity.class));
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

    public static void setMusicboolean(boolean musicboolean) {
        OptionsActivity.musicboolean = musicboolean;
    }


    public static void setTurnboolean(boolean turnboolean) {
        OptionsActivity.turnboolean = turnboolean;
    }
}
