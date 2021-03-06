package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {
    int score = 0;
    int lines = 0;
    MediaPlayer mediaPlayer = MainActivity.mediaPlayer;
    static public boolean musicboolean = OptionsActivity.musicboolean;
    static public boolean turnboolean = OptionsActivity.turnboolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Button btn = findViewById(R.id.endgame_button);
        SharedPreferences pref = this.getSharedPreferences("recentscores", Context.MODE_PRIVATE);
        score =  pref.getInt("score", 2);
        lines =  pref.getInt("lines",3);

        TextView tlines = findViewById(R.id.endgame_linesclearde);
        tlines.setText(lines+"");
        TextView tscore =  findViewById(R.id.endgame_score);
        tscore.setText(""+score);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOverActivity.this, MainActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
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
