package com.gruppe17.ba.aufgabe2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private final static int REQUEST_ENABLE_BT = 15;
    private static final int REQUEST_ENABLE_DISC = 14;

    private BluetoothAdapter btAdapter;

    private ArrayList<BluetoothDevice> activeDevices;
    private ArrayList<String> activeDevicesStrings;

    private IntentFilter filter = new IntentFilter();

    private ArrayAdapter<BluetoothDevice> listAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.listview);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        activeDevicesStrings = new ArrayList<String>();
        activeDevices = new ArrayList<BluetoothDevice>();

        listAdapter = new ArrayAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_1, activeDevices);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = (BluetoothDevice) parent.getAdapter().getItem(position);
            }
        });

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        initBluetooth();


    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(receiver, filter);
    }


    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(receiver);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_ENABLE_BT:
                btAdapter.startDiscovery();
                break;
            case REQUEST_ENABLE_DISC:

                break;
        }
    }


    public void startServer(View view) {
        BTServer server = new BTServer();

        server.start();
    }



    private void initBluetooth() {
        if (btAdapter == null) {
            Log.i("error", "Kein BluetoothAdapter vorhanden");
        }
        else if (btAdapter.isEnabled()) {
            btAdapter.startDiscovery();
        }
        else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i("broadcast", "bluetooth disovery started..");
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                activeDevices.add(device);
                activeDevicesStrings.add(device.getName() + " " + device.getAddress());
                listAdapter.notifyDataSetChanged();
                Log.i("broadcast", "Device found: " + device.getName());

            } else if (BluetoothDevice.ACTION_UUID.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                if (uuids != null) {
                    Log.i("broadcast", "Uuids found for device " + device.getName());
                    for(int i=0;i<uuids.length;i++) {
                        Log.i("broadcast", "uuid :" + uuids[i].toString());
                    }
                } else {
                    Log.i("broadcast", "uuids null");
                }

            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i("broadcast", "bluetooth discovery finished..");
                for (int i=0;i<activeDevices.size();i++) {
                    BluetoothDevice device = activeDevices.get(i);

                    if (!device.fetchUuidsWithSdp()) {
                        Log.i("broadcast", "error while fetching uuid for device: " + device.getName());
                    }
                }
            }
        }
    };

}
