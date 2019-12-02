package com.example.ejercicioequiposjugadoresfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;

public class EquipoSelected extends AppCompatActivity {

    private ImageView ivEquipoSelected;
    private TextView tvNombreEquipoSelected, tvCiudadEquipoSelected, tvEstadioEquipoSelected, tvAforoEquipoSelected;
    private Button btEditarEquipoSelected, btBorrarEquipoSelected, btVerJugadores;

    private String url;
    private Intent iGetEquipo;
    private long idEquipoAux;
    private Equipo equipoAux;

    private final String ID_EQUIPO_SELECTED ="idEquipoSelected";
    private static final String TEAM_IMGS_FOLDER = "upload/";

    private MainViewModel viewModel;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo_selected);

        initComponents();
    }

    private void initComponents() {

        repository = new Repository();
        url = repository.getURL();

        viewModel = MainActivity.viewModel;

        iGetEquipo = getIntent();
        idEquipoAux = iGetEquipo.getLongExtra(ID_EQUIPO_SELECTED, 0);

        ivEquipoSelected = findViewById(R.id.ivEquipoSelected);

        tvNombreEquipoSelected = findViewById(R.id.tvNombreEquipoSelected);
        tvCiudadEquipoSelected = findViewById(R.id.tvCiudadEquipoSelected);
        tvEstadioEquipoSelected = findViewById(R.id.tvEstadioEquipoSelected);
        tvAforoEquipoSelected = findViewById(R.id.tvAforoEquipoSelected);

        btEditarEquipoSelected = findViewById(R.id.btEditarEquipoSelected);
        btBorrarEquipoSelected = findViewById(R.id.btBorrarEquipoSelected);
        btVerJugadores = findViewById(R.id.btVerJugadoresSelected);

        initEvents();
    }

    private void initEvents() {
        viewModel.getLiveDataEquipo(idEquipoAux).observe(this, new Observer<Equipo>() {
            @Override
            public void onChanged(Equipo equipo) {
                equipoAux = new Equipo();
                equipoAux = equipo;
                asignarCamposEquipo(equipoAux);
            }
        });

        btEditarEquipoSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CargaViewTask().execute();
            }
        });

        btBorrarEquipoSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBorrar();
            }
        });

        btVerJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EquipoSelected.this, VerJugadores.class);
                i.putExtra(ID_EQUIPO_SELECTED, idEquipoAux);
                startActivity(i);
            }
        });


    }

    private void asignarCamposEquipo(Equipo equipo) {
        tvNombreEquipoSelected.setText(getResources().getString(R.string.nombreE)+" "+equipo.getNombre());
        tvCiudadEquipoSelected.setText(getResources().getString(R.string.ciudadE)+" "+equipo.getCiudad());
        tvEstadioEquipoSelected.setText(getResources().getString(R.string.estadioE)+" "+equipo.getEstadio());
        tvAforoEquipoSelected.setText(getResources().getString(R.string.aforoE)+" "+equipo.getAforo());
        Glide.with(this)
                .load(url+TEAM_IMGS_FOLDER+equipo.getEscudo())
                .override(500, 500)
                .into(ivEquipoSelected);
    }

    private void alertBorrar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.borrarEquipoCheck)
                .setTitle(R.string.borrarEquipoTitle)
                .setPositiveButton(R.string.borrarEquipoSi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.borrarEquipo(idEquipoAux);
                        Intent i = new Intent(EquipoSelected.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.borrarEquipoNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class CargaViewTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            synchronized (this){
                Intent i = new Intent(EquipoSelected.this, EditEquipo.class);
                i.putExtra(ID_EQUIPO_SELECTED, idEquipoAux);
                startActivity(i);
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
