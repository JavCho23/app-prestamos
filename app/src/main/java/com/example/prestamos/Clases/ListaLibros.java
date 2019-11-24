package com.example.prestamos.Clases;

import java.util.ArrayList;

public class ListaLibros {
    private static ArrayList<Libro> libros = new ArrayList<Libro>();
    public static void agregar(Libro l){
        libros.add(l);
    }
    public static void eliminar(int pos){
        libros.remove(pos);
    }
    public static Libro getLibro(int pos){
       return libros.get(pos);
    }
    public static ArrayList<Libro> getLista(){
        return libros;
    }
}
