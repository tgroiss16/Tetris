package com.example.tetris;

import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
     MediaPlayer mediaPlayer ;
     public boolean musicboolean = OptionsActivity.musicboolean;
     public boolean turnboolean = OptionsActivity.turnboolean;

     public static int highscorequack;
     public static int losesinmpquack;
     public static int mostlinesclearedquack;
     public static int totallinescleardquack;
     public static int winsinmpquack;
     public static int totalpointsquack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readscores();



        Button btn = (Button)findViewById(R.id.playgame_quickdrop_button);
        Button btn1 = (Button) findViewById(R.id.playgame_moveright_button);
        Button btn2 = (Button)findViewById(R.id.playgame_rotate_button);
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

    public String[] readscores() {
        String text = "";
        File file = new File("scores.txt");
        AssetManager assetManager = getAssets();
        try{
            System.out.println("1");
            InputStream is = assetManager.open("scores.txt");
            System.out.println("2");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        }catch(Throwable a){
            a.printStackTrace();
        }

        System.out.println(text + "Hello");
        String[] s = text.split(",");
        totallinescleardquack = Integer.parseInt(s[0]);
        totalpointsquack = Integer.parseInt(s[1]);
        highscorequack = Integer.parseInt(s[2]);
        mostlinesclearedquack = Integer.parseInt(s[3]);
        winsinmpquack = Integer.parseInt(s[4]);
        losesinmpquack = Integer.parseInt(s[5]);

        return s;

    }
}