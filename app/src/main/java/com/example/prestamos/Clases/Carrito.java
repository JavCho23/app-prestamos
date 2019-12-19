package com.example.prestamos.Clases;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class Carrito {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static ArrayList<Ejemplar> libros = new ArrayList<>();
        public static void agregar(Ejemplar e){
            libros.add(e);
        }
        public static void eliminar(Ejemplar E){
            libros.remove(E);
        }
        public static Ejemplar getEjemplar(int pos){
            return libros.get(pos);
        }
        public static ArrayList<Ejemplar> getLista(){
            return libros;
        }
        public static void cambiarEstado(){
            for ( Ejemplar libro: libros) {
                db.collection("libros").document(libro.getIdLibro()).collection("ejemplares").document(libro.getId()).update("estado",libro.isEstado()?false:true);

            }
        }
        public static void limpiar(){
            libros.clear();
        }
}
