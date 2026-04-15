package gestioncontactos;

import java.util.ArrayList;
import java.util.List;

public class ContactosModel {
    private List<Contacto> contactos;

    // Constructor
    public ContactosModel() {
        contactos = new ArrayList<>();
    }

    // Método para agregar un contacto
    public void agregarContacto(String nombre, String telefono, String correo) {
        contactos.add(new Contacto(nombre, telefono, correo));
    }

    // Método para obtener todos los contactos
    public List<Contacto> obtenerContactos() {
        return contactos;
    }

    // Método para eliminar un contacto
    public void eliminarContacto(int index) {
        if (index >= 0 && index < contactos.size()) {
            contactos.remove(index);
        }
    }

    // Clase Contacto
    public static class Contacto {
        private String nombre;
        private String telefono;
        private String correo;

        public Contacto(String nombre, String telefono, String correo) {
            this.nombre = nombre;
            this.telefono = telefono;
            this.correo = correo;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTelefono() {
            return telefono;
        }

        public String getCorreo() {
            return correo;
        }
    }
}