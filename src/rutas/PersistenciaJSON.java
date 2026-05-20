package rutas;

import java.io.*;
import java.nio.file.*;
import java.time.LocalTime;
import java.util.List;

public class PersistenciaJSON {
    private String archivo;

    public PersistenciaJSON(String archivo) {
        this.archivo = archivo;
    }

    public boolean guardarRuta(ListaLigada lista) {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n  \"paradas\": [\n");

            List<Parada> paradas = lista.obtenerTodas();
            for (int i = 0; i < paradas.size(); i++) {
                Parada p = paradas.get(i);
                json.append("    {\n");
                json.append("      \"nombre\": \"").append(escapeJson(p.getNombre())).append("\",\n");
                json.append("      \"latitud\": ").append(p.getLatitud()).append(",\n");
                json.append("      \"longitud\": ").append(p.getLongitud()).append(",\n");
                json.append("      \"tiempoLlegada\": \"").append(p.getTiempoLlegada()).append("\",\n");
                json.append("      \"descripcion\": \"").append(escapeJson(p.getDescripcion() != null ? p.getDescripcion() : "")).append("\"\n");
                json.append("    }");
                if (i < paradas.size() - 1) json.append(",");
                json.append("\n");
            }

            json.append("  ]\n}");

            Files.write(Paths.get(archivo), json.toString().getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ListaLigada cargarRuta() {
        ListaLigada lista = new ListaLigada();
        try {
            Path path = Paths.get(archivo);
            if (!Files.exists(path)) {
                return lista;
            }
            String contenido = new String(Files.readAllBytes(path));
            String[] paradasJson = contenido.split("\\},\\s*\\{");

            for (String paradaJson : paradasJson) {
                String nombre = extraerValor(paradaJson, "nombre");
                double latitud = Double.parseDouble(extraerValor(paradaJson, "latitud"));
                double longitud = Double.parseDouble(extraerValor(paradaJson, "longitud"));
                String tiempoStr = extraerValor(paradaJson, "tiempoLlegada");
                String descripcion = extraerValor(paradaJson, "descripcion");

                LocalTime tiempo = LocalTime.parse(tiempoStr);
                Parada parada = new Parada(nombre, latitud, longitud, tiempo, descripcion);
                lista.agregarAlFinal(parada);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private String escapeJson(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String extraerValor(String json, String campo) {
        String patron = "\"" + campo + "\": \"";
        int inicio = json.indexOf(patron);
        if (inicio == -1) {
            patron = "\"" + campo + "\": ";
            inicio = json.indexOf(patron);
            if (inicio == -1) return "";
            inicio += patron.length();
            int fin = json.indexOf(",", inicio);
            if (fin == -1) fin = json.indexOf("\n", inicio);
            return json.substring(inicio, fin).trim();
        }
        inicio += patron.length();
        int fin = json.indexOf("\"", inicio);
        return json.substring(inicio, fin);
    }
}
