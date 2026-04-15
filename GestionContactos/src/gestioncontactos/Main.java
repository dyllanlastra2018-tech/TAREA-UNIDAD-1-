package gestioncontactos;

public class Main {
    public static void main(String[] args) {
        // Crear el modelo
        ContactosModel model = new ContactosModel();

        // Crear la vista
        GestionDeContactosView view = new GestionDeContactosView();

        // Crear el controlador y conectar la vista y el modelo
        new GestionDeContactosController(view, model); 

        // Mostrar la vista
        view.setVisible(true);  // Esto hace visible la interfaz
    }
}