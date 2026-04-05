package com.java;

import java.util.Scanner;

public class ControlFlujo {

    // En Java 8, el switch clasico usa break y existe el riesgo de "fall-through"
    // si se olvida un break entre casos. En Java 17/21, la switch expression con
    // -> es mas segura y mas breve, porque evita ese error por defecto.
    public static String obtenerCategoriaSalarial(int nivelSalarial) {
        return switch (nivelSalarial) {
            case 1 -> "Salario bajo";
            case 2 -> "Salario medio";
            case 3 -> "Salario alto";
            default -> "Categoria salarial no valida";
        };
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        boolean salir = false;

        do {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Consultar datos");
            System.out.println("3. Generar reporte");
            System.out.println("4. Configuracion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            // En Java 8 se declararia de forma explicita:
            // String entrada = scanner.nextLine();
            // En Java 11+, var reduce ruido sin perder claridad.
            var entrada = scanner.nextLine();
            int opcion;

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero.");
                continue;
            }

            // Otra comparacion:
            // Java 8: String accion = ...;
            // Java 11+: var accion = ...;
            var accion = switch (opcion) {
                case 1 -> "Accion: Registrar usuario.";
                case 2 -> "Accion: Consultar datos.";
                case 3 -> "Accion: Generar reporte.";
                case 4 -> "Accion: Configuracion.";
                case 0 -> "Saliendo del sistema...";
                default -> "Opcion no valida. Intente de nuevo.";
            };

            System.out.println(accion);
            salir = opcion == 0;

            System.out.println();
        } while (!salir);

        scanner.close();
    }
}
