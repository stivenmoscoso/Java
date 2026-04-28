package com.riwi.talent.controller;

import com.riwi.talent.model.Empleado;
import com.riwi.talent.model.EmpleadoDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * Mediador entre la vista de consola y el modelo persistente.
 * La vista entrega datos crudos y el controlador coordina la operacion adecuada.
 */
public final class EmpleadoController {

    private final EmpleadoDAO empleadoDAO;

    public EmpleadoController(EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    public Empleado crearEmpleado(String nombre, String correo, String lenguajePrincipal, double salarioMensual)
        throws SQLException {
        Empleado nuevoEmpleado = new Empleado(null, nombre, correo, lenguajePrincipal, salarioMensual);
        return empleadoDAO.insertar(nuevoEmpleado);
    }

    public List<Empleado> listarEmpleados() throws SQLException {
        return empleadoDAO.listar();
    }

    public Empleado buscarEmpleadoPorId(int id) throws SQLException {
        return empleadoDAO.buscarPorId(id);
    }

    public boolean actualizarEmpleado(
        int id,
        String nombre,
        String correo,
        String lenguajePrincipal,
        double salarioMensual
    ) throws SQLException {
        Empleado empleadoActualizado = new Empleado(id, nombre, correo, lenguajePrincipal, salarioMensual);
        return empleadoDAO.actualizar(empleadoActualizado);
    }

    public boolean eliminarEmpleado(int id) throws SQLException {
        return empleadoDAO.eliminar(id);
    }
}
