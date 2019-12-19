package com.example.prestamos.Clases;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListaPrestamoInterno {
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
            db.collection("libros").document(libro.getIdLibro()).collection("ejemplares").document(libro.getId()).update("estado",libro.isEstado()?false:true);

        }
    }
    public static void cambiarEstado(int pos, int ejemplar){
        db.collection("libros").document(prestamos.get(pos).getEjemplares().get(ejemplar).getIdLibro()).collection("ejemplares").document(prestamos.get(pos).getEjemplares().get(ejemplar).getId()).update("estado",prestamos.get(pos).getEjemplares().get(ejemplar).isEstado()?false:true);


    }
}
