package com.cic.caretapaciente;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datos implements Serializable {
    @SerializedName("idPaciente")
    public int idPaciente;

    @SerializedName("saturacionOxigeno")
    public double saturacionOxigeno;

    @SerializedName("temperatura")
    public double temperatura;

    @SerializedName("capnografia")
    public int capnografia;


    @SerializedName("frecCardiaca")
    public int frecCardiaca;

    @SerializedName("frecRespiratoria")
    public int frecRespiratoria;

    @SerializedName("alerta")
    public boolean alerta;

    @SerializedName("preArtSistolica")
    public int preArtSistolica;

    @SerializedName("preArtDiastolica")
    public int preArtDiastolica;

    public Datos(int idPaciente, double saturacionOxigeno, double temperatura,  int capnografia, int frecCardiaca, int frecRespiratoria, boolean alerta, int preArtSistolica, int preArtDiastolica) {
        this.idPaciente = idPaciente;
        this.saturacionOxigeno = saturacionOxigeno;
        this.temperatura = temperatura;
        this.capnografia = capnografia;
        this.frecCardiaca = frecCardiaca;
        this.frecRespiratoria = frecRespiratoria;
        this.alerta = alerta;
        this.preArtSistolica = preArtSistolica;
        this.preArtDiastolica = preArtDiastolica;

    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public double getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public int getCapnografia() {
        return capnografia;
    }

    public int getFrecCardiaca() {
        return frecCardiaca;
    }

    public int getFrecRespiratoria() {
        return frecRespiratoria;
    }

    public boolean isAlerta() {
        return alerta;
    }

    public int getPreArtDiastolica() {
        return preArtDiastolica;
    }

    public int getPreArtSistolica() {
        return preArtSistolica;
    }


}
