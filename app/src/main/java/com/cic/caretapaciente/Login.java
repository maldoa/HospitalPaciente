package com.cic.caretapaciente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Login {

    @POST("sistmhospital-web/serviciosweb/validausuario")
    Call<Usuario> login(@Body LoginBody loginBody);

}

