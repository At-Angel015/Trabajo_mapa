package rutas;

import java.time.LocalTime;

public class Parada {
    private String nombre;
    private double latitud;
    private double longitud;
    private LocalTime tiempoLlegada;
    private String descripcion;
    private Parada siguiente;

    public Parada() {
    }

    public Parada(String nombre, double latitud, double longitud, LocalTime tiempoLlegada, String descripcion) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempoLlegada = tiempoLlegada;
        this.descripcion = descripcion;
        this.siguiente = null;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public LocalTime getTiempoLlegada() { return tiempoLlegada; }
    public void setTiempoLlegada(LocalTime tiempoLlegada) { this.tiempoLlegada = tiempoLlegada; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Parada getSiguiente() { return siguiente; }
    public void setSiguiente(Parada siguiente) { this.siguiente = siguiente; }
}
