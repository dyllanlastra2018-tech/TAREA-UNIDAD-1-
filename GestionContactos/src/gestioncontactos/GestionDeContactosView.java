package gestioncontactos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class GestionDeContactosView extends JFrame implements Serializable {
    private static final long serialVersionUID = 1L; 

    private JTable table;
    private JButton btnAgregar, btnEliminar;
    private JTextField filterField;

    public GestionDeContactosView() {
        // Configuración básica de la ventana
        setTitle("Gestión de Contactos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Crear la tabla
        String[] columnNames = {"Nombre", "Teléfono", "Correo"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Crear panel de filtro
        JPanel panelFiltro = new JPanel(new BorderLayout(5, 0));
        JLabel lblFiltro = new JLabel("Buscar:");
        filterField = new JTextField();
        filterField.setToolTipText("Escribe para filtrar contactos...");
        panelFiltro.add(lblFiltro, BorderLayout.WEST);
        panelFiltro.add(filterField, BorderLayout.CENTER);
        panel.add(panelFiltro, BorderLayout.NORTH);

        // Crear botones de agregar y eliminar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar Contacto");
        btnEliminar = new JButton("Eliminar Contacto");
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEliminar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar todo al panel principal
        setContentPane(panel);
    }

    // Métodos de la Vista

    // Obtener el campo de filtro
    public JTextField getFilterField() {
        return filterField;  // Aquí es donde estamos agregando el método
    }

    // Obtener el texto del filtro
    public String getFilterText() {
        return filterField.getText();
    }

    // Actualizar la tabla con los nuevos datos
    public void setTableData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);  // Limpiar la tabla
        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    // Agregar listener al botón "Agregar"
    public void addAgregarListener(ActionListener listener) {
        btnAgregar.addActionListener(listener);
    }

    // Agregar listener al botón "Eliminar"
    public void addEliminarListener(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }

    // Obtener la tabla
    public JTable getTable() {
        return table;
    }
}