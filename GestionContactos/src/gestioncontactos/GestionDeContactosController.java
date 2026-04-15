package gestioncontactos;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GestionDeContactosController {
    private GestionDeContactosView view;    // Vista
    private ContactosModel model;           // Modelo

    public GestionDeContactosController(GestionDeContactosView view, ContactosModel model) {
        this.view = view;
        this.model = model;

        // Evento para agregar un contacto
        this.view.addAgregarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog(view, "Nombre:");
                String telefono = JOptionPane.showInputDialog(view, "Teléfono:");
                String correo = JOptionPane.showInputDialog(view, "Correo:");

                if (nombre != null && telefono != null && correo != null) {
                    model.agregarContacto(nombre, telefono, correo);
                    updateTable();  // Actualiza la tabla
                }
            }
        });

        // Evento para eliminar un contacto
        this.view.addEliminarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(view, 
                        "¿Eliminar este contacto?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        model.eliminarContacto(selectedRow);
                        updateTable();  // Actualiza la tabla después de eliminar
                    }
                }
            }
        });

        // Filtro de búsqueda para la tabla
        this.view.getFilterField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                updateTable();  // Actualiza la tabla cuando se escribe en el filtro
            }
        });

        // Agregar el MouseListener para el clic derecho (menú contextual)
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {  
                    int row = view.getTable().rowAtPoint(e.getPoint());
                    view.getTable().setRowSelectionInterval(row, row);  
                    showPopupMenu(e); 
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {  // Verifico si es el botón derecho
                    showPopupMenu(e);  // Muestra el menú contextual
                }
            }
        });
    }

    // Método para mostrar el menú contextual de clic derecho
    private void showPopupMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Opción del menú contextual");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opción seleccionada desde el clic derecho.");
            }
        });
        popupMenu.add(menuItem);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());  // Muestra el menú en la posición del clic
    }

    // Método para actualizar la tabla con los datos del modelo
    private void updateTable() {
        String filter = view.getFilterText();  // Obtener el texto del filtro
        List<ContactosModel.Contacto> contactos = model.obtenerContactos();
        List<ContactosModel.Contacto> filteredList = new ArrayList<>();

        // Filtrar los contactos según el texto ingresado
        for (ContactosModel.Contacto contacto : contactos) {
            if (contacto.getNombre().contains(filter) ||
                contacto.getTelefono().contains(filter) ||
                contacto.getCorreo().contains(filter)) {
                filteredList.add(contacto);
            }
        }

        // Convertir la lista filtrada a un formato que la tabla pueda mostrar
        Object[][] data = new Object[filteredList.size()][3];
        for (int i = 0; i < filteredList.size(); i++) {
            data[i][0] = filteredList.get(i).getNombre();
            data[i][1] = filteredList.get(i).getTelefono();
            data[i][2] = filteredList.get(i).getCorreo();
        }

        // Actualizar la tabla en la vista
        view.setTableData(data);
    }
}