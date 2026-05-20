package rutas;

import java.util.ArrayList;
import java.util.List;

public class ListaLigada {
    private Parada cabeza;
    private int tamano;

    public ListaLigada() {
        this.cabeza = null;
        this.tamano = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamano() {
        return tamano;
    }

    public void agregarAlFinal(Parada nuevaParada) {
        if (estaVacia()) {
            cabeza = nuevaParada;
        } else {
            Parada actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevaParada);
        }
        tamano++;
    }

    public void agregarAlInicio(Parada nuevaParada) {
        nuevaParada.setSiguiente(cabeza);
        cabeza = nuevaParada;
        tamano++;
    }

    public Parada obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            return null;
        }
        Parada actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }

    public boolean eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            return false;
        }
        if (indice == 0) {
            cabeza = cabeza.getSiguiente();
        } else {
            Parada anterior = obtener(indice - 1);
            anterior.setSiguiente(anterior.getSiguiente().getSiguiente());
        }
        tamano--;
        return true;
    }

    public List<Parada> obtenerTodas() {
        List<Parada> paradas = new ArrayList<>();
        Parada actual = cabeza;
        while (actual != null) {
            paradas.add(actual);
            actual = actual.getSiguiente();
        }
        return paradas;
    }

    public void limpiar() {
        cabeza = null;
        tamano = 0;
    }

    public Parada getCabeza() {
        return cabeza;
    }
}
