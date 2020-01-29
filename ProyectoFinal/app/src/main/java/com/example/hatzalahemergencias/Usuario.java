package com.example.hatzalahemergencias;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public class Usuario {
    private int idUsuario;
    private int dniUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private ArrayList<Direccion> direccionesUsuario;
    private Long telefonoUsuario;
    private String mailUsuario;
    private boolean familiar;
    private int alta;
    private Date fechaNacimiento;
    private String fotoUsuario;
    private String contrasena;

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isFamiliar() {
        return familiar;
    }

    public void setFamiliar(boolean familiar) {
        this.familiar = familiar;
    }

    public Long getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(Long telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getMailUsuario() {
        return mailUsuario;
    }

    public void setMailUsuario(String mailUsuario) {
        this.mailUsuario = mailUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ArrayList<Direccion> getDireccionesUsuario() {
        return direccionesUsuario;
    }

    public void setDireccionesUsuario(ArrayList<Direccion> direccionesUsuario) {
        this.direccionesUsuario = direccionesUsuario;
    }

    public void setDniUsuario(int dniUsuario) {
        this.dniUsuario = dniUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getDniUsuario() {
        return dniUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public Usuario() {
    }

    public Usuario(int dniUsuario, String nombreUsuario, String apellidoUsuario, Long telefonoUsuario, String mailUsuario, int alta, Date fechaNacimiento, String fotoUsuario) {
        this.dniUsuario = dniUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.mailUsuario = mailUsuario;
        this.alta = 1;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoUsuario = "aa";
    }
}
