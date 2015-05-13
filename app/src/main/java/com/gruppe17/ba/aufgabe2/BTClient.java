package com.gruppe17.ba.aufgabe2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
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

    public BTClient(Context context, BluetoothDevice device) {
        mainContext = context;
        serverDevice = device;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(BTServer.BTUUID));
        } catch (IOException ex) {
            Log.i("error", ex.getMessage());
        }
    }


    @Override
    public void run() {

        btAdapter.cancelDiscovery();
        try {
            btSocket.connect();
            Toast.makeText(mainContext, "Verbindung wurde aufgebaut!", Toast.LENGTH_SHORT);
        } catch (Exception ex) {
            Log.i("error", ex.getMessage());
        }

    }

}
