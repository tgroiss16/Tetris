package com.example.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import components.Controls;
import components.GameState;
import engine.Sound;
import engine.WorkThread;
import views.BlockBoardView;
import components.Display;

public class GameActivity extends AppCompatActivity {
    private WorkThread mainThread;
    public GameState game;
    public Sound sound;
    MediaPlayer mediaPlayer;
    static public boolean musicboolean = OptionsActivity.musicboolean;
    static public boolean turnboolean = OptionsActivity.turnboolean;
    public Controls controls;
    public Display display;
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);
        Button btn = findViewById(R.id.playgame_quit_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
            }
        });
        TextView tlines = findViewById(R.id.playgame_txtlines);
        tlines.setText(0+"");
        TextView tscore = (TextView) findViewById(R.id.playgame_txtscore);
        tscore.setText(0+"");
        TextView tlevel = (TextView) findViewById(R.id.playgame_txtlevel);
        tlevel.setText(0+"");
        game = (GameState) getLastCustomNonConfigurationInstance();
        game = GameState.getNewInstance(this);
        game.reconnect(this);
        int i;
        controls = new Controls(this);
        display = new Display(this);
        findViewById(R.id.right).setOnClickListener(v -> controls.rightButtonPressed());
        findViewById(R.id.left).setOnClickListener(v -> controls.leftButtonPressed());
        findViewById(R.id.softdrop).setOnClickListener(v -> controls.downButtonPressed());



        ImageButton buttonRotateRight = findViewById(R.id.rotate);
        if (buttonRotateRight != null) {
            (findViewById(R.id.rotate)).setOnTouchListener((view, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    controls.rotateRightPressed();
                    (findViewById(R.id.rotate)).setPressed(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    (findViewById(R.id.rotate)).setPressed(false);
                }

                return true;
            });
        }

        ((BlockBoardView) findViewById(R.id.blockBoardView2)).init();
        ((BlockBoardView) findViewById(R.id.blockBoardView2)).setHost(this);



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

    public void startGame(BlockBoardView c)
    {
        mainThread = new WorkThread(this, c.getHolder());
        mainThread.setFirstTime(false);
        game.setRunning(true);
        mainThread.setRunning(true);
        mainThread.start();
    }
    public void destroyWorkThread()
    {
        boolean retry = true;
        mainThread.setRunning(false);

        while (retry) {
            try {
                mainThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }
    public void gameOver(long scorel, String gameTime, int lines)
    {
        int score = Integer.parseInt(scorel+"");
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
        startActivity(new Intent(GameActivity.this, MainActivity.class));
    }
    public void updateScore(long score, int lines, int level)
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                TextView tlines = findViewById(R.id.playgame_txtlines);
                tlines.setText(lines+"");
                TextView tscore = (TextView) findViewById(R.id.playgame_txtscore);
                tscore.setText(Integer.parseInt(""+score)+"");
                TextView tlevel = (TextView) findViewById(R.id.playgame_txtlevel);
                tlevel.setText(level+"");

            }
        });


    }
}
