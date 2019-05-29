package com.example.tetris;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class IOList {
    private int totallinescleard = 0;
    private int totalpoints = 0;
    private int highscore = 0;
    private int mostlinescleared = 0;
    private int winsinmultiplayer = 0;
    private int losesinmultiplayer = 0;
    private InputStream inputStream;
    private OutputStream outputStream;

    public IOList(int totallinescleard, int totalpoints, int highscore, int mostlinescleared, int winsinmultiplayer, int losesinmultiplayer) {
        MainActivity m = new MainActivity();
        inputStream = m.getInputStream();
        csvreader();

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
        inputStream = m.getInputStream();
        csvreader();

        this.totallinescleard =+ totallinescleard;
        this.totalpoints =+ totalpoints;
        this.highscore =+ highscore;
        this.mostlinescleared =+ mostlinescleared;
        csvwriter();
    }

    private void csvreader(){
        String[] row = new String[6];
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                row = csvLine.split(";");
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }

        totallinescleard = Integer.parseInt(row[0]);
        totalpoints = Integer.parseInt(row[1]);
        highscore = Integer.parseInt(row[2]);
        mostlinescleared = Integer.parseInt(row[3]);
        winsinmultiplayer = Integer.parseInt(row[4]);
        losesinmultiplayer = Integer.parseInt(row[5]);
    }

    private void csvwriter(){
        BufferedWriter write = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
           write.write(totallinescleard);
           write.write(totalpoints);
           write.write(highscore);
           write.write(mostlinescleared);
           write.write(winsinmultiplayer);
           write.write(losesinmultiplayer);
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                outputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
    }



}
