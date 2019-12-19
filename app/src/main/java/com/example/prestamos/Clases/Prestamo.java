package com.example.prestamos.Clases;

import java.util.ArrayList;

public class Prestamo {
    private String id;
    private String correo;
    private String dni;
    private String fecha;
    private String nombre;
    private String procedencia;
    private String telefono;
    private boolean tipo; // true interno
    private ArrayList<Ejemplar> ejemplares;

    public Prestamo() {
    }

    public Prestamo(String correo, String dni, String fecha, String nombre, String procedencia, String telefono, boolean tipo) {
        this.correo = correo;
        this.dni = dni;
        this.fecha = fecha;
        this.nombre = nombre;
        this.procedencia = procedencia;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public ArrayList<Ejemplar> getEjemplares() {
        return ejemplares;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEjemplares(ArrayList<Ejemplar> ejemplares) {
        this.ejemplares = ejemplares;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
