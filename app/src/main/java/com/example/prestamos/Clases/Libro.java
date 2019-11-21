package com.example.prestamos.Clases;

import android.content.Intent;

public class Libro {
    private String titulo;
    private int numero;
    private String dewey;
    private String cantidad;
    private String autores;
    private String anioPublicacion;
    private String programaEstudio;

    public Libro() {
    }

    public Libro(String titulo, String numero, String dewey, String cantidad, String autores, String anioPublicacion, String programaEstudio) {
        this.titulo = titulo;
        this.numero = Integer.parseInt(numero);
        this.dewey = dewey;
        this.cantidad = cantidad;
        this.autores = autores;
        this.anioPublicacion = anioPublicacion;
        this.programaEstudio = programaEstudio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = Integer.parseInt(numero);
    }

    public String getDewey() {
        return dewey;
    }

    public void setDewey(String dewey) {
        this.dewey = dewey;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(String anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getProgramaEstudio() {
        return programaEstudio;
    }

    public void setProgramaEstudio(String programaEstudio) {
        this.programaEstudio = programaEstudio;
    }
}
