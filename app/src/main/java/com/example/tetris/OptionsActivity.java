package com.example.tetris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class OptionsActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    static public boolean musicboolean;
    static public boolean soundboolean;
    static public boolean turnboolean;
    private boolean curchanged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        System.out.println("Options");
        System.out.println(musicboolean);
        System.out.println(soundboolean);
        System.out.println(turnboolean);

        ToggleButton musicbutton = (ToggleButton)findViewById(R.id.options_music_button);
        if(musicboolean){
            musicbutton.setChecked(musicboolean);
        }
        musicbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!musicboolean) {
                    mediaPlayer.stop();
                    curchanged = true;
                }
                setMusicboolean(isChecked);

                System.out.println("Listener"+isChecked);
            }
        });

        ToggleButton soundbutton = (ToggleButton)findViewById(R.id.options_sound_button);
        if(soundboolean){
            soundbutton.setChecked(soundboolean);

        }
        soundbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSoundboolean(isChecked);
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




//        Bundle bund = new Bundle();
//        final Intent intent = new Intent(this, MainActivity.class);
//        bund.putString("musicboolean", musicboolean+"");
//        System.out.println("Musicboolean bundle:"+musicboolean);
//        bund.putString(soundboolean+"",soundboolean+"");
//        bund.putString(turnboolean+"",turnboolean+"");
//        intent.putExtras(bund);

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
        if(!musicboolean){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tetristheme);
            mediaPlayer.start();

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!musicboolean&&curchanged) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        curchanged = false;

    }

    public boolean isMusicboolean() {
        return musicboolean;
    }

    public boolean isSoundboolean() {
        return soundboolean;
    }

    public boolean isTurnboolean() {
        return turnboolean;
    }

    public static void setMusicboolean(boolean musicboolean) {
        OptionsActivity.musicboolean = musicboolean;
    }

    public static void setSoundboolean(boolean soundboolean) {
        OptionsActivity.soundboolean = soundboolean;
    }

    public static void setTurnboolean(boolean turnboolean) {
        OptionsActivity.turnboolean = turnboolean;
    }
}
