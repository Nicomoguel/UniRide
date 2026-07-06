import java.util.ArrayList;

public class PruebaDB {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        ArrayList<User> usuariosParaGuardar = new ArrayList<>();
        
        Driver conductor = new Driver("A001", "pass123", "MEX123", (short)21, (short)2, true);
        Passenger pasajero = new Passenger("A002", "pass456", "MEX456", (short)19, (short)3);
        
        usuariosParaGuardar.add(conductor);
        usuariosParaGuardar.add(pasajero);

        // Probar el método de Guardado (Serialización)
        System.out.println("Guardando usuarios en el disco duro...");
        dbManager.saveUsers(usuariosParaGuardar);

        // Probar el método de Carga (Deserialización)
        System.out.println("Cargando usuarios desde el archivo txt...");
        ArrayList<User> usuariosCargados = dbManager.loadUsers();

        // Imprimir los resultados para verificar que los objetos volvieron a la vida
        System.out.println("\n--- RESULTADOS DE LA PRUEBA ---");
        for (User u : usuariosCargados) {
            System.out.println("Tipo de Usuario: " + u.getClass().getSimpleName());
            System.out.println("ID Estudiante: " + u.getStudentId());
            System.out.println("ID MEX: " + u.getIDMEX());
            
            // Validamos que el casteo funcione correctamente para el Driver
            if (u instanceof Driver) {
                System.out.println("¿Licencia Válida?: " + ((Driver) u).isLicenseValid());
            }
            System.out.println("-------------------------------");
        }
    }
}
