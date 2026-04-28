package com.riwi.talent.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion fisica del DAO usando JDBC.
 *
 * <p>Todas las operaciones usan PreparedStatement, incluso cuando reciben
 * datos simples como el id. Esto protege la aplicacion contra inyeccion SQL
 * y separa el SQL de los valores suministrados por el usuario.
 */
public final class JdbcEmpleadoDAO implements EmpleadoDAO {

    private static final String INSERT_SQL = """
        INSERT INTO coders (nombre, correo, lenguaje_principal, salario_mensual)
        VALUES (?, ?, ?, ?)
        """;

    private static final String LIST_SQL = """
        SELECT id, nombre, correo, lenguaje_principal, salario_mensual
        FROM coders
        ORDER BY id
        """;

    private static final String FIND_BY_ID_SQL = """
        SELECT id, nombre, correo, lenguaje_principal, salario_mensual
        FROM coders
        WHERE id = ?
        """;

    private static final String REPORT_SQL = """
        SELECT
            id,
            nombre,
            correo,
            lenguaje_principal,
            salario_mensual,
            CASE
                WHEN salario_mensual >= 9000 THEN 'Senior'
                WHEN salario_mensual >= 7000 THEN 'Mid'
                ELSE 'Junior'
            END AS categoria_salarial,
            UPPER(SUBSTRING(nombre, 1, 3)) || '-' || id AS alias_corporativo
        FROM coders
        ORDER BY salario_mensual DESC, nombre ASC
        """;

    private static final String UPDATE_SQL = """
        UPDATE coders
        SET nombre = ?, correo = ?, lenguaje_principal = ?, salario_mensual = ?
        WHERE id = ?
        """;

    private static final String DELETE_SQL = """
        DELETE FROM coders
        WHERE id = ?
        """;

    private final DatabaseConfig config;

    public JdbcEmpleadoDAO(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public Empleado insertar(Empleado empleado) throws SQLException {
        try (
            Connection connection = JdbcConnectionManager.openConnection(config);
            PreparedStatement statement = connection.prepareStatement(
                INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS
            )
        ) {
            statement.setString(1, empleado.nombre());
            statement.setString(2, empleado.correo());
            statement.setString(3, empleado.lenguajePrincipal());
            statement.setDouble(4, empleado.salarioMensual());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Empleado(
                        generatedKeys.getInt(1),
                        empleado.nombre(),
                        empleado.correo(),
                        empleado.lenguajePrincipal(),
                        empleado.salarioMensual()
                    );
                }
            }
        }

        throw new SQLException("No fue posible recuperar el id generado para el coder");
    }

    @Override
    public List<Empleado> listar() throws SQLException {
        List<Empleado> coders = new ArrayList<>();

        try (
            Connection connection = JdbcConnectionManager.openConnection(config);
            PreparedStatement statement = connection.prepareStatement(LIST_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                coders.add(mapearEmpleado(resultSet));
            }
        }

        return coders;
    }

    @Override
    public Empleado buscarPorId(int id) throws SQLException {
        try (
            Connection connection = JdbcConnectionManager.openConnection(config);
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapearEmpleado(resultSet) : null;
            }
        }
    }

    @Override
    public List<EmpleadoReporte> generarReporte() throws SQLException {
        List<EmpleadoReporte> reporte = new ArrayList<>();

        try (
            Connection connection = JdbcConnectionManager.openConnection(config);
            PreparedStatement statement = connection.prepareStatement(REPORT_SQL);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                // La consulta agrega columnas derivadas y el record permite mapearlas
                // de forma compacta sin crear un POJO mutable adicional solo para lectura.
                reporte.add(new EmpleadoReporte(
                    resultSet.getInt("id"),
                    resultSet.getString("nombre"),
                    resultSet.getString("correo"),
                    resultSet.getString("lenguaje_principal"),
                    resultSet.getDouble("salario_mensual"),
                    resultSet.getString("categoria_salarial"),
                    resultSet.getString("alias_corporativo")
                ));
            }
        }

        return reporte;
    }

    @Override
    public boolean actualizar(Empleado empleado) throws SQLException {
        if (empleado.id() == null) {
            throw new IllegalArgumentException("Para actualizar un coder se requiere id");
        }

        try (
            Connection connection = JdbcConnectionManager.openConnection(config);
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)
        ) {
            statement.setString(1, empleado.nombre());
            statement.setString(2, empleado.correo());
            statement.setString(3, empleado.lenguajePrincipal());
            statement.setDouble(4, empleado.salarioMensual());
            statement.setInt(5, empleado.id());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (
            Connection connection = JdbcConnectionManager.openConnection(config);
            PreparedStatement statement = connection.prepareStatement(DELETE_SQL)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Empleado mapearEmpleado(ResultSet resultSet) throws SQLException {
        return new Empleado(
            resultSet.getInt("id"),
            resultSet.getString("nombre"),
            resultSet.getString("correo"),
            resultSet.getString("lenguaje_principal"),
            resultSet.getDouble("salario_mensual")
        );
    }
}
