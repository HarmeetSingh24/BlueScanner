package com.example.harmeetsingh.bluescanner;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import helper.Db;

//import android.support.v7.app.AppCompatActivity;

public class MainActivity extends Activity {
    private Db dbHeplper;
    private Button ser;
    private BluetoothAdapter ba;
    final static String[] name = {"B0:B4:48:B9:57:86"};
    private Button bt1;
    private boolean isset = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button) findViewById(R.id.button1);
        ser = (Button) findViewById(R.id.button);
        ba = BluetoothAdapter.getDefaultAdapter();
        ba.startDiscovery();
        if(ba == null){
            bt1.setEnabled(false);
            ser.setEnabled(false);
        }else{
            if(!ba.isEnabled()){
                // ba.startDiscovery();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
                //st();
            }else{
                // ba.startDiscovery();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
                //st();
            }
        }

    }

    public MainActivity() {
        super();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(),"Searching for Help",Toast.LENGTH_LONG).show();
        ba.startDiscovery();
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }



    final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            try {
                if (BluetoothDevice.ACTION_FOUND.equals(act)) {
                    //  bt1.setEnabled(true);
                    //  ser.setEnabled(true);
                    final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    for (int i = 0; i < name.length; i++) {

                        if (name[i].equals(device.getAddress())) {

                            //connect(device);
                            isset = true;
                            Toast.makeText(getApplicationContext(), "Hit for Help", Toast.LENGTH_LONG).show();
                            bt1.setEnabled(true);
                            st1(device.getAddress());
                            ser.setEnabled(true);
                            st2(device.getAddress());
                            break;
                        }else{
                            isset = false;
                            //Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_LONG).show();
                            //bt1.setEnabled(false);
                            //ser.setEnabled(false);
                            //ba.cancelDiscovery();
                        }
                    }
                }/* else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(act)) {

                    bt1.setEnabled(false);
                    ser.setEnabled(false);
                    //ba.startDiscovery();

                }*/
                else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(act)){
                    if(isset==false) {
                        bt1.setEnabled(false);
                        ser.setEnabled(false);
                    }
                    ba.startDiscovery();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void st1(String h){

        dbHeplper = new Db(getApplicationContext());
        try {
            dbHeplper.createDataBase(h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String list = dbHeplper.getAllUser();
        String[] l = list.split(":");
        final String phone = l[2];
        final String message = "Need Help ASAP at "+l[0];
        //final String phoner = phone;
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("phone",phone);
                //Toast.makeText(getApplicationContext(), phone, Toast.LENGTH_LONG).show();
                SmsManager.getDefault().sendTextMessage(phone, null, message, null, null);
            }
        });
    }
    public void st2(String h){

        dbHeplper = new Db(getApplicationContext());
        try {
            dbHeplper.createDataBase(h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String list = dbHeplper.getAllUser();
        String[] l = list.split(":");
        final String phone = "tel:"+l[2];
        //final String message = "Need Help ASAP at "+l[0];
        //final String phoner = phone;
        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse(phone));
                startActivity(call);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
