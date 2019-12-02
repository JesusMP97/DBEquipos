package com.example.ejercicioequiposjugadoresfinal.model.interfaces;


import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrieveData {

    /* Jugadores Data*/

    @DELETE("jugador/{id}")
    Call<Integer> deleteJugador(@Path("id") long id);

    @GET("jugador/{id}")
    Call<Jugador> getJugador(@Path("id") long id);

    @GET("jugador")
    Call<ArrayList<Jugador>> getJugadores();

    @POST("jugador")
    Call<Long> postJugador(@Body Jugador jugadores);

    /*@PUT("jugadores/{id}")
    Call<Jugador> putJugador(@Path("id") long id, @Body Jugador jugador);*/

    @PUT("jugador/{id}")
    Call<Boolean> putJugador(@Path("id") long id, @Body Jugador jugador);

    /*Equipos Data*/

    @DELETE("equipo/{id}")
    Call<Integer> deleteEquipo(@Path("id") long id);

    @GET("equipo/{id}")
    Call<Equipo> getEquipo(@Path("id") long id);

    @GET("equipo")
    Call<ArrayList<Equipo>> getEquipos();

    @POST("equipo")
    Call<Long> postEquipo(@Body Equipo equipo);

    @PUT("equipo/{id}")
    Call<Equipo> putEquipo(@Path("id") long id, @Body Equipo equipo);

    /* Para subir la imagen*/
    @Multipart
    @POST("upload")
    Call<String> uploadImage(@Part MultipartBody.Part file);
}
