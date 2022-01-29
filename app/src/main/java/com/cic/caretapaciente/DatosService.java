package com.cic.caretapaciente;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DatosService {
    @POST("sistmhospital-web/serviciosweb/persistemedicion")
    Call<Datos> createComment(@Body Datos comment);




   /* @FormUrlEncoded
    @POST("sistmr-web/serviciosweb/persistemedicion")
    Call<Comment> createComment(@Field("idPaciente") int idPaciente, @Field("saturacionOxigeno") double saturacionOxigeno,
                                @Field("temperatura") double temperatura, @Field("capnografia") int capnografia,
                                @Field("frecCardiaca") int frecCardiaca, @Field("frecRespiratoria") int frecRespiratoria,
                                @Field("alerta") boolean alerta, @Field("preArtSistolica") int preArtSistolica,
                                @Field("preArtDiastolica")   int preArtDiastolica);
    @FormUrlEncoded
    @POST("sistmr-web/serviciosweb/persistemedicion")
    Call<Comment> createComment(@FieldMap Map<String, String> fields);*/
}