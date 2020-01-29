package com.example.hatzalahemergencias;

public class Direccion {
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEntre1(String entre1) {
        this.entre1 = entre1;
    }

    public void setEntre2(String entre2) {
        this.entre2 = entre2;
    }

    //constructor
    public Direccion(String direccion, String entre1, String entre2) {
        this.direccion = direccion;
        this.entre1 = entre1;
        this.entre2 = entre2;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEntre1() {
        return entre1;
    }

    public String getEntre2() {
        return entre2;
    }
    public Direccion(){}

    private int idDirecciones;
    private int idUsuario;
    private String direccion;
    private String entre1;
    private String entre2;
    private String etiqueta;
    private double lat;
    private double lon;

    public Direccion(int idUsuario, String direccion, String entre1, String entre2, String etiqueta, double lat, double lon) {
        this.idUsuario = idUsuario;
        this.direccion = direccion;
        this.entre1 = entre1;
        this.entre2 = entre2;
        this.etiqueta = etiqueta;
        this.lat = lat;
        this.lon = lon;
    }

    public int getIdDirecciones() {
        return idDirecciones;
    }

    public void setIdDirecciones(int idDirecciones) {
        this.idDirecciones = idDirecciones;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
