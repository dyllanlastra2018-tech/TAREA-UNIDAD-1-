package gestioncontactos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class GestionDeContactos extends JFrame {
    private static final long serialVersionUID = 1L;

    //  PALETA DE COLORES
    private static final Color AZUL_OSCURO = new Color(44, 62, 80);
    private static final Color AZUL_CLARO  = new Color(52, 152, 219);
    private static final Color GRIS_FONDO  = new Color(236, 240, 241);
    private static final Color VERDE       = new Color(39, 174, 96);
    private static final Color ROJO        = new Color(192, 57, 43);
    private static final Color BLANCO      = Color.WHITE;

    // FUENTES 
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FUENTE_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FUENTE_BOTON  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FUENTE_TABLA  = new Font("Segoe UI", Font.PLAIN, 13);

    // COMPONENTES 
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTitulo, lblIdioma;
    private JButton btnAgregar, btnEliminar, btnExportar;
    private JComboBox<String> comboIdioma;
    private JLabel lblBuscar;
    private JTextField filterField;

    // I18N con ResourceBundle 
    private ResourceBundle bundle;
    private void cargarBundle(int idx) {
        String[] langs = {"es", "en", "fr"};
        bundle = ResourceBundle.getBundle("gestioncontactos.messages",
            new Locale.Builder().setLanguage(langs[idx]).build());
    }

    private String t(String key) {
        return bundle.getString(key);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GestionDeContactos frame = new GestionDeContactos();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GestionDeContactos() {
        cargarBundle(0);
        construirUI();
        configurarEventos();
    }

    private void construirUI() {
        setTitle(t("titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 520);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(GRIS_FONDO);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // PANEL SUPERIOR (BorderLayout) 
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(AZUL_OSCURO);
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lblTitulo = new JLabel(t("titulo"));
        lblTitulo.setForeground(BLANCO);
        lblTitulo.setFont(FUENTE_TITULO);

        JPanel panelIdiomaBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelIdiomaBox.setBackground(AZUL_OSCURO);

        lblIdioma = new JLabel(t("label.idioma"));
        lblIdioma.setForeground(BLANCO);
        lblIdioma.setFont(FUENTE_LABEL);

        comboIdioma = new JComboBox<String>(new String[]{"ES", "EN", "FR"});
        comboIdioma.setFont(FUENTE_LABEL);
        comboIdioma.setPreferredSize(new Dimension(70, 26));

        panelIdiomaBox.add(lblIdioma);
        panelIdiomaBox.add(comboIdioma);
        panelTop.add(lblTitulo, BorderLayout.WEST);
        panelTop.add(panelIdiomaBox, BorderLayout.EAST);

        contentPane.add(panelTop, BorderLayout.NORTH);

        // PANEL CENTRAL CON TABS
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FUENTE_LABEL);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // ─ PESTAÑA 1: Contactos ─
        JPanel panelContactos = new JPanel(new BorderLayout(5, 5));
        panelContactos.setBackground(GRIS_FONDO);
        panelContactos.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Filtro
        JPanel panelFiltro = new JPanel(new BorderLayout(5, 0));
        panelFiltro.setBackground(GRIS_FONDO);
        panelFiltro.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        lblBuscar = new JLabel(t("btn.buscar"));
        lblBuscar.setFont(FUENTE_LABEL);

        filterField = new JTextField();
        filterField.setFont(FUENTE_TABLA);

        panelFiltro.add(lblBuscar, BorderLayout.WEST);
        panelFiltro.add(filterField, BorderLayout.CENTER);
        panelContactos.add(panelFiltro, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {t("label.nombre"), t("label.telefono"), t("label.email")};
        Object[][] data = {
            {"David",  "123456", "David@Gestion.com"},
            {"Pedro",  "654321", "Pedro@Gestion.com"},
            {"Carlos", "987654", "Carlos@Gestion.com"},
            {"Dyllan", "192837", "Dyllan@Gestion.com"}
        };
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(FUENTE_TABLA);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(AZUL_CLARO);
        table.setSelectionForeground(BLANCO);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(AZUL_OSCURO);
        table.getTableHeader().setForeground(BLANCO);
        table.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                String text = filterField.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(AZUL_CLARO, 1));
        panelContactos.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        panelBotones.setBackground(GRIS_FONDO);

        btnAgregar  = boton(t("btn.agregar"),  VERDE);
        btnEliminar = boton(t("btn.eliminar"), ROJO);
        btnExportar = boton(t("btn.exportar"), AZUL_CLARO);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);
        panelContactos.add(panelBotones, BorderLayout.SOUTH);

        tabbedPane.addTab(t("tab.contactos"), panelContactos);

        // ─ PESTAÑA 2: Estadísticas ─
        JPanel panelEstadisticas = new JPanel(new GridLayout(4, 1, 10, 10));
        panelEstadisticas.setBackground(GRIS_FONDO);
        panelEstadisticas.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelEstadisticas.add(new JLabel(t("label.total") + " " + tableModel.getRowCount()));
        panelEstadisticas.add(new JLabel(t("label.cargando")));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        panelEstadisticas.add(progressBar);

        JButton btnCargar = boton(t("btn.simular"), AZUL_CLARO);
        panelEstadisticas.add(btnCargar);

        tabbedPane.addTab(t("tab.estadisticas"), panelEstadisticas);

        // MENÚ CONTEXTUAL
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem itemEditar   = new JMenuItem(t("menu.editar"));
        JMenuItem itemEliminar = new JMenuItem(t("menu.eliminar"));
        JMenuItem itemCopiar   = new JMenuItem(t("menu.copiar"));
        JMenuItem itemExportar = new JMenuItem(t("menu.exportar"));

        popupMenu.add(itemEditar);
        popupMenu.add(itemEliminar);
        popupMenu.addSeparator();
        popupMenu.add(itemCopiar);
        popupMenu.add(itemExportar);

        MouseAdapter popupListener = new MouseAdapter() {
            private void verificarPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    if (e.getSource() == table) {
                        int row = table.rowAtPoint(e.getPoint());
                        if (row >= 0) table.setRowSelectionInterval(row, row);
                    }
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            @Override public void mousePressed(MouseEvent e)  { verificarPopup(e); }
            @Override public void mouseReleased(MouseEvent e) { verificarPopup(e); }
        };

        table.addMouseListener(popupListener);
        scrollPane.addMouseListener(popupListener);
        panelContactos.addMouseListener(popupListener);

        // ACCIONES MENÚ CONTEXTUAL
        itemEditar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) return;
            int fm = table.convertRowIndexToModel(fila);
            String nombre   = JOptionPane.showInputDialog(this, t("label.nombre"),   tableModel.getValueAt(fm, 0));
            String telefono = JOptionPane.showInputDialog(this, t("label.telefono"), tableModel.getValueAt(fm, 1));
            String correo   = JOptionPane.showInputDialog(this, t("label.email"),    tableModel.getValueAt(fm, 2));
            if (nombre != null && telefono != null && correo != null) {
                tableModel.setValueAt(nombre,   fm, 0);
                tableModel.setValueAt(telefono, fm, 1);
                tableModel.setValueAt(correo,   fm, 2);
            }
        });

        itemEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                t("msg.confirmar"), t("msg.seleccionar.titulo"), JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
                tableModel.removeRow(table.convertRowIndexToModel(fila));
        });

        itemCopiar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) return;
            int fm = table.convertRowIndexToModel(fila);
            String texto = tableModel.getValueAt(fm, 0) + " | " +
                           tableModel.getValueAt(fm, 1) + " | " +
                           tableModel.getValueAt(fm, 2);
            java.awt.datatransfer.StringSelection sel = new java.awt.datatransfer.StringSelection(texto);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            JOptionPane.showMessageDialog(this, t("msg.copiado"));
        });

        itemExportar.addActionListener(e -> exportToCSV());
        btnExportar.addActionListener(e -> exportToCSV());

        btnCargar.addActionListener(e -> {
            progressBar.setValue(0);
            Timer timer = new Timer(50, null);
            timer.addActionListener(ev -> {
                int val = progressBar.getValue();
                if (val >= 100) {
                    timer.stop();
                    progressBar.setString(t("msg.listo"));
                } else {
                    progressBar.setValue(val + 2);
                    progressBar.setString((val + 2) + "%");
                }
            });
            timer.start();
        });

        // ATAJO Ctrl+N
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPane.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "newContact");
        actionMap.put("newContact", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { btnAgregar.doClick(); }
        });
    }

    private void configurarEventos() {

        // Cambio de idioma
        comboIdioma.addActionListener(e -> {
            cargarBundle(comboIdioma.getSelectedIndex());
            actualizarTextos();
        });

        // Agregar
        btnAgregar.addActionListener(e -> {
            String nombre   = JOptionPane.showInputDialog(this, t("label.nombre"));
            String telefono = JOptionPane.showInputDialog(this, t("label.telefono"));
            String correo   = JOptionPane.showInputDialog(this, t("label.email"));
            if (nombre != null && !nombre.trim().isEmpty() &&
                telefono != null && correo != null) {
                tableModel.addRow(new Object[]{nombre, telefono, correo});
            }
        });

        // Eliminar
        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, t("msg.seleccionar"), t("msg.seleccionar.titulo"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            tableModel.removeRow(table.convertRowIndexToModel(fila));
        });
    }

    private void actualizarTextos() {
        setTitle(t("titulo"));
        lblTitulo.setText(t("titulo"));
        lblIdioma.setText(t("label.idioma"));
        lblBuscar.setText(t("btn.buscar"));
        btnAgregar.setText(t("btn.agregar"));
        btnEliminar.setText(t("btn.eliminar"));
        btnExportar.setText(t("btn.exportar"));
        tableModel.setColumnIdentifiers(new String[]{
            t("label.nombre"), t("label.telefono"), t("label.email")});
        revalidate();
        repaint();
    }

    private JButton boton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(BLANCO);
        b.setFont(FUENTE_BOTON);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public void exportToCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("contactos.csv"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        File archivo = chooser.getSelectedFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                writer.write(tableModel.getColumnName(j));
                if (j < tableModel.getColumnCount() - 1) writer.write(",");
            }
            writer.write("\n");
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    writer.write(table.getValueAt(i, j).toString());
                    if (j < table.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
            }
            JOptionPane.showMessageDialog(this, t("msg.exportado") + "\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, t("msg.error") + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}