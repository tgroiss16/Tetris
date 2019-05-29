package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    static public boolean musicboolean = OptionsActivity.musicboolean;
    static public boolean turnboolean = OptionsActivity.turnboolean;

    public int score = 3000000;
    public int lines = 32131;

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






        //Here Davids fuck ton of magic happens





        SharedPreferences prefs = this.getSharedPreferences("scores", Context.MODE_PRIVATE);
        int highscorequack = prefs.getInt("highscore", 0);
        int totallinescleardquack = prefs.getInt("totallinescleared",0);
        int totalpointsquack = prefs.getInt("totalpoints",0);
        int mostlinesclearedquack = prefs.getInt("mostlinescleared",0);
        SharedPreferences pres = this.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        int top1 = pres.getInt("top1",0);
        int top2 = pres.getInt("top2",0);
        int top3 = pres.getInt("top3",0);

        if(top3<=score){
            if(top2<=score){
                if(top1<=score){
                    top3= top2;
                    top2 = top1;
                    top1 = score;
                }
                else{
                    top3 = top2;
                    top2 = score;
                }
            }
            else{
                top3 = score;
            }
        }
        SharedPreferences press = this.getSharedPreferences("highscores",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = press.edit();
        edit.putInt("top1", top1);
        edit.putInt("top2", top2);
        edit.putInt("top3", top3);
        edit.commit();


        if(score != 0 && lines != 0){
            if(highscorequack < score){
                highscorequack = score;
            }
            if(mostlinesclearedquack < lines){
                mostlinesclearedquack = lines;
            }
            totalpointsquack += score;
            totallinescleardquack += lines;
            score = 0;
            lines = 0;
        }
        SharedPreferences prefss = this.getSharedPreferences("scores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefss.edit();
        editor.putInt("highscore", highscorequack);
        editor.putInt("totallinescleared",totallinescleardquack);
        editor.putInt("totalpoints", totalpointsquack);
        editor.putInt("mostlinescleared", mostlinesclearedquack);
        editor.commit();


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
