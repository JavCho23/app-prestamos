package com.example.prestamos.Clases;

import java.util.ArrayList;

public class ListaLibros {
    private static ArrayList<Libro> libros;
    public void agregar(Libro l){
        libros.add(l);
    }
    public void eliminar(int pos){
        libros.remove(pos);
    }
    public void getLibro(int pos){
        libros.get(pos);
    }
}
