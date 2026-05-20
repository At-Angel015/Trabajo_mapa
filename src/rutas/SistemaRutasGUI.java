package rutas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class SistemaRutasGUI extends JFrame {
    private ListaLigada ruta;
    private PersistenciaJSON persistencia;
    private JTable tablaParadas;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtLatitud, txtLongitud, txtTiempo, txtDescripcion;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("H:mm");

    public SistemaRutasGUI() {
        ruta = new ListaLigada();
        persistencia = new PersistenciaJSON("rutas.json");
        cargarDatos();
        inicializarGUI();
    }

    private void inicializarGUI() {
        setTitle("Sistema de Gestion de Rutas de Transporte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 250));

        JPanel panelFormulario = crearPanelFormulario();
        JPanel panelTabla = crearPanelTabla();
        JPanel panelBotones = crearPanelBotones();

        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panelFormulario.setPreferredSize(new Dimension(0, 220));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelFormulario, panelTabla);
        split.setResizeWeight(0.22);
        split.setDividerSize(8);
        split.setDividerLocation(220);
        split.setBorder(null);

        panelPrincipal.add(split, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
        actualizarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 10, 12, 10);

        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteLabel = new Font("Segoe UI", Font.BOLD, 13);

        txtNombre = crearCampo(fuenteCampo);
        txtLatitud = crearCampo(fuenteCampo);
        txtLongitud = crearCampo(fuenteCampo);
        txtTiempo = crearCampo(fuenteCampo);
        txtDescripcion = crearCampo(fuenteCampo);

        Dimension dimGrande = new Dimension(0, 42);
        txtNombre.setPreferredSize(dimGrande);
        txtLatitud.setPreferredSize(dimGrande);
        txtLongitud.setPreferredSize(dimGrande);
        txtTiempo.setPreferredSize(dimGrande);
        txtDescripcion.setPreferredSize(dimGrande);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35; gbc.weighty = 0;
        panel.add(crearLabel("Nombre:", fuenteLabel), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35;
        panel.add(txtNombre, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        panel.add(crearLabel("Latitud:", fuenteLabel), gbc);
        gbc.gridx = 3; gbc.weightx = 0.15;
        panel.add(txtLatitud, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        panel.add(crearLabel("Longitud:", fuenteLabel), gbc);
        gbc.gridx = 1; gbc.weightx = 0.35;
        panel.add(txtLongitud, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        panel.add(crearLabel("Tiempo:", fuenteLabel), gbc);
        gbc.gridx = 3; gbc.weightx = 0.15;
        panel.add(txtTiempo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.15; gbc.gridwidth = 1;
        panel.add(crearLabel("Descripcion:", fuenteLabel), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.85;
        panel.add(txtDescripcion, gbc);
        gbc.gridwidth = 1;

        return panel;
    }

    private JTextField crearCampo(Font fuente) {
        JTextField campo = new JTextField();
        campo.setFont(fuente);
        return campo;
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuente);
        return lbl;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {"Nombre", "Latitud", "Longitud", "Tiempo", "Descripcion"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaParadas = new JTable(modeloTabla);
        tablaParadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaParadas.setRowHeight(30);
        tablaParadas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaParadas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaParadas.setShowGrid(true);
        tablaParadas.setGridColor(new Color(220, 220, 235));
        tablaParadas.setIntercellSpacing(new Dimension(5, 5));

        JScrollPane scroll = new JScrollPane(tablaParadas);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));

        JButton btnAgregar = new JButton("Agregar Parada");
        JButton btnSeleccionar = new JButton("Seleccionar Parada");
        JButton btnModificar = new JButton("Modificar Parada");
        JButton btnEliminar = new JButton("Eliminar Parada");
        JButton btnMostrar = new JButton("Mostrar Paradas");
        JButton btnGuardar = new JButton("Guardar Ruta");
        JButton btnCargar = new JButton("Cargar Ruta");
        JButton btnDistancia = new JButton("Calcular Distancia");

        Font fuenteBoton = new Font("Segoe UI", Font.BOLD, 13);
        for (JButton btn : new JButton[]{btnAgregar, btnSeleccionar, btnModificar, btnEliminar,
                                           btnMostrar, btnGuardar, btnCargar, btnDistancia}) {
            btn.setFont(fuenteBoton);
            btn.setPreferredSize(new Dimension(160, 50));
            btn.setFocusPainted(false);
        }

        btnAgregar.addActionListener(e -> agregarParada());
        btnSeleccionar.addActionListener(e -> seleccionarParada());
        btnModificar.addActionListener(e -> modificarParada());
        btnEliminar.addActionListener(e -> eliminarParada());
        btnMostrar.addActionListener(e -> mostrarParadas());
        btnGuardar.addActionListener(e -> guardarRuta());
        btnCargar.addActionListener(e -> cargarRuta());
        btnDistancia.addActionListener(e -> calcularDistancia());

        panel.add(btnAgregar);
        panel.add(btnSeleccionar);
        panel.add(btnModificar);
        panel.add(btnEliminar);
        panel.add(btnMostrar);
        panel.add(btnGuardar);
        panel.add(btnCargar);
        panel.add(btnDistancia);

        return panel;
    }

    private void agregarParada() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio"); return; }
            double latitud = parsearCoordenada(txtLatitud.getText().trim(), "Latitud");
            double longitud = parsearCoordenada(txtLongitud.getText().trim(), "Longitud");
            LocalTime tiempo = LocalTime.parse(txtTiempo.getText().trim(), FORMATO_HORA);
            String descripcion = txtDescripcion.getText().trim();

            Parada parada = new Parada(nombre, latitud, longitud, tiempo, descripcion);
            ruta.agregarAlFinal(parada);
            limpiarCampos();
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Parada agregada exitosamente");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar parada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parsearCoordenada(String texto, String nombreCampo) {
        if (texto.isEmpty()) throw new IllegalArgumentException("El campo '" + nombreCampo + "' es obligatorio");
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + nombreCampo + "' debe ser un numero valido. Ej: 40.4168 o 40,4168");
        }
    }

    private void seleccionarParada() {
        int fila = tablaParadas.getSelectedRow();
        if (fila >= 0) {
            Parada parada = ruta.obtener(fila);
            if (parada != null) {
                txtNombre.setText(parada.getNombre());
                txtLatitud.setText(String.valueOf(parada.getLatitud()));
                txtLongitud.setText(String.valueOf(parada.getLongitud()));
                txtTiempo.setText(parada.getTiempoLlegada().format(FORMATO_HORA));
                txtDescripcion.setText(parada.getDescripcion());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una parada de la tabla");
        }
    }

    private void modificarParada() {
        int fila = tablaParadas.getSelectedRow();
        if (fila >= 0) {
            try {
                String nombre = txtNombre.getText().trim();
                if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio"); return; }
                double latitud = parsearCoordenada(txtLatitud.getText().trim(), "Latitud");
                double longitud = parsearCoordenada(txtLongitud.getText().trim(), "Longitud");
                LocalTime tiempo = LocalTime.parse(txtTiempo.getText().trim(), FORMATO_HORA);
                String descripcion = txtDescripcion.getText().trim();

                Parada parada = ruta.obtener(fila);
                parada.setNombre(nombre);
                parada.setLatitud(latitud);
                parada.setLongitud(longitud);
                parada.setTiempoLlegada(tiempo);
                parada.setDescripcion(descripcion);
                limpiarCampos();
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Parada modificada exitosamente");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al modificar parada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una parada de la tabla");
        }
    }

    private void eliminarParada() {
        int fila = tablaParadas.getSelectedRow();
        if (fila >= 0) {
            ruta.eliminar(fila);
            limpiarCampos();
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Parada eliminada exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una parada de la tabla");
        }
    }

    private void mostrarParadas() {
        actualizarTabla();
        StringBuilder sb = new StringBuilder();
        List<Parada> paradas = ruta.obtenerTodas();
        for (int i = 0; i < paradas.size(); i++) {
            Parada p = paradas.get(i);
            sb.append((i + 1) + ". " + p.getNombre() + " (" + p.getLatitud() + ", " + p.getLongitud() + ")\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Paradas de la Ruta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarRuta() {
        if (persistencia.guardarRuta(ruta)) {
            JOptionPane.showMessageDialog(this, "Ruta guardada exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la ruta");
        }
    }

    private void cargarRuta() {
        ListaLigada nuevaRuta = persistencia.cargarRuta();
        if (nuevaRuta.getTamano() > 0) {
            this.ruta = nuevaRuta;
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Ruta cargada exitosamente");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron datos para cargar");
        }
    }

    private void calcularDistancia() {
        double distancia = CalculoDistancia.calcularDistanciaTotal(ruta);
        JOptionPane.showMessageDialog(this,
            String.format("Distancia total aproximada: %.2f km", distancia),
            "Calculo de Distancia",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatos() {
        ruta = persistencia.cargarRuta();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Parada> paradas = ruta.obtenerTodas();
        for (Parada p : paradas) {
            Object[] fila = {
                p.getNombre(),
                p.getLatitud(),
                p.getLongitud(),
                p.getTiempoLlegada(),
                p.getDescripcion()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtTiempo.setText("");
        txtDescripcion.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SistemaRutasGUI().setVisible(true);
        });
    }
}
