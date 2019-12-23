package com.example.prestamos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBtAdapter;
    Handler bluetoothIn;

    final int handlerState = 0;             //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private MainActivity.ConnectedThread mConnectedThread;
    private final int NOTIF_ALERTA_ID = 1;
    private final String CHANNEL_ID = "BIBLIOTECA_NOTIFICACION";
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private String address = null;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        ImageView imageLibros = findViewById(R.id.imageLibros);
        ImageView imagePrestamo = findViewById(R.id.imagePrestamo);
        ImageView imageLibrosPrestados = findViewById(R.id.imageLibrosPrestados);
        ImageView imageEntregas = findViewById(R.id.imageEntregas);

        imageLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, libros.class));
            }
        });

        imagePrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, carrito_prestamo.class));
            }
        });

        imageLibrosPrestados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, Prestamos_Internos.class));
            }
        });

        imageEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, Prestamos_Externos.class));
            }
        });

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {          //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);              //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("#");// determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        // extract string
                        Toast.makeText(MainActivity.this, dataInPrint, Toast.LENGTH_SHORT).show();
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(MainActivity.this,null);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                            CharSequence name = "Biblioteca alerta";
                            String description = "Alertar salidas no autorizadas";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                            mChannel.setDescription(description);
                            mChannel.enableLights(true);

                            mChannel.setLightColor(Color.RED);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
                            mNotificationManager.createNotificationChannel(mChannel);
                            mBuilder =
                                    new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID);
                        }

                        mBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                                        .setContentTitle("Mensaje de Alerta")
                                        .setContentText(dataInPrint)
                                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                        .setTicker("Alerta!")
                                        .setAutoCancel(false);
//                        Intent notIntent =
//                                new Intent(MainActivity.this, MainActivity.class);
//
//                        PendingIntent contIntent =
//                                PendingIntent.getActivity(
//                                        MainActivity.this, 0, notIntent, 0);
//
//                        mBuilder.setContentIntent(contIntent);


                        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
                        recDataString.delete(0, recDataString.length());      //clear all string data
                        // strIncom =" ";
                    }
                }

            }
        };
    }



    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBtAdapter.isEnabled())  {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBTState();


        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        //Get the MAC address from the DeviceListActivty via EXTRA
        address = "00:18:E4:40:00:06";
        // Initialize array adapter for paired devices
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!btSocket.isConnected()) {
            // Get the local Bluetooth adapter


            //create device and set the MAC address
            //Log.i("ramiro", "adress : " + address);


            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
                Toast.makeText(getBaseContext(), "Conectado al arduino", Toast.LENGTH_LONG).show();

            } catch (IOException e) {

            }
        }
        mConnectedThread = new MainActivity.ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off


    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);         //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();

            }
        }
    }

}
