package com.example.ejercicioequiposjugadoresfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddJugador extends AppCompatActivity {

    private final int IMAGEN_SUBIR = 1;
    private static final String ID_EQUIPO = "idEquipoSelected";

    private ImageButton ibFotoJugador;
    private EditText etNombreJugador, etApellidosJugador;
    private Button btCrearJugador;
    private TextView tvError;

    private MainViewModel viewModel;

    private Jugador jugadorAux;
    private String nombreFoto, url;
    private Uri myUri;
    private long idEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jugador);

        initComponents();
    }

    private void initComponents() {
        idEquipo = getIntent().getLongExtra(ID_EQUIPO, 0);

        Repository repository = new Repository();
        url = repository.getURL();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        etNombreJugador = findViewById(R.id.etNombreJugadorCrear);
        etApellidosJugador = findViewById(R.id.etApellidosJugadorCrear);
        tvError = findViewById(R.id.tvErrorCrearJugador);

        ibFotoJugador = findViewById(R.id.ibCrearJugador);
        btCrearJugador = findViewById(R.id.btCrearJugador);

        initEvents();
    }

    private void initEvents() {
        ibFotoJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirFoto();
            }
        });

        btCrearJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(haRellenado()){
                    asignarCampos();

                    viewModel.añadirJugador(jugadorAux);
                    Intent i = new Intent(AddJugador.this, VerJugadores.class);
                    i.putExtra(ID_EQUIPO, idEquipo);
                    startActivity(i);
                    btCrearJugador.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean haRellenado() {
        if(etNombreJugador.length() == 0){
            tvError.setText(R.string.errorNombreJ);
        }else if(etApellidosJugador.length() == 0){
            tvError.setText(R.string.errorApellidos);
        }else if(myUri == null){
            tvError.setText(R.string.errorFoto);
        }else{
            return true;
        }
        return false;
    }

    private void asignarCampos() {
        saveSelectedImageInFile(myUri);
        jugadorAux = new Jugador();
        jugadorAux.setNombre(etNombreJugador.getText().toString());
        jugadorAux.setApellidos(etApellidosJugador.getText().toString());
        jugadorAux.setFoto(nombreFoto);
        jugadorAux.setIdequipo(idEquipo);
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
        String cadena ="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        String nombre ="";
        int len = cadena.length()-1, aux;
        for (int e= 0; e<i; e++){
            aux = (int) (Math.random()*len);
            nombre = nombre + cadena.substring(aux, aux+1);
        }
        return nombre;
    }
}
