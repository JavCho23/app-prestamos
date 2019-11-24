package com.example.prestamos.Clases;

import java.util.ArrayList;

public class Carrito {
        private static ArrayList<Ejemplar> libros;
        public void agregar(Ejemplar e){
            libros.add(e);
        }
        public void eliminar(int pos){
            libros.remove(pos);
        }
        public void getEjemplar(int pos){
            libros.get(pos);
        }
}
