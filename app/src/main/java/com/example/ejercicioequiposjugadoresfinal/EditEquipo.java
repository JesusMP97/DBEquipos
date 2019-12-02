package com.example.ejercicioequiposjugadoresfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditEquipo extends AppCompatActivity {

    private ImageButton ibEscudoEquipo;
    private EditText etNombreEquipo, etCiudadEquipo, etEstadioEquipo, etAforoEquipo;
    private Button btEditarEquipo;

    private MainViewModel viewModel;

    private Equipo equipoAux;
    private long idEquipoAux;
    private String nombreEscudo, url;
    private Uri myUri;

    private final int IMAGEN_SUBIR = 1;
    private final String ID_EQUIPO_SELECTED ="idEquipoSelected";
    private static final String TEAM_IMGS_FOLDER = "upload/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_equipo);

        initComponents();
    }

    private void initComponents() {
        Repository repository = new Repository();
        url = repository.getURL();

        idEquipoAux = getIntent().getLongExtra(ID_EQUIPO_SELECTED, 0);

        viewModel = MainActivity.viewModel;

        etNombreEquipo = findViewById(R.id.etNombreEquipoEdit);
        etCiudadEquipo = findViewById(R.id.etCiudadEquipoEdit);
        etEstadioEquipo = findViewById(R.id.etEstadioEquipoEdit);
        etAforoEquipo = findViewById(R.id.etAforoEquipoEdit);

        ibEscudoEquipo = findViewById(R.id.ibEditarEquipo);
        btEditarEquipo = findViewById(R.id.btEditarEquipo);

        initEvents();
    }

    private void initEvents() {
        ibEscudoEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirFoto();
            }
        });

        btEditarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarNuevosCampos();
                alertEditar();
            }
        });

        viewModel.getLiveDataEquipo(idEquipoAux).observe(this, new Observer<Equipo>() {
            @Override
            public void onChanged(Equipo equipo) {
                cargaElementos(equipo);
            }
        });
    }

    private void cargaElementos(Equipo equipo){
        equipoAux = equipo;
        etNombreEquipo.setText(equipoAux.getNombre());
        etCiudadEquipo.setText(equipoAux.getCiudad());
        etEstadioEquipo.setText(equipoAux.getEstadio());
        etAforoEquipo.setText(""+equipoAux.getAforo());
        Glide.with(EditEquipo.this)
                .load(url+TEAM_IMGS_FOLDER+equipoAux.getEscudo())
                .override(500,500)
                .into(ibEscudoEquipo);
    }

    private void asignarNuevosCampos() {
        if (myUri != null){
            saveSelectedImageInFile(myUri);
            equipoAux.setEscudo(nombreEscudo);
        }
        equipoAux.setNombre(etNombreEquipo.getText().toString());
        equipoAux.setCiudad(etCiudadEquipo.getText().toString());
        equipoAux.setEstadio(etEstadioEquipo.getText().toString());
        equipoAux.setAforo(Integer.parseInt(etAforoEquipo.getText().toString()));
        Log.d("EQUIPO", equipoAux.toString());
    }

    private void elegirFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        String[] types = {"image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        startActivityForResult(intent, IMAGEN_SUBIR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEN_SUBIR && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
            myUri = uri;
            Glide.with(this)
                    .load(uri)
                    .override(500, 500)
                    .into(ibEscudoEquipo);
        }
    }

    private void saveSelectedImageInFile(Uri uri) {
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT < 28) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                bitmap = null;
            }
        } else {
            try {
                final InputStream in = this.getContentResolver().openInputStream(uri);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                bitmap = null;
            }
        }
        if(bitmap != null) {
            File file = saveBitmapInFile(bitmap);
            if(file != null) {
                viewModel.uploadImage(file);
            }
        }
    }

    private File saveBitmapInFile(Bitmap bitmap) {
        nombreEscudo = generarNombre(10)+".jpg";
        File file = new File(getFilesDir(), nombreEscudo);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            file = null;
        }
        return file;
    }

    private String generarNombre(int i) {
        String cadena ="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        String nombre ="";
        int len = cadena.length()-1, aux;
        for (int e= 0; e<i; e++){
            aux = (int) (Math.random()*len);
            nombre = nombre + cadena.substring(aux, aux+1);
        }
        return nombre;
    }

    private void alertEditar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.editarEquipoCheck)
                .setTitle(R.string.editarEquipoTitle)
                .setPositiveButton(R.string.editarEquipoSi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.actualizarEquipo(idEquipoAux, equipoAux);
                        new CargaViewTask().execute();
                    }
                })
                .setNegativeButton(R.string.editarEquipoNo, new DialogInterface.OnClickListener() {
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
                Intent i = new Intent(EditEquipo.this, MainActivity.class);
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

