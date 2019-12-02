package com.example.ejercicioequiposjugadoresfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ejercicioequiposjugadoresfinal.model.adapters.EquipoAdapter;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MainViewModel viewModel;
    private EquipoAdapter equipoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    public static String myIp ="0.0.0.0";
    private long idEquipo = 0;
    private final String URL = "SettingsUrl";
    private final String idEquipoSelected ="idEquipoSelected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddEquipo.class);
                startActivity(i);
            }
        });

        initComponents();
    }

    private void initComponents() {
        cogerShared();
        recyclerView = findViewById(R.id.recycler_equipos);
        equipoAdapter = new EquipoAdapter(this, new EquipoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Equipo equipo) {
                idEquipo = equipo.getId();
                new CargaViewTask().execute();
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(equipoAdapter);
        recyclerView.setHasFixedSize(true);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveDataEquiposList().observe(this, new Observer<List<Equipo>>() {
            @Override
            public void onChanged(List<Equipo> equipos) {
                equipoAdapter.setData(equipos);
            }
        });
    }

    private void cogerShared() {
        SharedPreferences mySharedUrl = PreferenceManager.getDefaultSharedPreferences(this);
        myIp = mySharedUrl.getString(URL, "0.0.0.0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        cogerShared();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private class CargaViewTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            synchronized (this){
                Intent i = new Intent(MainActivity.this, EquipoSelected.class);
                i.putExtra(idEquipoSelected, idEquipo);
                startActivity(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
