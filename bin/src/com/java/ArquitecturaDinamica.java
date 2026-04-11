package com.java;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ArquitecturaDinamica {
    // Ahora se modela con una coleccion dinamica de empleados
    // en lugar de arreglos fijos.
    static final ArrayList<Empleado> EMPLEADOS = new ArrayList<>(Arrays.asList(
        new Empleado("COD001", "Ana", new ArrayList<>(Arrays.asList(4.5, 4.7, 4.8))),
        new Empleado("COD002", "Luis", new ArrayList<>(Arrays.asList(3.9, 4.1, 4.0))),
        new Empleado("COD003", "Marta", new ArrayList<>(Arrays.asList(4.9, 4.6, 4.7)))
    ));
    // El HashMap permite ubicar un coder por su ID.
    static final HashMap<String, Empleado> EMPLEADOS_POR_ID = crearIndiceEmpleados();

    // Cada empleado guarda su nombre y sus calificaciones trimestrales.
    static class Empleado {
        // El ID funciona como clave unica para localizar al coder en el HashMap.
        String id;
        String nombre;
        ArrayList<Double> calificacionesTrimestrales;

        Empleado(String id, String nombre, ArrayList<Double> calificacionesTrimestrales) {
            this.id = id;
            this.nombre = nombre;
            this.calificacionesTrimestrales = calificacionesTrimestrales;
        }
    }

    public static HashMap<String, Empleado> crearIndiceEmpleados() {
        var indice = new HashMap<String, Empleado>();

        // Se construye una tabla clave-valor para acceder al empleado por ID.
        for (var empleado : EMPLEADOS) {
            indice.put(empleado.id, empleado);
        }

        return indice;
    }

    public static Empleado buscarEmpleadoPorId(String id) {
        // get devuelve el empleado asociado al ID o null si no existe.
        return EMPLEADOS_POR_ID.get(id);
    }

    public static void mostrarEmpleadoPorId(String id) {
        var empleado = buscarEmpleadoPorId(id);

        if (empleado == null) {
            System.out.println("No se encontro ningun coder con el ID: " + id);
            return;
        }

        System.out.println("Coder encontrado:");
        System.out.println("ID: " + empleado.id);
        System.out.println("Nombre: " + empleado.nombre);
        System.out.println("Calificaciones trimestrales: " + empleado.calificacionesTrimestrales);
    }

    public static boolean agregarEmpleado(Empleado empleado) {
        // Evita IDs duplicados antes de insertar en ambas colecciones.
        if (EMPLEADOS_POR_ID.containsKey(empleado.id)) {
            return false;
        }

        // Se agrega el empleado a la lista y al indice por ID.
        EMPLEADOS.add(empleado);
        EMPLEADOS_POR_ID.put(empleado.id, empleado);
        return true;
    }

    public static boolean eliminarEmpleadoPorId(String id) {
        // Primero se elimina del HashMap y luego de la lista para mantener consistencia.
        var empleado = EMPLEADOS_POR_ID.remove(id);

        if (empleado == null) {
            return false;
        }

        EMPLEADOS.remove(empleado);
        return true;
    }

    public static void listarEmpleados() {
        if (EMPLEADOS.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        // Se recorre la coleccion principal para mostrar todos los coders activos.
        System.out.println("=== Lista de coders ===");

        for (var empleado : EMPLEADOS) {
            System.out.println("ID: " + empleado.id + " | Nombre: " + empleado.nombre);
        }
    }

    public static ArrayList<Double> capturarCalificaciones(Scanner scanner) {
        var calificaciones = new ArrayList<Double>();

        // Se solicitan exactamente 3 calificaciones para conservar el formato trimestral.
        for (var i = 1; i <= 3; i++) {
            while (true) {
                System.out.print("Ingrese la calificacion del trimestre " + i + ": ");
                var entrada = scanner.nextLine().trim();

                try {
                    var calificacion = Double.parseDouble(entrada);
                    calificaciones.add(calificacion);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Debe ingresar un numero decimal valido.");
                }
            }
        }

        return calificaciones;
    }

    public static void registrarEmpleado(Scanner scanner) {
        System.out.print("Ingrese el ID del coder: ");
        var id = scanner.nextLine().trim().toUpperCase();

        if (id.isEmpty()) {
            System.out.println("El ID no puede estar vacio.");
            return;
        }

        if (EMPLEADOS_POR_ID.containsKey(id)) {
            System.out.println("Ya existe un coder con ese ID.");
            return;
        }

        System.out.print("Ingrese el nombre del coder: ");
        var nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }

        // Se construye el objeto y luego se registra en la lista y el mapa.
        var calificaciones = capturarCalificaciones(scanner);
        var empleado = new Empleado(id, nombre, calificaciones);

        if (agregarEmpleado(empleado)) {
            System.out.println("Coder registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar el coder.");
        }
    }

    public static void eliminarEmpleado(Scanner scanner) {
        System.out.print("Ingrese el ID del coder a eliminar: ");
        var id = scanner.nextLine().trim().toUpperCase();

        if (eliminarEmpleadoPorId(id)) {
            System.out.println("Coder eliminado correctamente.");
        } else {
            System.out.println("No existe un coder con ese ID.");
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

            System.out.println("ID: " + empleado.id);
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
                // La consulta ahora usa el HashMap para buscar por clave unica.
                System.out.print("Ingrese el ID del coder a consultar: ");
                var idBuscado = scanner.nextLine().trim().toUpperCase();
                mostrarEmpleadoPorId(idBuscado);
            } else if (opcion == 1) {
                // Permite crear un nuevo coder y guardarlo en ambas colecciones.
                registrarEmpleado(scanner);
            } else if (opcion == 3) {
                mostrarReporteDesempeno();
            } else if (opcion == 4) {
                // Menu corto de gestion para listar o eliminar registros existentes.
                System.out.println("1. Listar coders");
                System.out.println("2. Eliminar coder");
                System.out.print("Seleccione una opcion de gestion: ");

                var opcionGestion = scanner.nextLine().trim();

                if ("1".equals(opcionGestion)) {
                    listarEmpleados();
                } else if ("2".equals(opcionGestion)) {
                    eliminarEmpleado(scanner);
                } else {
                    System.out.println("Opcion de gestion no valida.");
                }
            } else if (opcion < 0 || opcion > 4) {
                System.out.println("La opcion debe estar entre 0 y 4.");
            }

            salir = opcion == 0;

            System.out.println();
        } while (!salir);

        scanner.close();
    }
}
