package com.java;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ArquitecturaDinamica {
    // Ahora se modela con una coleccion dinamica de empleados
    // en lugar de arreglos fijos.
    static final ArrayList<Empleado> EMPLEADOS = new ArrayList<>(Arrays.asList(
        new Empleado("COD001", "Ana", new ArrayList<>(Arrays.asList(4.5, 4.7, 4.8))),
        new Empleado("COD002", "Luis", new ArrayList<>(Arrays.asList(3.9, 4.1, 4.0))),
        new Empleado("COD003", "Marta", new ArrayList<>(Arrays.asList(4.9, 4.6, 4.7)))
    ));
    // List.of() crea una lista inmutable mas segura que un ArrayList tradicional para datos fijos,
    // porque evita modificaciones accidentales desde otras partes del programa. Como es inmutable,
    // no permite operaciones como .add(), .remove() o .set().
    static final List<String> TECNOLOGIAS = List.of("Java", "Spring", "SQL", "Git");
    // Map.of tambien crea un mapa inmutable, ideal para sedes fijas que no deben alterarse
    // durante la ejecucion del sistema.
    static final Map<String, String> SEDES = Map.of(
        "BOG", "Bogota",
        "MED", "Medellin",
        "CAL", "Cali"
    );
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

    public static void mostrarReporteDesempeno() {
        System.out.println("Tecnologias base: " + TECNOLOGIAS);
        System.out.println("Sedes disponibles: " + SEDES);
        System.out.println();

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
            System.out.println("=== Menu de Empleados ===");
            System.out.println("1. Agregar coder");
            System.out.println("2. Buscar coder por ID");
            System.out.println("3. Generar reporte");
            System.out.println("4. Gestion de coders");
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
            } else if (opcion == 0) {
                System.out.println("Saliendo del sistema...");
            } else if (opcion < 0 || opcion > 4) {
                System.out.println("La opcion debe estar entre 0 y 4.");
            }

            salir = opcion == 0;

            System.out.println();
        } while (!salir);

        scanner.close();
    }
}
