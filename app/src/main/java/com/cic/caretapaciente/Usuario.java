package com.cic.caretapaciente;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("Respuesta")
    private int respuesta;

    @SerializedName("Mensaje")
    private String mensaje;

    @SerializedName("Error")
    private String error;

    @SerializedName("idPaciente")
    private int id;



public Usuario(int respuesta, String mensaje, String error,int id){
    this.respuesta = respuesta;
    this.mensaje = mensaje;
    this.error = error;
    this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getError() {
        return error;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }
}
