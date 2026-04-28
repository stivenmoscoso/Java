package com.java;

/**
 * Record inmutable para transportar la configuracion de conexion.
 * En MVC este tipo de dato puede viajar del controlador al modelo sin exponer
 * setters ni permitir cambios inesperados durante el flujo.
 */
public record DatabaseConfig(String url, String usuario, String password) {

    public DatabaseConfig {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL de la base de datos es obligatoria");
        }
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario de la base de datos es obligatorio");
        }
        if (password == null) {
            throw new IllegalArgumentException("La clave de la base de datos no puede ser null");
        }
    }

    public static DatabaseConfig fromEnvironment() {
        String url = leerPrimeroNoVacio(System.getProperty("db.url"), System.getenv("DB_URL"));
        String usuario = leerPrimeroNoVacio(System.getProperty("db.user"), System.getenv("DB_USER"));
        String password = leerPrimeroNoNulo(System.getProperty("db.password"), System.getenv("DB_PASSWORD"));
        return new DatabaseConfig(url, usuario, password);
    }

    private static String leerPrimeroNoVacio(String primero, String segundo) {
        if (primero != null && !primero.isBlank()) {
            return primero;
        }
        if (segundo != null && !segundo.isBlank()) {
            return segundo;
        }
        return null;
    }

    private static String leerPrimeroNoNulo(String primero, String segundo) {
        return primero != null ? primero : segundo;
    }
}
