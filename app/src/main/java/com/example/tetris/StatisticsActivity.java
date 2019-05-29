package com.example.tetris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    public boolean musicboolean = OptionsActivity.musicboolean;
    public boolean turnboolean = OptionsActivity.turnboolean;

    public int highscorequack = MainActivity.highscorequack;
    public int losesinmpquack = MainActivity.losesinmpquack;
    public int mostlinesclearedquack = MainActivity.mostlinesclearedquack;
    public int totallinescleardquack = MainActivity.totallinescleardquack;
    public int winsinmpquack = MainActivity.winsinmpquack;
    public int totalpointsquack = MainActivity.totalpointsquack;

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
        final TextView highscore = findViewById(R.id.statistics_txthighscore);
        final TextView losesinmp = findViewById(R.id.statistics_txtlosesinmp);
        final TextView mostlinescleared = findViewById(R.id.statistics_txtmostlinescleared);
        final TextView totallinescleared = findViewById(R.id.statistics_txttotallinescleared);
        final TextView winsinmp = findViewById(R.id.statistics_txtwinsinmp);
        final TextView points = findViewById(R.id.statistics_txtpoints);

        System.out.println(highscorequack+"Meh");
        highscore.setText(highscorequack+"");
        losesinmp.setText(losesinmpquack+"");
        mostlinescleared.setText(mostlinesclearedquack+"");
        totallinescleared.setText(totallinescleardquack+"");
        winsinmp.setText(winsinmpquack+"");
        points.setText(totalpointsquack+"");








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
