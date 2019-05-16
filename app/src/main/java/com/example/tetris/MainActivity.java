package com.example.tetris;

import android.content.Intent;
import android.graphics.Region;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
     public boolean musicboolean = OptionsActivity.musicboolean;
     public boolean soundboolean = OptionsActivity.soundboolean;
     public boolean turnboolean = OptionsActivity.turnboolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try {
//            Intent intent = getIntent();
//            Bundle extras = intent.getExtras();
//            String music = extras.getString("musicboolean");
//            String sound = extras.getString(OptionsActivity.soundboolean + "");
//            String turn = extras.getString(OptionsActivity.turnboolean + "");
//            musicboolean = Boolean.getBoolean(music);
//            soundboolean = Boolean.getBoolean(sound);
//            turnboolean = Boolean.getBoolean(turn);
//        }catch(Throwable t){
//
//        }
        System.out.println("Main");
        System.out.println(musicboolean);
        System.out.println(soundboolean);
        System.out.println(turnboolean);
//        Bundle bund1 = new Bundle();
//        final Intent intent1 = new Intent(this, GameActivity.class);
//        bund1.putString("musicboolean", musicboolean+"");
//        bund1.putString(soundboolean+"",soundboolean+"");
//        bund1.putString(turnboolean+"",turnboolean+"");
//        intent1.putExtras(bund1);
//        Bundle bund2 = new Bundle();
//        final Intent intent2 = new Intent(this, MultiplayerActivity.class);
//        bund2.putString("musicboolean", musicboolean+"");
//        bund2.putString(soundboolean+"",soundboolean+"");
//        bund2.putString(turnboolean+"",turnboolean+"");
//        intent2.putExtras(bund2);
//        Bundle bund3 = new Bundle();
//        final Intent intent3 = new Intent(this, OptionsActivity.class);
//        bund3.putString("musicboolean", musicboolean+"");
//        bund3.putString(soundboolean+"",soundboolean+"");
//        bund3.putString(turnboolean+"",turnboolean+"");
//        intent3.putExtras(bund3);
//        Bundle bund4 = new Bundle();
//        final Intent intent4 = new Intent(this, StatisticsActivity.class);
//        bund4.putString("musicboolean", musicboolean+"");
//        bund4.putString(soundboolean+"",soundboolean+"");
//        bund4.putString(turnboolean+"",turnboolean+"");
//        intent4.putExtras(bund4);




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
