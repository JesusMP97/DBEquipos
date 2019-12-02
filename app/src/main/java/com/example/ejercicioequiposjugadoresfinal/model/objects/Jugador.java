package com.example.ejercicioequiposjugadoresfinal.model.objects;

public class Jugador {

    private long id, idequipo;
    private String nombre, apellidos, foto;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdequipo() {
        return idequipo;
    }

    public void setIdequipo(long idequipo) {
        this.idequipo = idequipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", idequipo=" + idequipo +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
