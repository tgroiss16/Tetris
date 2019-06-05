package com.example.tetris;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Bundle;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Set;
import java.util.UUID;

import static com.example.tetris.R.layout.activity_bluetooth;

public class MultiplayerActivity extends AppCompatActivity {
    Handler bluetoothIn;
    MediaPlayer mediaPlayer;
    static public String mac;
    public boolean musicboolean = OptionsActivity.musicboolean;
    public boolean turnboolean = OptionsActivity.turnboolean;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothAdapter mBtAdapter = null;
    private BluetoothSocket btSocket = null;
    private boolean ready = false;
    private static final UUID MY_UUID = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activity_bluetooth);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            write("16");
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
    @Override
    protected void onResume() {
        super.onResume();


    }
    private OutputStream outputStream;
    private InputStream inStream;

    private void init() throws IOException {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if(bondedDevices.size() > 0) {
                    Object[] devices = (Object []) bondedDevices.toArray();
                    BluetoothDevice device = (BluetoothDevice) devices[0];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());

                    try {
                        socket.connect();
                        Log.e("","Connected");
                    } catch (IOException e) {
                        Log.e("",e.getMessage());
                        try {
                            Log.e("","trying fallback...");

                            socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                            socket.connect();
                            String a = String.valueOf(socket.isConnected());
                            Log.e("",a);
                            Log.e("","Connected");
                        }
                        catch (Exception e2) {
                            Log.e("", "Couldn't establish Bluetooth connection!");
                        }
                    }

                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();

                }


            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    public void run() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true) {
            try {
                bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
