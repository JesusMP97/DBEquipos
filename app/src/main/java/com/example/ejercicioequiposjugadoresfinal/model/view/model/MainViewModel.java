package com.example.ejercicioequiposjugadoresfinal.model.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;

import java.io.File;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Repository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    //RECUPERAR URL

    public String getURL(){
        return repository.getURL();
    }

    //RECUPERAR EQUIPOS Y JUGADORES

    public LiveData<Equipo> getLiveDataEquipo(long id){
        return repository.getLiveDataEquipo(id);
    }

    public LiveData<List<Equipo>> getLiveDataEquiposList(){
        return repository.getLiveDataEquiposList();
    }

    public LiveData<Jugador> getLiveDataJugador(long id){
        return repository.getLiveDataJugador(id);
    }

    public LiveData<List<Jugador>> getLiveDataJugadoresList(){
        return repository.getLiveDataJugadoresList();
    }

    //AÑADIR EQUIPO Y JUGADOR

    public void añadirEquipo(Equipo equipo){
        repository.añadirEquipo(equipo);
    }

    public void añadirJugador(Jugador jugador){
        repository.añadirJugador(jugador);
    }

    //BORRAR EQUIPO Y JUGADOR

    public void borrarEquipo(long id){
        repository.borrarEquipo(id);
    }

    public void borrarJugador(long id){
        repository.borrarJugador(id);
    }

    //ACTUALIZAR EQUIPO Y JUGADOR

    public void actualizarEquipo(long id, Equipo equipo){
        repository.actualizarEquipo(id, equipo);
    }

    public void actualizarJugador(long id, Jugador jugador){
        repository.actualizarJugador(id, jugador);
    }

    //SUBIR IMAGEN

    public void uploadImage(File file){
        repository.uploadImage(file);
    }
}
