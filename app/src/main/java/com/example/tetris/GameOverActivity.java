package com.example.tetris;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {
    int score = 0;
    int lines = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Button btn = findViewById(R.id.endgame_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameOverActivity.this, MainActivity.class));
            }
        });

        SharedPreferences pref = this.getSharedPreferences("recentscores", Context.MODE_PRIVATE);
        score =  pref.getInt("score", 2);
        lines =  pref.getInt("lines",3);

        TextView tlines = findViewById(R.id.endgame_linesclearde);
        tlines.setText(lines+"");
        TextView tscore =  findViewById(R.id.endgame_score);
        tscore.setText(""+score);
    }
}
