    package com.example.ejercicioequiposjugadoresfinal.model.objects;

    import android.os.Parcel;
    import android.os.Parcelable;

    public class Equipo {

        private long id;
        private String nombre, ciudad, estadio, escudo;
        private int aforo;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getEstadio() {
            return estadio;
        }

        public void setEstadio(String estadio) {
            this.estadio = estadio;
        }

        public int getAforo() {
            return aforo;
        }

        public void setAforo(int aforo) {
            this.aforo = aforo;
        }

        public String getEscudo() {
            return escudo;
        }

        public void setEscudo(String escudo) {
            this.escudo = escudo;
        }

        @Override
        public String toString() {
            return "Equipo{" +
                    "id=" + id +
                    ", nombre='" + nombre + '\'' +
                    ", ciudad='" + ciudad + '\'' +
                    ", estadio='" + estadio + '\'' +
                    ", escudo='" + escudo + '\'' +
                    ", aforo=" + aforo +
                    '}';
        }
    }
