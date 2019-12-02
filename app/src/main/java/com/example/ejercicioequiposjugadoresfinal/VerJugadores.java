package com.example.ejercicioequiposjugadoresfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.ejercicioequiposjugadoresfinal.model.adapters.JugadorAdapter;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VerJugadores extends AppCompatActivity {

    public static MainViewModel viewModel;
    private JugadorAdapter jugadorAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerJugadores;
    private Repository repository;

    private long idEquipo;
    private String myUrl;

    private final String URL = "SettingsUrl";
    private final String ID_EQUIPO_SELECTED = "idEquipoSelected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_jugadores);

        FloatingActionButton fab = findViewById(R.id.fabAñadirJugadores);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerJugadores.this, AddJugador.class);
                i.putExtra(ID_EQUIPO_SELECTED, idEquipo);
                startActivity(i);
            }
        });

        initComponents();
    }

    private void initComponents() {

        repository = new Repository();
        SharedPreferences mySharedUrl = PreferenceManager.getDefaultSharedPreferences(this);
        myUrl = mySharedUrl.getString(URL, "0.0.0.0");
        repository.setUrl(myUrl);

        idEquipo = getIntent().getLongExtra(ID_EQUIPO_SELECTED, 0);

        recyclerJugadores = findViewById(R.id.recyclerJugadores);

        jugadorAdapter = new JugadorAdapter(this, idEquipo);
        layoutManager = new LinearLayoutManager(this);
        recyclerJugadores.setLayoutManager(layoutManager);
        recyclerJugadores.setAdapter(jugadorAdapter);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveDataJugadoresList().observe(this, new Observer<List<Jugador>>() {
            @Override
            public void onChanged(List<Jugador> jugadores) {
                ArrayList<Jugador> jugadoresAux = new ArrayList<>();
                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i).getIdequipo() == idEquipo) {
                        jugadoresAux.add(jugadores.get(i));
                    }
                }
                jugadorAdapter.setData(jugadoresAux);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mySharedUrl = PreferenceManager.getDefaultSharedPreferences(this);
        myUrl = mySharedUrl.getString(URL, "0.0.0.0");
        repository.setUrl(myUrl);
    }

}
