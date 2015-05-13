package com.gruppe17.ba.aufgabe2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by privat-patrick on 13.05.2015.
 */
public class BTClient extends Thread {

    private BluetoothDevice serverDevice;
    private BluetoothSocket btSocket;
    private BluetoothAdapter btAdapter;
    private Context mainContext;

    public BTClient(Context context) {
        mainContext = context;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public void connect(BluetoothDevice device) {
        serverDevice = device;

        try {
            btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(BTServer.BTUUID));
        } catch (IOException ex) {
            Log.i("error", ex.getMessage());
        }
        start();
    }


    @Override
    public void run() {

        btAdapter.cancelDiscovery();
        Looper.prepare();
        try {
            btSocket.connect();
            Toast.makeText(mainContext, "Verbindung wurde aufgebaut!", Toast.LENGTH_SHORT).show();
            Log.i("client", "client connected");
            Looper.loop();
        } catch (Exception ex) {
            Log.i("error", ex.getMessage());
        }

    }

}
