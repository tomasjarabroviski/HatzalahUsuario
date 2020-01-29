package com.example.hatzalahemergencias;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi
{
    @GET("direcciones")
    Call<List<Direccion>> getDireccionesAbm(@Query("idUsuario") int userid);


    @POST("direcciones")
    Call<Direccion> createDireccion(@Body Direccion direcciones_abm);


    @GET("direcciones/{id}")
    Call<Direccion> updateDireccion1(@Path("id") int idDireccion);

    @PUT("direcciones/{id}")
    Call<Direccion> updateDireccion2(@Path("id") int idDireccion, @Body Direccion midireccion);


    @DELETE("direcciones/{id}")
    Call<Void> deleteDireccion(@Path("id") int idDireccion);

    @POST("usuario")
    Call<UsuarioInsert> createUsuario(@Body UsuarioInsert usuarioInsert);

    @POST("fichamedica")
    Call<FichaMedica> createFicha(@Body FichaMedica fichaMedica);



    @GET("usuario/{id}")
    Call<UsuarioInsert> ObtenerUsuario(@Path("id") int idUsuario);

    @GET("usuario/{id}")
    Call<UsuarioInsert> getUsuarioUno(@Path("id") int idUsuario);

    @PUT("usuario/{id}")
    Call<Usuario> updateUsuario(@Path("id") int idUsuario, @Body UsuarioInsert MiUsuario);


    @GET("usuario")
    Call<List<UsuarioInsert>> ObtenerUsuarioPorDNI(@Query("dniUsuario") int dni);

    @POST("circulo")
    Call<List<Usuario>> createCirculo(@Body CirculoFamiliar circuloFamiliar);

    @GET("circulo")
    Call<List<CirculoFamiliar>> getCirculosFamiliaresporId(@Query("idUsuario") int userid);


    @GET("fichamedica")
    Call<List<FichaMedica>> getFiechaMedicaporId(@Query("idUsuario") int userid);

    @PUT("fichamedica/{id}")
    Call<FichaMedica> updateFichaMedica(@Path("id") int idFichaMedica, @Body FichaMedica fichaMedica);

    @GET("circulo")
    Call<List<CirculoFamiliar>> getCirculofamiliarporIdyFamiliar(
            @Query("idUsuario") int userid,
            @Query("idFamiliar") int familiarid

    );

    @DELETE("circulo/{id}")
    Call<List<UsuarioInsert>> deleteDCirculo(@Path("id") int idCirculo);

    @GET("rescatistas/{id}")
    Call<Rescatista> getRescaistaUno(@Path("id") int idRescatista);

}
