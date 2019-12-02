package com.example.ejercicioequiposjugadoresfinal.model.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ejercicioequiposjugadoresfinal.MainActivity;
import com.example.ejercicioequiposjugadoresfinal.model.interfaces.RetrieveData;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private final String SUCCEED_TAG = "SUCCEED", ERROR_TAG="ERROR";
    private String myUrl;
    private RetrieveData api;

    private ArrayList<Equipo> listaEquipos =new ArrayList<>();
    private ArrayList<Jugador> listaJugadores =new ArrayList<>();

    private MutableLiveData<List<Equipo>> mutableEquiposList =new MutableLiveData<>();
    private MutableLiveData<Equipo> mutableEquipo = new MutableLiveData<>();

    private MutableLiveData<List<Jugador>> mutableJugadoresList =new MutableLiveData<>();
    private MutableLiveData<Jugador> mutableJugador = new MutableLiveData<>();

    public Repository(){
        myUrl = "http://"+MainActivity.myIp +"/web/Equipos/public/";
        recoverApi();
        retrieveEquiposList();
        retrieveJugadoresList();
    }

    private void recoverApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(myUrl+"api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(RetrieveData.class);
    }

    public void retrieveEquiposList(){
        Call<ArrayList<Equipo>> callEquipos = api.getEquipos();
        callEquipos.enqueue(new Callback<ArrayList<Equipo>>() {
            @Override
            public void onResponse(Call<ArrayList<Equipo>> call, Response<ArrayList<Equipo>> response) {
                Log.d(SUCCEED_TAG, response.body().toString());
                listaEquipos = response.body();
                mutableEquiposList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Equipo>> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void retrieveJugadoresList(){
        Call<ArrayList<Jugador>> callJugadores = api.getJugadores();
        callJugadores.enqueue(new Callback<ArrayList<Jugador>>() {
            @Override
            public void onResponse(Call<ArrayList<Jugador>> call, Response<ArrayList<Jugador>> response) {
                Log.d(SUCCEED_TAG, response.body().toString());
                listaJugadores = response.body();
                mutableJugadoresList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Jugador>> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void añadirEquipo(Equipo equipo){
        Call<Long> callAñadirEquipo = api.postEquipo(equipo);
        callAñadirEquipo.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Log.d(SUCCEED_TAG, response.body().toString());
                if(response.body() > 0){
                    retrieveEquiposList();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void añadirJugador(Jugador jugador){
        Call<Long> callAñadirJugador = api.postJugador(jugador);
        callAñadirJugador.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Log.d(SUCCEED_TAG, response.body().toString());
                if(response.body() != 0){
                    retrieveJugadoresList();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void borrarEquipo(long id){
        Call<Integer> callBorrarEquipo = api.deleteEquipo(id);
        callBorrarEquipo.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                retrieveEquiposList();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void borrarJugador(long id){
        Call<Integer> callBorrarJugador = api.deleteJugador(id);
        callBorrarJugador.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                retrieveJugadoresList();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void actualizarEquipo(long id, Equipo equipo){
        Call<Equipo> callActualizarEquipo = api.putEquipo(id, equipo);
        callActualizarEquipo.enqueue(new Callback<Equipo>() {
            @Override
            public void onResponse(Call<Equipo> call, Response<Equipo> response) {
                retrieveEquiposList();
            }

            @Override
            public void onFailure(Call<Equipo> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void actualizarJugador(long id, Jugador jugador){
        Call<Boolean> call = api.putJugador(id, jugador);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                retrieveJugadoresList();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void getSingleTeam(long id){
        Call<Equipo> callGetOneTeam = api.getEquipo(id);
        callGetOneTeam.enqueue(new Callback<Equipo>() {
            @Override
            public void onResponse(Call<Equipo> call, Response<Equipo> response) {
                Log.d(SUCCEED_TAG, response.body().toString());
                mutableEquipo.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Equipo> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void getSinglePlayer(long id){
        Call<Jugador> callGetOnePlayer = api.getJugador(id);
        callGetOnePlayer.enqueue(new Callback<Jugador>() {
            @Override
            public void onResponse(Call<Jugador> call, Response<Jugador> response) {
                Log.d(SUCCEED_TAG, response.body().toString());
                mutableJugador.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Jugador> call, Throwable t) {
                Log.e(ERROR_TAG, t.getMessage());
            }
        });
    }

    public void uploadImage(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part request = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<String> call = api.uploadImage(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v(SUCCEED_TAG, response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v(ERROR_TAG, t.getLocalizedMessage());
            }
        });
    }

    public void setUrl(String url){
        this.myUrl = "http://"+url+"/web/equipo/public/";
    }

    public String getURL(){
        return this.myUrl;
    }

    public List<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    public List<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public LiveData<List<Equipo>> getLiveDataEquiposList(){
        return mutableEquiposList;
    }

    public LiveData<List<Jugador>> getLiveDataJugadoresList(){
        return mutableJugadoresList;
    }

    public LiveData<Equipo> getLiveDataEquipo(long id) {
        getSingleTeam(id);
        return mutableEquipo;
    }

    public LiveData<Jugador> getLiveDataJugador(long id) {
        getSinglePlayer(id);
        return mutableJugador;
    }
}
