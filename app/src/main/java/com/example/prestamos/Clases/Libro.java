package com.example.prestamos.Clases;


public class Libro {
    private String titulo;
    private double numero;
    private String dewey;
    private double cantidad;
    private String autores;
    private String anioPublicacion;
    private String programaEstudio;
    private int prestados;

    public Libro() {
    }

    public Libro(String titulo, double numero, String dewey, double cantidad, String autores, String anioPublicacion, String programaEstudio, int prestados) {
        this.titulo = titulo;
        this.numero = numero;
        this.dewey = dewey;
        this.cantidad = cantidad;
        this.autores = autores;
        this.anioPublicacion = anioPublicacion;
        this.programaEstudio = programaEstudio;
        this.prestados = prestados;
    }

    public double getNumero() {
        return numero;
    }

    public void setNumero(double numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getDewey() {
        return dewey;
    }

    public void setDewey(String dewey) {
        this.dewey = dewey;
    }


    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrestados() {
        return prestados;
    }

    public void setPrestados(int prestados) {
        this.prestados = prestados;
    }

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
