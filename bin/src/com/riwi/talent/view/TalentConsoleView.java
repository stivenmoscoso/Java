package com.riwi.talent.view;

import com.riwi.talent.controller.EmpleadoController;
import com.riwi.talent.model.Empleado;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Vista de consola.
 * Toda la interaccion con Scanner se concentra aqui para respetar MVC.
 */
public final class TalentConsoleView {

    private final EmpleadoController controller;
    private final Scanner scanner;

    public TalentConsoleView(EmpleadoController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();

            try {
                continuar = procesarOpcion(opcion);
            } catch (IllegalArgumentException | SQLException exception) {
                System.out.println("No fue posible completar la operacion: " + exception.getMessage());
            }
        }
    }

    private boolean procesarOpcion(String opcion) throws SQLException {
        return switch (opcion) {
            case "1" -> {
                crearEmpleado();
                yield true;
            }
            case "2" -> {
                listarEmpleados();
                yield true;
            }
            case "3" -> {
                actualizarEmpleado();
                yield true;
            }
            case "4" -> {
                eliminarEmpleado();
                yield true;
            }
            case "5" -> {
                buscarEmpleadoPorId();
                yield true;
            }
            case "0" -> false;
            default -> {
                System.out.println("Opcion invalida. Intenta nuevamente.");
                yield true;
            }
        };
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("=== Riwi Talent - Gestion de Coders ===");
        System.out.println("1. Insertar coder");
        System.out.println("2. Listar coders");
        System.out.println("3. Actualizar coder");
        System.out.println("4. Eliminar coder");
        System.out.println("5. Buscar coder por id");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opcion: ");
    }

    private void crearEmpleado() throws SQLException {
        Empleado empleado = controller.crearEmpleado(
            leerTexto("Nombre"),
            leerTexto("Correo"),
            leerTexto("Lenguaje principal"),
            leerDouble("Salario mensual")
        );
        System.out.println("Coder creado con id " + empleado.id());
    }

    private void listarEmpleados() throws SQLException {
        List<Empleado> empleados = controller.listarEmpleados();

        if (empleados.isEmpty()) {
            System.out.println("No hay coders registrados.");
            return;
        }

        empleados.forEach(empleado -> System.out.println(formatearEmpleado(empleado)));
    }

    private void actualizarEmpleado() throws SQLException {
        int id = leerEntero("Id del coder a actualizar");
        boolean actualizado = controller.actualizarEmpleado(
            id,
            leerTexto("Nuevo nombre"),
            leerTexto("Nuevo correo"),
            leerTexto("Nuevo lenguaje principal"),
            leerDouble("Nuevo salario mensual")
        );
        System.out.println(actualizado ? "Coder actualizado correctamente." : "No se encontro el coder.");
    }

    private void eliminarEmpleado() throws SQLException {
        int id = leerEntero("Id del coder a eliminar");
        boolean eliminado = controller.eliminarEmpleado(id);
        System.out.println(eliminado ? "Coder eliminado correctamente." : "No se encontro el coder.");
    }

    private void buscarEmpleadoPorId() throws SQLException {
        int id = leerEntero("Id del coder a buscar");
        Empleado empleado = controller.buscarEmpleadoPorId(id);
        System.out.println(empleado != null ? formatearEmpleado(empleado) : "No se encontro el coder.");
    }

    private String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine().trim();
    }

    private int leerEntero(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private double leerDouble(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private String formatearEmpleado(Empleado empleado) {
        return "Id: " + empleado.id()
            + " | Nombre: " + empleado.nombre()
            + " | Correo: " + empleado.correo()
            + " | Lenguaje: " + empleado.lenguajePrincipal()
            + " | Salario: " + String.format(Locale.US, "$%,.2f", empleado.salarioMensual());
    }
}
