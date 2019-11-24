package com.example.prestamos.Clases;

public class Ejemplar {
    private String rfid;
    private boolean estado;
    private String titulo;
    private String idLibro;
    private String id;

    public Ejemplar() {
    }

    public Ejemplar(String rfid, boolean estado, String titulo, String idLibro, String id) {
        this.rfid = rfid;
        this.estado = estado;
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
