package com.example.prestamos.Clases;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.widget.Toast;

import com.example.prestamos.MainActivity;
import com.example.prestamos.activity_prestamos;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ListaPrestamo {
    Handler bluetoothIn;

    final int handlerState = 0;             //used to identify handler message
    private static BluetoothAdapter btAdapter = null;
    private static BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private static ListaPrestamo.ConnectedThread mConnectedThread;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static ArrayList<Prestamo> prestamos = new ArrayList<Prestamo>();
    public static void agregar(Prestamo l){
        prestamos.add(l);
    }
    public static Prestamo getPrestamo(int pos){
        return prestamos.get(pos);
    }
    public static ArrayList<Prestamo> getLista(){
        return prestamos;
    }
    public static void cambiarEstado(int pos){

        for ( Ejemplar libro: prestamos.get(pos).getEjemplares()) {
            db.collection("libros").document(libro.getIdLibro()).collection("ejemplares").document(libro.getId()).update("estado",true);
            mConnectedThread.write( "."+ libro.getRfid());

        }
    }
    public static void cambiarEstado(int pos, int ejemplar){
            db.collection("libros").document(prestamos.get(pos).getEjemplares().get(ejemplar).getIdLibro()).collection("ejemplares").document(prestamos.get(pos).getEjemplares().get(ejemplar).getId()).update("estado",true);

            //Codigo para mandar los libros prestados al arduino
            mConnectedThread.write( "."+ prestamos.get(pos).getEjemplares().get(ejemplar).getRfid());



    }

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

            }
        }
    }
}
