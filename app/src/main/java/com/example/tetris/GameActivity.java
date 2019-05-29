package com.example.tetris;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
        game = (GameState) getLastCustomNonConfigurationInstance();
        game = GameState.getNewInstance(this);
        game.reconnect(this);

        controls = new Controls(this);
        display = new Display(this);
        findViewById(R.id.right).setOnClickListener(v -> controls.rightButtonPressed());

        findViewById(R.id.left).setOnClickListener(v -> controls.leftButtonPressed());

        findViewById(R.id.softdrop).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                controls.downButtonPressed();
                (findViewById(R.id.softdrop)).setPressed(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                controls.downButtonReleased();
                (findViewById(R.id.softdrop)).setPressed(false);
            }

            return true;
        });


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

    }

}
