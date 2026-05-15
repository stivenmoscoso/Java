package com.riwi.talent.controller;

import com.riwi.talent.model.Empleado;
import com.riwi.talent.model.EmpleadoDAO;
import com.riwi.talent.model.EmpleadoReporte;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

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

    public String generarReporteFinal() throws SQLException {
        List<EmpleadoReporte> reporte = empleadoDAO.generarReporte();

        if (reporte.isEmpty()) {
            return """
                ==============================
                REPORTE FINAL DE CODERS
                ==============================
                No hay datos registrados en la base de datos.
                """;
        }

        StringBuilder detalle = new StringBuilder();
        double nominaTotal = 0;

        for (EmpleadoReporte empleado : reporte) {
            nominaTotal += empleado.salarioMensual();
            detalle.append("""
                Id: %d
                Nombre: %s
                Correo: %s
                Lenguaje principal: %s
                Salario mensual: %s
                Categoria salarial: %s
                Alias corporativo: %s
                ------------------------------
                """.formatted(
                empleado.id(),
                empleado.nombre(),
                empleado.correo(),
                empleado.lenguajePrincipal(),
                String.format(Locale.US, "$%,.2f", empleado.salarioMensual()),
                empleado.categoriaSalarial(),
                empleado.aliasCorporativo()
            ));
        }

        return """
            ==============================
            REPORTE FINAL DE CODERS
            ==============================
            Total de registros: %d
            Nomina mensual consolidada: %s
            ------------------------------
            %s
            """.formatted(
            reporte.size(),
            String.format(Locale.US, "$%,.2f", nominaTotal),
            detalle
        );
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
