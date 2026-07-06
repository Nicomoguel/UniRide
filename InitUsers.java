import java.util.ArrayList;

public class InitUsers{
    /*
    public static ArrayList getUsers(){
        DatabaseManager dbManager = new DatabaseManager();
        ArrayList<User> usuariosParaGuardar = new ArrayList<>();
        Driver conductor = new Driver("A001", "pass123", "MEX123", (short)21, (short)2, true);
        Passenger pasajero = new Passenger("A002", "pass456", "MEX456", (short)19, (short)3); 
        usuariosParaGuardar.add(conductor);
        usuariosParaGuardar.add(pasajero);
        dbManager.saveUsers(usuariosParaGuardar);
        ArrayList<User> usuariosCargados = dbManager.loadUsers();

        return usuariosCargados;
    }
    */


    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        ArrayList<User> usuariosParaGuardar = new ArrayList<>();
        
        // Conductor: destino Prepa 8 "Carlos Castillo Peraza" (nodo 133 = 145,214), desviación máx 60
        Driver conductor = new Driver("A001", "pass123", "MEX123", 60, (short)21, (short)2, true, new Node("2", 328, 34), new Node("133", 145, 214), new Schedule("10:30", "12:39"));
        // Dos conductores más, cada uno con una ruta distinta (origen -> destino diferentes)
        Driver conductor2 = new Driver("A006", "pass606", "MEX606", 70, (short)25, (short)3, true, new Node("origen6", 490, 78), new Node("destino6", 260, 210), new Schedule("08:00", "09:30"));
        Driver conductor3 = new Driver("A007", "pass707", "MEX707", 50, (short)30, (short)4, true, new Node("origen7", 221, 29), new Node("destino7", 439, 202), new Schedule("14:00", "15:45"));
        // Pasajeros con horario compatible con el conductor: la distancia es la que decide el match
        Passenger pasajero1 = new Passenger("A002", "pass456", "MEX456", (short)19, (short)30, new Node("cerca", 362, 86), new Node("prepa8", 145, 214), new Schedule("10:42", "11:30")); // ~34px -> MATCH
        Passenger pasajero2 = new Passenger("A003", "pass456", "MEX432", (short)22, (short)30, new Node("lejos1", 490, 78), new Node("prepa8", 145, 214), new Schedule("10:22", "11:10")); // ~72px -> DISTANCIA
        Passenger pasajero3 = new Passenger("A004", "pass789", "MEX789", (short)20, (short)60, new Node("lejos2", 260, 210), new Node("prepa8", 145, 214), new Schedule("11:15", "12:00")); // ~95px -> DISTANCIA
        Passenger pasajero4 = new Passenger("A005", "pass321", "MEX321", (short)23, (short)30, new Node("lejos3", 204, 73), new Node("prepa8", 145, 214), new Schedule("10:35", "11:20")); // ~130px -> DISTANCIA

        usuariosParaGuardar.add(conductor);
        usuariosParaGuardar.add(conductor2);
        usuariosParaGuardar.add(conductor3);
        usuariosParaGuardar.add(pasajero1);
        usuariosParaGuardar.add(pasajero2);
        usuariosParaGuardar.add(pasajero3);
        usuariosParaGuardar.add(pasajero4);

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
