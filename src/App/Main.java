package App;

import acceso2.ConexionMySQL;

public class Main {
    public static void main(String[] args) {
        ConexionMySQL conexion = new ConexionMySQL("ADMIN", "TU_CONTRASEÑA", "");

        if (conexion.probarConexion()) {
            System.out.println("Conexión correcta");
        } else {
            System.out.println("Conexión fallida");
        }
    }
}

