package com.example.prestamos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import com.example.prestamos.Clases.Carrito;
import com.example.prestamos.Clases.Ejemplar;
import com.example.prestamos.Clases.Prestamo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.AbstractCollection;
import java.util.Date;
import java.util.UUID;

public class activity_prestamos extends AppCompatActivity {
    EditText dni, nombre, procedencia, correo, telefono;
    FirebaseFirestore db;
    String fechaActual;
    Button registrar;
    RadioButton interno;
    Handler bluetoothIn;

    final int handlerState = 0;             //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        registrar = findViewById(R.id.generarPrestamo);
        dni = findViewById(R.id.dni);
        nombre = findViewById(R.id.nombrePersona);
        procedencia = findViewById(R.id.procedencia);
        correo = findViewById(R.id.correo);
        telefono = findViewById(R.id.numeroCelular);

        final Button registrar = findViewById(R.id.generarPrestamo);
        final EditText dni = findViewById(R.id.dni);
        final EditText nombre = findViewById(R.id.nombrePersona);
        final EditText procedencia = findViewById(R.id.procedencia);
        final EditText correo = findViewById(R.id.correo);
        final EditText telefono = findViewById(R.id.numeroCelular);

        EditText fecha = findViewById(R.id.fechaPrestamo);
        interno = findViewById(R.id.interno);
        interno.setSelected(true);
        fechaActual = DateFormat.getDateTimeInstance().format(new Date());

        db = FirebaseFirestore.getInstance();

        fecha.setText(fechaActual);


            registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        generarPrestamo(v);

                }

            });

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = "00:18:E4:40:00:06";

        //create device and set the MAC address
        //Log.i("ramiro", "adress : " + address);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);

        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
            Toast.makeText(getBaseContext(), "Conectado al arduino", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La conexion fallo", Toast.LENGTH_LONG).show();

            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
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
    public void generarPrestamo (View v){
        if (validar()){


            registrar.setEnabled(false);
            Prestamo prestamo = new Prestamo(correo.getText().toString(), dni.getText().toString(), fechaActual, nombre.getText().toString(), procedencia.getText().toString(), telefono.getText().toString(), interno.isActivated());

            db.collection("prestamos")
                    .add(prestamo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Carrito.cambiarEstado();
                            for (final Ejemplar ejemplar : Carrito.getLista()) {
                                db.collection("prestamos")
                                        .document(documentReference.getId()).collection("ejemplares").add(ejemplar)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {


                                               if(!interno.isActivated()){
                                                   //Codigo para mandar los libros prestados al arduino
                                                   mConnectedThread.write( "#"+ ejemplar.getRfid());
                                               }

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(activity_prestamos.this, "Prestamo no registrado" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                            db.collection("prestamos").document(documentReference.getId()).update("id",documentReference.getId()) .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(activity_prestamos.this, "Préstamo registrado", Toast.LENGTH_LONG).show();
                                }
                            })
                            ;
                            Carrito.limpiar();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_prestamos.this, "Préstamo no registrado" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    public boolean validar(){
        boolean retorno=true;

        if(dni.getText().length() == 0){
            dni.setError("Ingrese DNI");
            retorno=false;
        }
        if (nombre.getText().length()==0){
            nombre.setError("Ingrese nombre");
            retorno=false;
        }
        if(procedencia.getText().length()==0){
            procedencia.setError("Ingrese procedencia");
            retorno=false;
        }
        if (correo.getText().length()==0){
            correo.setError("Ingrese correo");
            retorno=false;
        }
        if (telefono.getText().length()==0){
            telefono.setError("Ingrese teléfono");
            retorno=false;
        }

        return  retorno;
    }

}
