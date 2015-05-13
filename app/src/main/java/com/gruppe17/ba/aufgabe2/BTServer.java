package com.gruppe17.ba.aufgabe2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by privat-patrick on 12.05.2015.
 */
public class BTServer extends Thread {

    private BluetoothServerSocket serverSocket;
    private Context mainContext;
    public final static String SERVICENAME = "MUCmimic";
    public final static String BTUUID = "4080ad8d-8ba2-4846-8803-a3206a8975be";

    public BTServer(Context context) {

        mainContext = context;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            adapter.cancelDiscovery();
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(SERVICENAME, UUID.fromString(BTUUID));
            Toast.makeText(mainContext, "Server wurde gestartet!", Toast.LENGTH_SHORT).show();
            Log.i("BTServer", "serverSocket created");
        } catch (IOException ex) {
            Log.i("server", ex.getMessage());
        }
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        Looper.prepare();

        while(true) {
            try {
                Log.i("server", "waiting for clients");
                socket = serverSocket.accept();

                if (socket != null) {
                    Toast.makeText(mainContext, "Verbindung wurde aufgebaut!", Toast.LENGTH_SHORT).show();
                    Log.i("server", "client connected");
                }
                Looper.loop();
            } catch (IOException ex) {
                Log.i("server", ex.getMessage());
            }
        }

    }
}
