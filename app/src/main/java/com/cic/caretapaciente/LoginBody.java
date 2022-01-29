package com.cic.caretapaciente;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("idusuario")
    private String id;

    @SerializedName("cifra")
    private String contrasenia;


    public LoginBody (String id, String contrasenia){
        this.id = id;
        this.contrasenia = contrasenia;
    }

    public String getId() {
        return id;
    }

    public String getContrasenia() {
        return contrasenia;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }


}
