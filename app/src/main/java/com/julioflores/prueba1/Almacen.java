package com.julioflores.prueba1;

public class Almacen {
    private int id, rack, fila, columna, lotemp, cantidad;
    private String materiaprima, persona, observaciones, fechahora;

    public Almacen(){ }

    public Almacen(int id, int rack, int fila, int columna, int lotemp, int cantidad, String materiaprima, String persona, String observaciones, String fechahora) {
        this.id = id;
        this.rack = rack;
        this.fila = fila;
        this.columna = columna;
        this.lotemp = lotemp;
        this.cantidad = cantidad;
        this.materiaprima = materiaprima;
        this.persona = persona;
        this.observaciones = observaciones;
        this.fechahora = fechahora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRack() {
        return rack;
    }

    public void setRack(int rack) {
        this.rack = rack;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getLotemp() {
        return lotemp;
    }

    public void setLotemp(int lotemp) {
        this.lotemp = lotemp;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMateriaprima() {
        return materiaprima;
    }

    public void setMateriaprima(String materiaprima) {
        this.materiaprima = materiaprima;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    @Override
    public String toString() {
        return id + " " + rack + fila + columna + " " + materiaprima+  " " + lotemp + " " + cantidad + " " + persona + observaciones + fechahora;
    }
}
