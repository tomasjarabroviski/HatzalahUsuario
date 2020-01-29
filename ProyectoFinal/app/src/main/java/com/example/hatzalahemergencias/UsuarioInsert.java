package com.example.hatzalahemergencias;

public class UsuarioInsert {

    private int idUsuario;

    private String dniusuario;

    private String nombreUsuario;

    private String apellidoUsuario;

    private String telefonoUsuario;

    private String mailUsuario;

    private String contrasenaUsuario;

    private int alta;

    public UsuarioInsert() {

    }

    private String fotoUsuario;

    private String fechaNacimientoUsuario;

    public UsuarioInsert(String dniusuario, String nombreUsuario, String apellidoUsuario, String telefonoUsuario, String mailUsuario, String contrasenaUsuario, int alta, String fotoUsuario, String fechaNacimientoUsuario) {
        this.dniusuario = dniusuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.mailUsuario = mailUsuario;
        this.contrasenaUsuario = contrasenaUsuario;
        this.alta = alta;
        this.fotoUsuario = fotoUsuario;
        this.fechaNacimientoUsuario = fechaNacimientoUsuario;
    }

    public String getDniusuario() {
        return dniusuario;
    }

    public void setDniusuario(String dniusuario) {
        this.dniusuario = dniusuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getMailUsuario() {
        return mailUsuario;
    }

    public void setMailUsuario(String mailUsuario) {
        this.mailUsuario = mailUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public int getAlta() {
        return alta;
    }

    public void setAlta(int alta) {
        this.alta = alta;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getFechaNacimientoUsuario() {
        return fechaNacimientoUsuario;
    }

    public void setFechaNacimientoUsuario(String fechaNacimientoUsuario) {
        this.fechaNacimientoUsuario = fechaNacimientoUsuario;
    }
}
