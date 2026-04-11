package com.java;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ArquitecturaDinamica {
    // La Semana 2 ahora se modela con una coleccion dinamica de empleados
    // en lugar de arreglos fijos.
    static final ArrayList<Empleado> EMPLEADOS = new ArrayList<>(Arrays.asList(
        new Empleado("Ana", new ArrayList<>(Arrays.asList(4.5, 4.7, 4.8))),
        new Empleado("Luis", new ArrayList<>(Arrays.asList(3.9, 4.1, 4.0))),
        new Empleado("Marta", new ArrayList<>(Arrays.asList(4.9, 4.6, 4.7)))
    ));

    // Cada empleado guarda su nombre y sus calificaciones trimestrales.
    static class Empleado {
        String nombre;
        ArrayList<Double> calificacionesTrimestrales;

        Empleado(String nombre, ArrayList<Double> calificacionesTrimestrales) {
            this.nombre = nombre;
            this.calificacionesTrimestrales = calificacionesTrimestrales;
        }
    }

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

    public static String validarRangoTipoPrimitivo(long valor) {
        if (valor >= Byte.MIN_VALUE && valor <= Byte.MAX_VALUE) {
            return "El valor cabe en byte, short, int y long.";
        } else if (valor >= Short.MIN_VALUE && valor <= Short.MAX_VALUE) {
            return "El valor cabe en short, int y long.";
        } else if (valor >= Integer.MIN_VALUE && valor <= Integer.MAX_VALUE) {
            return "El valor cabe en int y long.";
        } else {
            return "El valor solo cabe en long.";
        }
    }

    public static void mostrarReporteDesempeno() {
        // El foreach recorre directamente la lista dinamica de empleados.
        for (var empleado : EMPLEADOS) {
            var suma = 0.0;

            System.out.println("Coder: " + empleado.nombre);
            System.out.println("Calificaciones trimestrales: ");

            // Los for anidados permiten recorrer la matriz fila por fila
            // y calcular el promedio de desempeno de cada empleado.
            for (var j = 0; j < empleado.calificacionesTrimestrales.size(); j++) {
                var calificacion = empleado.calificacionesTrimestrales.get(j);
                suma += calificacion;
                System.out.println("Trimestre " + (j + 1) + ": " + calificacion);
            }

            var promedio = suma / empleado.calificacionesTrimestrales.size();
            // Casting explicito de double a int para generar un "Puntaje Simplificado".
            // Se pierde la parte decimal, por lo que disminuye la precision del valor original.
            var puntajeSimplificado = (int) promedio;
            var estadoPromocion = promedio >= 4.5 ? "Promovible" : "En seguimiento";

            System.out.println("Promedio general: " + promedio);
            System.out.println("Puntaje Simplificado (casting de double a int): " + puntajeSimplificado);
            System.out.println("Estado de promocion: " + estadoPromocion);
            System.out.println();
        }
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
                // En Java 8 los mensajes de error y rastros de excepcion solian ser menos
                // descriptivos. En Java 17/21 se mejoro el detalle diagnostico, lo que
                // facilita identificar con mas rapidez la causa exacta del problema.
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

            if (opcion == 2) {
                System.out.print("Ingrese un numero entero para validar su rango: ");
                var datoCapturado = scanner.nextLine();

                try {
                    var valor = Long.parseLong(datoCapturado);
                    System.out.println(validarRangoTipoPrimitivo(valor));
                } catch (NumberFormatException e) {
                    System.out.println("El dato ingresado no corresponde a un valor long valido.");
                }
            } else if (opcion == 3) {
                mostrarReporteDesempeno();
            } else if (opcion < 0 || opcion > 4) {
                System.out.println("La opcion debe estar entre 0 y 4.");
            }

            salir = opcion == 0;

            System.out.println();
        } while (!salir);

        scanner.close();
    }
}
