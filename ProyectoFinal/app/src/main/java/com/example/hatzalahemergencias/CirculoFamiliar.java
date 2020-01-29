package com.example.hatzalahemergencias;

import com.google.gson.annotations.SerializedName;

public class CirculoFamiliar {
    private int idCirculoFamiliar;
    private int idUsuario;
    private int idFamiliar;
    private String color;

    public CirculoFamiliar(int idUsuario, int idFamiliar, String color) {
        this.idUsuario = idUsuario;
        this.idFamiliar = idFamiliar;
        this.color = color;
    }

    public int getIdCirculoFamiliar() {
        return idCirculoFamiliar;
    }

    public void setIdCirculoFamiliar(int idCirculoFamiliar) {
        this.idCirculoFamiliar = idCirculoFamiliar;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdFamiliar() {
        return idFamiliar;
    }

    public void setIdFamiliar(int idFamiliar) {
        this.idFamiliar = idFamiliar;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
