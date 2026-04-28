package com.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utilidad JDBC para centralizar la apertura de conexiones.
 *
 * <p>La seguridad basica se apoya en dos decisiones:
 * 1. La configuracion se lee desde variables de entorno o system properties,
 *    evitando credenciales quemadas en el codigo fuente.
 * 2. Las consultas usan PreparedStatement para reducir el riesgo de SQL Injection.
 *
 * <p>Java 17/21 recomienda try-with-resources porque Connection,
 * PreparedStatement y ResultSet implementan AutoCloseable. Cuando el bloque
 * termina, Java invoca close() automaticamente en orden inverso, incluso si
 * ocurre una excepcion. Esto reduce fugas de memoria y de recursos nativos,
 * por ejemplo conexiones abiertas en el pool o cursores sin liberar.
 */
public final class JdbcConnectionManager {

    private JdbcConnectionManager() {
    }

    public static Connection openConnection(DatabaseConfig config) throws SQLException {
        return DriverManager.getConnection(
            config.url(),
            config.usuario(),
            config.password()
        );
    }

    public static Connection openConnectionFromEnvironment() throws SQLException {
        return openConnection(DatabaseConfig.fromEnvironment());
    }

    public static boolean validarConexion(DatabaseConfig config) throws SQLException {
        String sql = "SELECT 1";

        // En Java moderno, cada recurso se declara en el encabezado del try.
        // Al salir del bloque, el cierre ocurre automaticamente aunque falle la consulta.
        // Eso evita dejar sockets, cursores o conexiones abiertos en memoria.
        try (
            Connection connection = openConnection(config);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.next();
        }
    }

    public static String buscarNombrePorId(DatabaseConfig config, String idPersona) throws SQLException {
        String sql = "SELECT nombre FROM personas WHERE id = ?";

        // PreparedStatement parametriza el valor y ayuda a prevenir inyeccion SQL.
        try (
            Connection connection = openConnection(config);
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, idPersona);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString("nombre") : null;
            }
        }
    }

    public static boolean validarConexionLegacy(DatabaseConfig config) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnection(config);
            statement = connection.prepareStatement("SELECT 1");
            resultSet = statement.executeQuery();
            return resultSet.next();
        } finally {
            // Java 8 hacia atras solia requerir este cierre manual en finally.
            // Si el desarrollador olvidaba uno de estos bloques o no validaba null,
            // el recurso quedaba abierto y aparecian memory leaks o agotamiento
            // de conexiones disponibles en la base de datos.
            cerrarSilenciosamente(resultSet);
            cerrarSilenciosamente(statement);
            cerrarSilenciosamente(connection);
        }
    }

    private static void cerrarSilenciosamente(AutoCloseable resource) throws SQLException {
        if (resource == null) {
            return;
        }

        try {
            resource.close();
        } catch (Exception exception) {
            if (exception instanceof SQLException sqlException) {
                throw sqlException;
            }
            throw new SQLException("Error cerrando recurso JDBC", exception);
        }
    }
}
