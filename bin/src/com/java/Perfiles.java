package com.java;

import java.util.List;

public class Perfiles {

    public static void main(String[] args) {
        List<Persona> personas = List.of(
            new Desarrollador(
                new DatosPersona("EMP-001", "Ana Torres", "ana@empresa.com"),
                "Backend",
                List.of("Java", "Spring", "SQL")
            ),
            new Gerente(
                new DatosPersona("EMP-002", "Luis Mejia", "luis@empresa.com"),
                "Plataforma Digital",
                12
            ),
            new ConsultorExterno(
                new DatosPersona("EXT-003", "Sofia Rojas", "sofia@consultora.com"),
                "Arquitectura de Software",
                "Tech Advisors"
            )
        );

        System.out.println("=== Estilo Moderno: jerarquia sellada + records ===");
        personas.forEach(persona -> {
            System.out.println(describirPerfil(persona));
            System.out.println("Compensacion estimada: " + persona.calcularCompensacionMensual());
            System.out.println();
        });

        PersonaLegacy legado = new DemoLegacy("LEG-001", "Perfil abierto");
        System.out.println("=== Referencia Legacy (Java 8/11) ===");
        System.out.println(legado.resumen());
        System.out.println();

        DesempenoReportLegacy reporteLegacy = new DesempenoReportLegacy(
            1,
            4.6,
            "Cumple objetivos del mes con seguimiento manual"
        );
        System.out.println("=== Sintaxis Legacy: POJO tradicional ===");
        System.out.println(reporteLegacy);
        System.out.println();

        System.out.println("=== Fin de mes: reportes inmutables con record ===");
        emitirReportesFinDeMes(personas).forEach(System.out::println);
    }

    public static String describirPerfil(Persona persona) {
        return switch (persona) {
            case Desarrollador desarrollador -> """
                Desarrollador:
                - Nombre: %s
                - Area: %s
                - Stack: %s
                """.formatted(
                    desarrollador.nombre(),
                    desarrollador.area(),
                    String.join(", ", desarrollador.tecnologias())
                );
            case Gerente gerente -> """
                Gerente:
                - Nombre: %s
                - Unidad: %s
                - Personas a cargo: %d
                """.formatted(
                    gerente.nombre(),
                    gerente.unidadNegocio(),
                    gerente.personasACargo()
                );
            case ConsultorExterno consultor -> """
                Consultor Externo:
                - Nombre: %s
                - Especialidad: %s
                - Empresa: %s
                """.formatted(
                    consultor.nombre(),
                    consultor.especialidad(),
                    consultor.empresaConsultora()
                );
        };
    }

    public static List<DesempeñoReport> emitirReportesFinDeMes(List<Persona> personas) {
        return personas.stream()
            .map(persona -> new DesempeñoReport(
                extraerIdNumerico(persona.id()),
                calcularPromedioDesempeno(persona),
                generarFeedback(persona)
            ))
            .toList();
    }

    private static int extraerIdNumerico(String id) {
        return Integer.parseInt(id.replaceAll("\\D", ""));
    }

    private static double calcularPromedioDesempeno(Persona persona) {
        double promedio = switch (persona) {
            case Desarrollador desarrollador -> Math.min(5.0, 4.1 + (desarrollador.tecnologias().size() * 0.2));
            case Gerente gerente -> Math.min(5.0, 4.0 + (gerente.personasACargo() * 0.03));
            case ConsultorExterno consultor -> consultor.especialidad().length() > 20 ? 4.7 : 4.4;
        };
        return Math.round(promedio * 100.0) / 100.0;
    }

    private static String generarFeedback(Persona persona) {
        return switch (persona) {
            case Desarrollador desarrollador ->
                "Entrega continua destacada en " + desarrollador.area()
                    + " con dominio de " + String.join(", ", desarrollador.tecnologias());
            case Gerente gerente ->
                "Liderazgo efectivo en " + gerente.unidadNegocio()
                    + " con supervision de " + gerente.personasACargo() + " colaboradores";
            case ConsultorExterno consultor ->
                "Aporta conocimiento especializado en " + consultor.especialidad()
                    + " para la firma " + consultor.empresaConsultora();
        };
    }
}

abstract class PersonaLegacy {
    private final String identificador;
    private final String nombre;

    protected PersonaLegacy(String identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract String resumen();
}

final class DemoLegacy extends PersonaLegacy {
    DemoLegacy(String identificador, String nombre) {
        super(identificador, nombre);
    }

    @Override
    public String resumen() {
        return "Legacy -> " + getIdentificador() + " / " + getNombre()
            + " (cualquier clase podria extender PersonaLegacy sin restricciones)";
    }
}

final class DesempenoReportLegacy {
    private final int idEmpleado;
    private final double promedio;
    private final String feedback;

    DesempenoReportLegacy(int idEmpleado, double promedio, String feedback) {
        this.idEmpleado = idEmpleado;
        this.promedio = promedio;
        this.feedback = feedback;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public double getPromedio() {
        return promedio;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "DesempenoReportLegacy{"
            + "idEmpleado=" + idEmpleado
            + ", promedio=" + promedio
            + ", feedback='" + feedback + '\''
            + '}';
    }
}

/*
 * Sealed Classes protegen mejor la API porque el dominio define de forma explicita
 * quienes pueden heredar. A diferencia de una jerarquia abierta de Java 8/11,
 * esto evita extensiones inesperadas, hace el modelo mas predecible para reglas
 * de negocio y permite al compilador validar exhaustividad en switch/pattern matching.
 */
sealed abstract class Persona permits Empleado, ConsultorExterno {
    private final DatosPersona datos;

    protected Persona(DatosPersona datos) {
        this.datos = datos;
    }

    public final String id() {
        return datos.id();
    }

    public final String nombre() {
        return datos.nombre();
    }

    public final String correo() {
        return datos.correo();
    }

    public abstract double calcularCompensacionMensual();
}

sealed abstract class Empleado extends Persona permits Desarrollador, Gerente {
    protected Empleado(DatosPersona datos) {
        super(datos);
    }
}

final class Desarrollador extends Empleado {
    private final String area;
    private final List<String> tecnologias;

    Desarrollador(DatosPersona datos, String area, List<String> tecnologias) {
        super(datos);
        this.area = area;
        this.tecnologias = List.copyOf(tecnologias);
    }

    public String area() {
        return area;
    }

    public List<String> tecnologias() {
        return tecnologias;
    }

    @Override
    public double calcularCompensacionMensual() {
        return 6500 + (tecnologias.size() * 350);
    }
}

final class Gerente extends Empleado {
    private final String unidadNegocio;
    private final int personasACargo;

    Gerente(DatosPersona datos, String unidadNegocio, int personasACargo) {
        super(datos);
        this.unidadNegocio = unidadNegocio;
        this.personasACargo = personasACargo;
    }

    public String unidadNegocio() {
        return unidadNegocio;
    }

    public int personasACargo() {
        return personasACargo;
    }

    @Override
    public double calcularCompensacionMensual() {
        return 9000 + (personasACargo * 180);
    }
}

final class ConsultorExterno extends Persona {
    private final String especialidad;
    private final String empresaConsultora;

    ConsultorExterno(DatosPersona datos, String especialidad, String empresaConsultora) {
        super(datos);
        this.especialidad = especialidad;
        this.empresaConsultora = empresaConsultora;
    }

    public String especialidad() {
        return especialidad;
    }

    public String empresaConsultora() {
        return empresaConsultora;
    }

    @Override
    public double calcularCompensacionMensual() {
        return 8000;
    }
}

record DatosPersona(String id, String nombre, String correo) {
    DatosPersona {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
    }
}

record DesempeñoReport(int idEmpleado, double promedio, String feedback) {
    DesempeñoReport {
        if (idEmpleado <= 0) {
            throw new IllegalArgumentException("El idEmpleado debe ser positivo");
        }
        if (promedio < 0 || promedio > 5) {
            throw new IllegalArgumentException("El promedio debe estar entre 0 y 5");
        }
        if (feedback == null || feedback.isBlank()) {
            throw new IllegalArgumentException("El feedback es obligatorio");
        }
    }
}
