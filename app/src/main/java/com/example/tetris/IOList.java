package com.example.tetris;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;


public class IOList {
    private int totallinescleard = 0;
    private int totalpoints = 0;
    private int highscore = 0;
    private int mostlinescleared = 0;
    private int winsinmultiplayer = 0;
    private int losesinmultiplayer = 0;

    public IOList(int totallinescleard, int totalpoints, int highscore, int mostlinescleared, int winsinmultiplayer, int losesinmultiplayer) {
        MainActivity m = new MainActivity();
        String ss = m.readscores();
        String[] s = ss.split(",");
        this.totallinescleard = Integer.parseInt(s[0]);
        this.totalpoints = Integer.parseInt(s[1]);
        this.highscore = Integer.parseInt(s[2]);
        this.mostlinescleared = Integer.parseInt(s[3]);
        this.winsinmultiplayer = Integer.parseInt(s[4]);
        this.losesinmultiplayer = Integer.parseInt(s[5]);

        this.totallinescleard =+ totallinescleard;
        this.totalpoints =+ totalpoints;
        this.highscore =+ highscore;
        this.mostlinescleared =+ mostlinescleared;
        this.winsinmultiplayer =+ winsinmultiplayer;
        this.losesinmultiplayer =+ losesinmultiplayer;
        csvwriter();
    }
    public IOList(int totallinescleard, int totalpoints, int highscore, int mostlinescleared) {
        MainActivity m = new MainActivity();
        String ss = m.readscores();
        String[] s = ss.split(",");
        this.totallinescleard = Integer.parseInt(s[0]);
        this.totalpoints = Integer.parseInt(s[1]);
        this.highscore = Integer.parseInt(s[2]);
        this.mostlinescleared = Integer.parseInt(s[3]);
        this.winsinmultiplayer = Integer.parseInt(s[4]);
        this.losesinmultiplayer = Integer.parseInt(s[5]);


        this.totallinescleard =+ totallinescleard;
        this.totalpoints =+ totalpoints;
        this.highscore =+ highscore;
        this.mostlinescleared =+ mostlinescleared;
        csvwriter();
    }
    public IOList(){
        MainActivity m = new MainActivity();
        String ss = m.readscores();
        String[] s = ss.split(",");
        this.totallinescleard = Integer.parseInt(s[0]);
        this.totalpoints = Integer.parseInt(s[1]);
        this.highscore = Integer.parseInt(s[2]);
        this.mostlinescleared = Integer.parseInt(s[3]);
        this.winsinmultiplayer = Integer.parseInt(s[4]);
        this.losesinmultiplayer = Integer.parseInt(s[5]);
    }



    private void csvwriter(){

            FileWriter write;
            try{
                write = new FileWriter("scores.txt");
                write.write(totallinescleard);
                write.write(totalpoints);
                write.write(highscore);
                write.write(mostlinescleared);
                write.write(winsinmultiplayer);
                write.write(losesinmultiplayer);
                write.flush();
                write.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
    }


    public int getTotallinescleard() {
        return totallinescleard;
    }

    public int getTotalpoints() {
        return totalpoints;
    }

    public int getHighscore() {
        return highscore;
    }

    public int getMostlinescleared() {
        return mostlinescleared;
    }

    public int getWinsinmultiplayer() {
        return winsinmultiplayer;
    }

    public int getLosesinmultiplayer() {
        return losesinmultiplayer;
    }
}
