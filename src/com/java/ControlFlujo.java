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

            var entrada = scanner.nextLine();
            int opcion;

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Debe ingresar un numero.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("Accion: Registrar usuario.");
                    break;
                case 2:
                    System.out.println("Accion: Consultar datos.");
                    break;
                case 3:
                    System.out.println("Accion: Generar reporte.");
                    break;
                case 4:
                    System.out.println("Accion: Configuracion.");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
                    break;
            }

            System.out.println();
        } while (!salir);

        scanner.close();
    }
}
