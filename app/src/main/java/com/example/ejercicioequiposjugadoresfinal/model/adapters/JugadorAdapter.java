package com.example.ejercicioequiposjugadoresfinal.model.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ejercicioequiposjugadoresfinal.EditJugador;
import com.example.ejercicioequiposjugadoresfinal.R;
import com.example.ejercicioequiposjugadoresfinal.VerJugadores;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Jugador;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;
import com.example.ejercicioequiposjugadoresfinal.model.view.model.MainViewModel;

import java.util.List;

public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.ViewHolder> {

    private MainViewModel viewModel;
    private LayoutInflater inflater;
    private List<Jugador> listaJugadores;
    private Context context;
    private long idEquipo;

    private static final String JUG_IMGS_FOLDER = "upload/";
    private static final String ID_JUGADOR = "idJugadorSelected";
    private static final String ID_EQUIPO = "idEquipoSelected";
    private Repository repository;
    private String url;

    public JugadorAdapter(Context context, long idEquipo){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.viewModel = VerJugadores.viewModel;
        this.idEquipo = idEquipo;
        repository = new Repository();
        url = repository.getURL();
    }

    @NonNull
    @Override
    public JugadorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.jugador_item, parent, false);
        JugadorAdapter.ViewHolder holder = new JugadorAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorAdapter.ViewHolder holder, final int position) {
        final Jugador jugadorActual = listaJugadores.get(position);
        Log.d("XYZA", ""+jugadorActual.getIdequipo());
        Log.d("XYZA", ""+idEquipo);
            holder.tvNombre.setText(jugadorActual.getNombre());
            holder.tvApellido.setText(jugadorActual.getApellidos());
            Glide.with(context)
                    .load(url+ JUG_IMGS_FOLDER + jugadorActual.getFoto())
                    .override(500, 500)
                    .into(holder.ivJugador);
            holder.ibBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.borrarJugadorCheck)
                            .setTitle(R.string.borrarJugadorTitle);
                    builder.setPositiveButton(R.string.borrarJugadorSi, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            viewModel.borrarJugador(jugadorActual.getId());
                            notifyItemRemoved(position);
                            listaJugadores.remove(position);
                        }
                    });
                    builder.setNegativeButton(R.string.borrarJugadorNo, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            holder.ibEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditJugador.class);
                    i.putExtra(ID_JUGADOR, jugadorActual.getId());
                    i.putExtra(ID_EQUIPO, idEquipo);
                    context.startActivity(i);
                }
            });
    }


    @Override
    public int getItemCount() {
        int elements = 0;
        if(listaJugadores !=null){
            elements = listaJugadores.size();
        }
        return elements;
    }

    public void setData(List<Jugador> listaJugadores){
        this.listaJugadores = listaJugadores;
        notifyDataSetChanged();
        //cargaArrayAux();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout cvJugadores;
        private ImageButton ibEditar, ibBorrar;
        private ImageView ivJugador;
        private TextView tvNombre, tvApellido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvJugadores = itemView.findViewById(R.id.cvJugador);
            ibBorrar = itemView.findViewById(R.id.ibBorrarJugador);
            ibEditar = itemView.findViewById(R.id.ibEditarJugador);
            ivJugador = itemView.findViewById(R.id.ivJugador);
            tvApellido = itemView.findViewById(R.id.tvApellidoJugador);
            tvNombre = itemView.findViewById(R.id.tvNombreJugador);
        }
    }
}
