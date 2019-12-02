package com.example.ejercicioequiposjugadoresfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditJugador extends AppCompatActivity {

    private final int IMAGEN_SUBIR = 1;
    private static final String JUG_IMGS_FOLDER = "upload/";
    private static final String ID_JUGADOR = "idJugadorSelected";
    private static final String ID_EQUIPO = "idEquipoSelected";

    private ImageButton ibFotoJugador;
    private EditText etNombreJugador, etApellidosJugador;
    private Button btEditarJugador;

    private MainViewModel viewModel;
    private long idJugadorSelected, idEquipoSelected;

    private Jugador jugadorAux;
    private String nombreFoto, url;
    private Uri myUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jugador);

        initComponents();
    }

    private void initComponents() {
        Repository repository = new Repository();
        url = repository.getURL();

        idJugadorSelected = getIntent().getLongExtra(ID_JUGADOR, 0);
        idEquipoSelected = getIntent().getLongExtra(ID_EQUIPO, 0);

        viewModel = VerJugadores.viewModel;

        etNombreJugador = findViewById(R.id.etNombreJugadorEdit);
        etApellidosJugador = findViewById(R.id.etApellidosJugadorEdit);

        ibFotoJugador = findViewById(R.id.ibEditarJugador);
        btEditarJugador = findViewById(R.id.btEditarJugador);

        initEvents();
    }

    private void initEvents() {
        ibFotoJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirFoto();
            }
        });

        viewModel.getLiveDataJugador(idJugadorSelected).observe(this, new Observer<Jugador>() {
            @Override
            public void onChanged(Jugador jugador) {
                cargaElementos(jugador);
            }
        });

        btEditarJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignarNuevosCampos();
                viewModel.actualizarJugador(idJugadorSelected, jugadorAux);
                finish();
            }
        });
    }

    private void asignarNuevosCampos() {
        if (myUri != null){
            saveSelectedImageInFile(myUri);
            jugadorAux.setFoto(nombreFoto);
        }
        jugadorAux.setNombre(etNombreJugador.getText().toString());
        jugadorAux.setApellidos(etApellidosJugador.getText().toString());
        jugadorAux.setIdequipo(idEquipoSelected);
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
        nombreFoto = generarNombre(10)+".jpg";
        File file = new File(getFilesDir(), nombreFoto);
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
        String cadena ="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
        String nombre ="";
        int len = cadena.length()-1, aux;
        for (int e= 0; e<i; e++){
            aux = (int) (Math.random()*len);
            nombre = nombre + cadena.substring(aux, aux+1);
        }
        return nombre;
    }

    private void cargaElementos(Jugador jugador){
        jugadorAux = jugador;
        etNombreJugador.setText(jugadorAux.getNombre());
        etApellidosJugador.setText(jugadorAux.getApellidos());
        Glide.with(this)
                .load(url+JUG_IMGS_FOLDER+jugadorAux.getFoto())
                .override(500,500)
                .into(ibFotoJugador);
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
                    .into(ibFotoJugador);
        }
    }
}
