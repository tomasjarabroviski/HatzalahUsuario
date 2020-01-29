package com.example.hatzalahemergencias;

public class FichaMedica {

    private int idFicha;

    private int idUsuario;

    private String tipoDeSangre;

    private String alergias;

    private String med;

    public FichaMedica(int idUsuario, String tipoDeSangre, String alergias, String med) {
        this.idUsuario = idUsuario;
        this.tipoDeSangre = tipoDeSangre;
        this.alergias = alergias;
        this.med = med;
    }

    public int getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(int idFicha) {
        this.idFicha = idFicha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoDeSangre() {
        return tipoDeSangre;
    }

    public void setTipoDeSangre(String tipoDeSangre) {
        this.tipoDeSangre = tipoDeSangre;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }
}
