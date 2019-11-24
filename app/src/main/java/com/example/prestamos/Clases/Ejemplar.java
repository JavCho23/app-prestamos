package com.example.prestamos.Clases;

public class Ejemplar {
    private String rfid;
    private boolean estado;

    public Ejemplar() {
    }

    public Ejemplar(String rfid, boolean estado) {
        this.rfid = rfid;
        this.estado = estado;
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
