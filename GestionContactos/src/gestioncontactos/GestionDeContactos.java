package gestioncontactos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GestionDeContactos extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GestionDeContactos frame = new GestionDeContactos();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GestionDeContactos() {
        setTitle("Gestión de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // ─ JTabbedPane ─
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // ─ PESTAÑA 1: Contactos ─
        JPanel panelContactos = new JPanel(new BorderLayout(5, 5));
        panelContactos.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Campo de filtro en la parte NORTE (ahora sí funciona con BorderLayout)
        JPanel panelFiltro = new JPanel(new BorderLayout(5, 0));
        JLabel lblFiltro = new JLabel("Buscar:");
        JTextField filterField = new JTextField();
        filterField.setToolTipText("Escribo para filtrar contactos...");
        panelFiltro.add(lblFiltro, BorderLayout.WEST);
        panelFiltro.add(filterField, BorderLayout.CENTER);
        panelContactos.add(panelFiltro, BorderLayout.NORTH);

        // Tabla con DefaultTableModel para poder agregar/eliminar filas
        String[] columnNames = {"Nombre", "Teléfono", "Correo"};
        Object[][] data = {
            {"David",  "123456", "David@Gestion.com"},
            {"Pedro",   "654321", "Pedro@Gestion.com"},
            {"Carlos", "987654", "Carlos@Gestion.com"},
            {"Dyllan", "192837", "Dyllan@Gestion.com"}
        };
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        // Ordenamiento
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Filtro de texto
        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                String text = filterField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panelContactos.add(scrollPane, BorderLayout.CENTER);

        // Botones en la parte SUR
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnExportar = new JButton("Exportar CSV");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);
        panelContactos.add(panelBotones, BorderLayout.SOUTH);

        tabbedPane.addTab("Contactos", panelContactos);

        // PESTAÑA 2: Estadísticas
        JPanel panelEstadisticas = new JPanel(new GridLayout(4, 1, 10, 10));
        panelEstadisticas.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelEstadisticas.add(new JLabel("Total de contactos: " + tableModel.getRowCount()));
        panelEstadisticas.add(new JLabel("Cargando datos..."));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        panelEstadisticas.add(progressBar);

        JButton btnCargar = new JButton("Simular carga");
        panelEstadisticas.add(btnCargar);

        tabbedPane.addTab("Estadísticas", panelEstadisticas);

        // MENÚ CONTEXTUAL (clic derecho sobre la tabla)
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem itemEditar   = new JMenuItem("  Editar contacto");
        JMenuItem itemEliminar = new JMenuItem("  Eliminar contacto");
        JMenuItem itemCopiar   = new JMenuItem("  Copiar fila");
        JMenuItem itemExportar = new JMenuItem("  Exportar CSV");

        popupMenu.add(itemEditar);
        popupMenu.add(itemEliminar);
        popupMenu.addSeparator();
        popupMenu.add(itemCopiar);
        popupMenu.add(itemExportar);

        // Helper: listener reutilizable para mostrar el popup
        MouseAdapter popupListener = new MouseAdapter() {
            private void verificarPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    if (e.getSource() == table) {
                        int row = table.rowAtPoint(e.getPoint());
                        if (row >= 0) {
                            table.setRowSelectionInterval(row, row);
                        }
                    }
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent e)  { verificarPopup(e); }
            @Override
            public void mouseReleased(MouseEvent e) { verificarPopup(e); }
        };

        table.addMouseListener(popupListener);
        scrollPane.addMouseListener(popupListener);
        panelContactos.addMouseListener(popupListener);

        // ACCIONES DEL MENÚ CONTEXTUAL
        itemEditar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) return;
            int filaModelo = table.convertRowIndexToModel(fila);

            String nombre   = JOptionPane.showInputDialog(this, "Nombre:",   tableModel.getValueAt(filaModelo, 0));
            String telefono = JOptionPane.showInputDialog(this, "Teléfono:", tableModel.getValueAt(filaModelo, 1));
            String correo   = JOptionPane.showInputDialog(this, "Correo:",   tableModel.getValueAt(filaModelo, 2));

            if (nombre != null && telefono != null && correo != null) {
                tableModel.setValueAt(nombre,   filaModelo, 0);
                tableModel.setValueAt(telefono, filaModelo, 1);
                tableModel.setValueAt(correo,   filaModelo, 2);
            }
        });

        itemEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este contacto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(table.convertRowIndexToModel(fila));
            }
        });

        itemCopiar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) return;
            int fm = table.convertRowIndexToModel(fila);
            String texto = tableModel.getValueAt(fm, 0) + " | " +
                           tableModel.getValueAt(fm, 1) + " | " +
                           tableModel.getValueAt(fm, 2);
            java.awt.datatransfer.StringSelection sel =
                new java.awt.datatransfer.StringSelection(texto);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            JOptionPane.showMessageDialog(this, "Fila copiada al portapapeles.");
        });

        itemExportar.addActionListener(e -> exportToCSV());
        btnExportar.addActionListener(e -> exportToCSV());

        // BOTÓN AGREGAR 
        btnAgregar.addActionListener(e -> {
            String nombre   = JOptionPane.showInputDialog(this, "Nombre:");
            String telefono = JOptionPane.showInputDialog(this, "Teléfono:");
            String correo   = JOptionPane.showInputDialog(this, "Correo:");
            if (nombre != null && telefono != null && correo != null) {
                tableModel.addRow(new Object[]{nombre, telefono, correo});
            }
        });

        //  BOTÓN ELIMINAR 
        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila primero.");
                return;
            }
            tableModel.removeRow(table.convertRowIndexToModel(fila));
        });

        // SIMULACIÓN DE BARRA DE PROGRESO
        btnCargar.addActionListener(e -> {
            progressBar.setValue(0);
            Timer timer = new Timer(50, null);
            timer.addActionListener(ev -> {
                int val = progressBar.getValue();
                if (val >= 100) {
                    timer.stop();
                    progressBar.setString("¡Listo!");
                } else {
                    progressBar.setValue(val + 2);
                    progressBar.setString(val + 2 + "%");
                }
            });
            timer.start();
        });

        // ATAJO DE TECLADO Ctrl+N
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPane.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "newContact");
        actionMap.put("newContact", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAgregar.doClick(); // Reutiliza la lógica del botón Agregar
            }
        });
    }

    // EXPORTAR A CSV
    public void exportToCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("contactos.csv"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            // Cabeceras
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                writer.write(tableModel.getColumnName(j));
                if (j < tableModel.getColumnCount() - 1) writer.write(",");
            }
            writer.write("\n");
            // Filas (en el orden visible de la tabla)
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    writer.write(table.getValueAt(i, j).toString());
                    if (j < table.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
            }
            JOptionPane.showMessageDialog(this, "Exportado correctamente:\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
