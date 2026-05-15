package com.riwi.talent.model;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrato DAO para aislar la capa de acceso a datos del resto de la aplicacion.
 * Esta interfaz encaja en el modelo dentro de una organizacion MVC.
 */
public interface EmpleadoDAO {

    Empleado insertar(Empleado empleado) throws SQLException;

    List<Empleado> listar() throws SQLException;

    Empleado buscarPorId(int id) throws SQLException;

    List<EmpleadoReporte> generarReporte() throws SQLException;

    boolean actualizar(Empleado empleado) throws SQLException;

    boolean eliminar(int id) throws SQLException;
}
