package com.example.prestamos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.prestamos.Clases.Carrito;
import com.example.prestamos.Clases.Ejemplar;
import com.example.prestamos.Clases.EjemplarAdaptador;
import com.example.prestamos.Clases.ListaPrestamo;
import com.example.prestamos.Clases.ListaPrestamoInterno;
import com.example.prestamos.Clases.Prestamo;
import com.example.prestamos.Clases.PrestamoAdaptador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class detalle_prestamo extends AppCompatActivity {
    private EjemplarAdaptador adaptador;
    private Button devolver;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String pres;
    Handler bluetoothIn;

    final int handlerState = 0;             //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private detalle_prestamo.ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_prestamo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            TextView dni = findViewById(R.id.detalle_dni);
            TextView nombre = findViewById(R.id.detalle_nombre);
            TextView procedencia = findViewById(R.id.detalle_procedencia);
            TextView telefono = findViewById(R.id.procedencia_telefono);
            TextView correo = findViewById(R.id.detalle_correo);
            TextView fecha = findViewById(R.id.fechaPrestamo);

            devolver = findViewById(R.id.btnDevolver);

        final Prestamo prestamo;
        if(getIntent().getBooleanExtra("tipo",true)){
          prestamo=  ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0));
        }else prestamo = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0));
            dni.setText(prestamo.getDni());
            nombre.setText(prestamo.getNombre());
            procedencia.setText(prestamo.getProcedencia());
            telefono.setText(prestamo.getTelefono());
            correo.setText(prestamo.getCorreo());
        final ListView ejemplares = findViewById(R.id.listaDetallePrestamo);

        adaptador = new EjemplarAdaptador(this,prestamo.getEjemplares());
        ejemplares.setAdapter(adaptador);

        ejemplares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               if (prestamo.getEjemplares().isEmpty()){
                   new AlertDialog.Builder(detalle_prestamo.this)
                           .setTitle( "Confirmar")
                           .setMessage( "¿Esta seguro de devolver este ejemplar?")
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   if(getIntent().getBooleanExtra("tipo",true)){
                                       ListaPrestamoInterno.cambiarEstado(getIntent().getIntExtra("pos", 0), position);
                                       pres = ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0)).getId();
                                       ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0)).getEjemplares().remove(position);
                                   }else {
                                       ListaPrestamo.cambiarEstado(getIntent().getIntExtra("pos", 0), position);
                                       pres = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0)).getId();
                                       ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0)).getEjemplares().remove(position);
                                   }
                                   adaptador.notifyDataSetChanged();
                                   String idPrestamo = prestamo.getEjemplares().get(position).getIdPrestamo();
                                   db.collection("prestamos").document(pres)
                                           .collection("ejemplares")
                                           .document( idPrestamo)
                                           .delete()
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   mConnectedThread.write(prestamo.getEjemplares().get(position).getRfid());
                                                   prestamo.getEjemplares().remove(position);

                                                   Toast.makeText(detalle_prestamo.this, "Ejemplar devuelto", Toast.LENGTH_SHORT).show();
                                                   closeContextMenu();
                                               }
                                           });
                               }
                           })
                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   closeContextMenu();
                               }
                           }).show();
               }

            }
        });

        devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(detalle_prestamo.this)
                        .setTitle( "Confirmar")
                        .setMessage( "¿Esta seguro de devolver todos los ejemplares?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Prestamo prestamo;
                                if(getIntent().getBooleanExtra("tipo",true)){
                                   ListaPrestamoInterno.cambiarEstado(getIntent().getIntExtra("pos", 0));
                                   prestamo = ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0));

                                   ListaPrestamoInterno.getLista().remove(getIntent().getIntExtra("pos", 0));
                                }else {
                                    ListaPrestamo.cambiarEstado(getIntent().getIntExtra("pos", 0));
                                    prestamo = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0));
                                    ListaPrestamo.getLista().remove(getIntent().getIntExtra("pos", 0));
                                }
                                for(Ejemplar ejemplar : prestamo.getEjemplares()){
                                    mConnectedThread.write(ejemplar.getRfid());
                                }
                                db.collection("prestamos").document(prestamo.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(detalle_prestamo.this, "Prestamo devuelto", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeContextMenu();
                            }
                        }).show();
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
        adaptador.notifyDataSetChanged();

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
            Toast.makeText(getBaseContext(), "La conexion fallo, ingrese otra vez a esta ventana", Toast.LENGTH_LONG).show();


        }
        mConnectedThread = new detalle_prestamo.ConnectedThread(btSocket);
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
