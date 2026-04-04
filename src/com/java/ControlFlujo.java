package com.java;

import java.util.Scanner;

public class ControlFlujo {

    public static String obtenerCategoriaSalarial(int nivelSalarial) {
        return switch (nivelSalarial) {
            case 1 -> "Salario bajo";
            case 2 -> "Salario medio";
            case 3 -> "Salario alto";
            default -> "Categoria salarial no valida";
        };
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Consultar datos");
            System.out.println("3. Generar reporte");
            System.out.println("4. Configuracion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion;
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                System.out.println("Entrada invalida. Debe ingresar un numero.");
                scanner.nextLine();
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
        }

        scanner.close();
    }
}
