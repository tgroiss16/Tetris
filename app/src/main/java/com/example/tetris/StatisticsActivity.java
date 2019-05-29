package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        final TextView highscore = findViewById(R.id.statistics_txthighscore);
        final TextView losesinmp = findViewById(R.id.statistics_txtlosesinmp);
        final TextView mostlinescleared = findViewById(R.id.statistics_txtmostlinescleared);
        final TextView totallinescleared = findViewById(R.id.statistics_txttotallinescleared);
        final TextView winsinmp = findViewById(R.id.statistics_txtwinsinmp);
        final TextView points = findViewById(R.id.statistics_txtpoints);

        SharedPreferences prefs = this.getSharedPreferences("scores", Context.MODE_PRIVATE);
        int highscorequack = prefs.getInt("highscore", 0);
        int totallinescleardquack = prefs.getInt("totallinescleared",0);
        int totalpointsquack = prefs.getInt("totalpoints",0);
        int mostlinesclearedquack = prefs.getInt("mostlinescleared",0);
        int losesinmpquack = prefs.getInt("losesinmp", 0);
        int winsinmpquack = prefs.getInt("winsinmp", 0);
        System.out.println(highscorequack+"Meh");
        highscore.setText(highscorequack+"");
        losesinmp.setText(losesinmpquack+"");
        mostlinescleared.setText(mostlinesclearedquack+"");
        totallinescleared.setText(totallinescleardquack+"");
        winsinmp.setText(winsinmpquack+"");
        points.setText(totalpointsquack+"");

        SharedPreferences press = this.getSharedPreferences("highscores", Context.MODE_PRIVATE);
        int top1 = press.getInt("top1", 0);
        int top2 = press.getInt("top2",0);
        int top3 = press.getInt("top3",0);


        List<Integer> l = new ArrayList<>();
        l.add(top1);
        l.add(top2);
        l.add(top3);
        ListView listView = findViewById(R.id.statistics_highscorelist);
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,l);
        listView.setAdapter(spinnerAdapter);


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
