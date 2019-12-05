package com.example.prestamos.Clases;

import java.util.ArrayList;

public class ListaPrestamo {
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
}
