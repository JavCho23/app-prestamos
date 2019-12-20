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

        }
    }
    public static void cambiarEstado(int pos, int ejemplar){
            db.collection("libros").document(prestamos.get(pos).getEjemplares().get(ejemplar).getIdLibro()).collection("ejemplares").document(prestamos.get(pos).getEjemplares().get(ejemplar).getId()).update("estado",true);

            //Codigo para mandar los libros prestados al arduino



    }


}
