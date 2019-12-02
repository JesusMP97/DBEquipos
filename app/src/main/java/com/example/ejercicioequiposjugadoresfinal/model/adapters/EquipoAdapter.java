package com.example.ejercicioequiposjugadoresfinal.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ejercicioequiposjugadoresfinal.R;
import com.example.ejercicioequiposjugadoresfinal.model.objects.Equipo;
import com.example.ejercicioequiposjugadoresfinal.model.repository.Repository;


import java.util.List;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private LayoutInflater inflater;
    private List<Equipo> listaEquipos;
    private Context context;
    private static final String TEAM_IMGS_FOLDER = "upload/";
    private Repository repository;
    private String url;

    public EquipoAdapter(Context context, OnItemClickListener listener){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        repository = new Repository();
        url = repository.getURL();
    }

    public interface OnItemClickListener{
        void onItemClick(Equipo equipo);
    }

    @NonNull
    @Override
    public EquipoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.equipo_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EquipoAdapter.ViewHolder holder, int position) {
        final Equipo equipoActual = listaEquipos.get(position);
        holder.tvEquipo.setText(equipoActual.getNombre());
        Glide.with(context)
                .load(url+TEAM_IMGS_FOLDER+equipoActual.getEscudo())
                .override(500,500)
                .into(holder.ivEquipo);
        holder.cdEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(equipoActual);
            }
        });
    }

    @Override
    public int getItemCount() {
        int elements=0;
        if(listaEquipos !=null){
            elements= listaEquipos.size();
        }
        return elements;
    }

    public void setData(List<Equipo> listaEquipos){
        this.listaEquipos = listaEquipos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivEquipo;
        private TextView tvEquipo;
        private ConstraintLayout cdEquipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEquipo = itemView.findViewById(R.id.ivEquipo);
            tvEquipo = itemView.findViewById(R.id.tvEquipo);
            cdEquipo = itemView.findViewById(R.id.cvEquipo);
        }
    }
}
