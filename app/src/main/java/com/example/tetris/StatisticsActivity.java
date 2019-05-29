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
        final TextView highscore = (TextView) findViewById(R.id.statistics_txthighscore);
        final TextView losesinmp = (TextView) findViewById(R.id.statistics_txtlosesinmp);
        final TextView mostlinescleared = (TextView) findViewById(R.id.statistics_txtmostlinescleared);
        final TextView totallinescleared = (TextView) findViewById(R.id.statistics_txttotallinescleared);
        final TextView winsinmp = (TextView) findViewById(R.id.statistics_txtwinsinmp);
        final TextView points = (TextView) findViewById(R.id.statistics_txtpoints);
        IOList io = new IOList();
        highscore.setText(io.getHighscore());
        losesinmp.setText(io.getLosesinmultiplayer());
        mostlinescleared.setText(io.getMostlinescleared());
        totallinescleared.setText(io.getTotallinescleard());
        winsinmp.setText(io.getWinsinmultiplayer());
        points.setText(io.getTotalpoints());








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
