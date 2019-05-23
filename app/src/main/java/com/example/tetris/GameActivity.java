package com.example.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import components.Controls;
import components.GameState;
import engine.Sound;
import engine.WorkThread;
import views.BlockBoardView;
import components.Display;
import views.DefeatDialogFragment;

public class GameActivity extends AppCompatActivity {
    private WorkThread mainThread;
    public GameState game;
    public Sound sound;
    private DefeatDialogFragment defeatDialog;
    public Controls controls;
    public Display display;
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
    public void gameOver(long score, String gameTime, int apm)
    {
        defeatDialog.setData(score, gameTime, apm);
        defeatDialog.show(getSupportFragmentManager(), "hamster");
    }
    public void putScore(long score)
    {
        String playerName = game.getPlayerName();

        if (playerName == null || playerName.equals("")) {
            playerName = getResources().getString(R.string.anonymous);
        }

        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.playername_key), playerName);
        intent.putExtra(getResources().getString(R.string.score_key), score);
        setResult(MainActivity.RESULT_OK, intent);

        finish();
    }
}
