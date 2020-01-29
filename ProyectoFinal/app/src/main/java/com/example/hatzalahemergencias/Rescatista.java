package com.example.hatzalahemergencias;

public class Rescatista {
    private int idRescatista;
    private double dniRescatista;
    private String nombreRescatista;
    private String apellidoRescatista;
    private Long telefonoRescatista;
    private String direccionRescatista;
    private String contrasenaRescatista;
    private String ultCapTem;
    private String ultCapTora;
    private String ultCapCruzRoja;
    private int online;
    private String proximaCap;
    private String fotoRescatista;
    private String mailRescatista;
    private String fechaNacimientoRescatista;


    public Rescatista(double dniRescatista, String nombreRescatista, String apellidoRescatista, Long telefonoRescatista, String direccionRescatista, String contrasenaRescatista, String ultCapTem, String ultCapTora, String ultCapCruzRoja, int online, double ubicacionLat, double ubicacionLon, String proximaCap, String fotoRescatista, String mailRescatista, String fechaNacimientoRescatista) {
        this.dniRescatista = dniRescatista;
        this.nombreRescatista = nombreRescatista;
        this.apellidoRescatista = apellidoRescatista;
        this.telefonoRescatista = telefonoRescatista;
        this.direccionRescatista = direccionRescatista;
        this.contrasenaRescatista = contrasenaRescatista;
        this.ultCapTem = ultCapTem;
        this.ultCapTora = ultCapTora;
        this.ultCapCruzRoja = ultCapCruzRoja;
        this.online = online;
        this.proximaCap = proximaCap;
        this.fotoRescatista = fotoRescatista;
        this.mailRescatista = mailRescatista;
        this.fechaNacimientoRescatista = fechaNacimientoRescatista;
    }

    public int getIdRescatista() {
        return idRescatista;
    }

    public void setIdRescatista(int idRescatista) {
        this.idRescatista = idRescatista;
    }

    public double getDniRescatista() {
        return dniRescatista;
    }

    public void setDniRescatista(double dniRescatista) {
        this.dniRescatista = dniRescatista;
    }

    public String getNombreRescatista() {
        return nombreRescatista;
    }

    public void setNombreRescatista(String nombreRescatista) {
        this.nombreRescatista = nombreRescatista;
    }

    public String getApellidoRescatista() {
        return apellidoRescatista;
    }

    public void setApellidoRescatista(String apellidoRescatista) {
        this.apellidoRescatista = apellidoRescatista;
    }

    public Long getTelefonoRescatista() {
        return telefonoRescatista;
    }

    public void setTelefonoRescatista(Long telefonoRescatista) {
        this.telefonoRescatista = telefonoRescatista;
    }

    public String getDireccionRescatista() {
        return direccionRescatista;
    }

    public void setDireccionRescatista(String direccionRescatista) {
        this.direccionRescatista = direccionRescatista;
    }

    public String getContrasenaRescatista() {
        return contrasenaRescatista;
    }

    public void setContrasenaRescatista(String contrasenaRescatista) {
        this.contrasenaRescatista = contrasenaRescatista;
    }

    public String getUltCapTem() {
        return ultCapTem;
    }

    public void setUltCapTem(String ultCapTem) {
        this.ultCapTem = ultCapTem;
    }

    public String getUltCapTora() {
        return ultCapTora;
    }

    public void setUltCapTora(String ultCapTora) {
        this.ultCapTora = ultCapTora;
    }

    public String getUltCapCruzRoja() {
        return ultCapCruzRoja;
    }

    public void setUltCapCruzRoja(String ultCapCruzRoja) {
        this.ultCapCruzRoja = ultCapCruzRoja;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getProximaCap() {
        return proximaCap;
    }

    public void setProximaCap(String proximaCap) {
        this.proximaCap = proximaCap;
    }

    public String getFotoRescatista() {
        return fotoRescatista;
    }

    public void setFotoRescatista(String fotoRescatista) {
        this.fotoRescatista = fotoRescatista;
    }

    public String getMailRescatista() {
        return mailRescatista;
    }

    public void setMailRescatista(String mailRescatista) {
        this.mailRescatista = mailRescatista;
    }

    public String getFechaNacimientoRescatista() {
        return fechaNacimientoRescatista;
    }

    public void setFechaNacimientoRescatista(String fechaNacimientoRescatista) {
        this.fechaNacimientoRescatista = fechaNacimientoRescatista;
    }
}