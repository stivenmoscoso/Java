package com.java;

import java.util.List;
import java.util.Locale;

public class Perfiles {

    public static void main(String[] args) {
        List<Persona> personas = List.of(
            new Desarrollador(
                new DatosPersona("EMP-001", "Ana Torres", "ana@empresa.com"),
                "Java"
            ),
            new Gerente(
                new DatosPersona("EMP-002", "Luis Mejia", "luis@empresa.com"),
                125000.0
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
            System.out.println("Compensacion estimada: " + formatearMonto(persona.calcularCompensacionMensual()));
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
        System.out.println();

        System.out.println("=== Interfaces: bonos de ascenso + default log ===");
        personas.stream()
            .filter(Promocionable.class::isInstance)
            .map(Promocionable.class::cast)
            .forEach(promocionable -> {
                System.out.println("Bono calculado: " + formatearMonto(promocionable.calcularBonoAscenso()));
                System.out.println(promocionable.registrarLogPromocion());
                System.out.println();
            });

        System.out.println("=== Polimorfismo: validacion legacy vs pattern matching ===");
        personas.forEach(persona -> {
            System.out.println(validarPerfilLegacy(persona));
            System.out.println(validarPerfilModerno(persona));
            System.out.println();
        });
    }

    public static String describirPerfil(Persona persona) {
        return switch (persona) {
            case Desarrollador desarrollador -> """
                Desarrollador:
                - Nombre: %s
                - Lenguaje principal: %s
                """.formatted(
                    desarrollador.nombre(),
                    desarrollador.lenguajePrincipal()
                );
            case Gerente gerente -> """
                Gerente:
                - Nombre: %s
                - Presupuesto mensual: %s
                """.formatted(
                    gerente.nombre(),
                    formatearMonto(gerente.presupuestoMensual())
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
            case Desarrollador desarrollador ->
                desarrollador.lenguajePrincipal().equalsIgnoreCase("java") ? 4.8 : 4.5;
            case Gerente gerente ->
                gerente.presupuestoMensual() >= 100000 ? 4.7 : 4.3;
            case ConsultorExterno consultor ->
                consultor.especialidad().length() > 20 ? 4.7 : 4.4;
        };
        return Math.round(promedio * 100.0) / 100.0;
    }

    private static String generarFeedback(Persona persona) {
        return switch (persona) {
            case Desarrollador desarrollador ->
                "Mantiene un desempeno consistente trabajando con "
                    + desarrollador.lenguajePrincipal();
            case Gerente gerente ->
                "Gestiona un presupuesto mensual de "
                    + formatearMonto(gerente.presupuestoMensual()) + " con liderazgo estable";
            case ConsultorExterno consultor ->
                "Aporta conocimiento especializado en " + consultor.especialidad()
                    + " para la firma " + consultor.empresaConsultora();
        };
    }

    public static String validarPerfilLegacy(Persona persona) {
        // Java 8/11: el flujo clasico obliga a validar con instanceof
        // y luego repetir un cast manual para poder usar el comportamiento concreto.
        if (persona instanceof Desarrollador) {
            Desarrollador desarrollador = (Desarrollador) persona;
            return "Legacy -> " + desarrollador.nombre()
                + " desarrolla principalmente en " + desarrollador.getLenguaje();
        }

        // El mismo patron repetitivo aparece para cada subtipo concreto.
        if (persona instanceof Gerente) {
            Gerente gerente = (Gerente) persona;
            return "Legacy -> " + gerente.nombre()
                + " administra un presupuesto de " + formatearMonto(gerente.getPresupuestoMensual());
        }

        return "Legacy -> perfil sin reglas de validacion especificas";
    }

    public static String validarPerfilModerno(Persona persona) {
        // Java 17/21: Pattern Matching for instanceof une comprobacion
        // y variable tipada en una sola expresion, con menos ruido y menos casts.
        if (persona instanceof Desarrollador desarrollador) {
            return "Moderno -> " + desarrollador.nombre()
                + " desarrolla principalmente en " + desarrollador.lenguajePrincipal();
        }

        // El codigo queda mas directo porque la variable ya llega con el tipo correcto.
        if (persona instanceof Gerente gerente) {
            return "Moderno -> " + gerente.nombre()
                + " administra un presupuesto de " + formatearMonto(gerente.presupuestoMensual());
        }

        return "Moderno -> perfil sin reglas de validacion especificas";
    }

    private static String formatearMonto(double valor) {
        return String.format(Locale.US, "$%,.2f", valor);
    }
}

interface Promocionable {
    double calcularBonoAscenso();

    // Desde Java 8 una interfaz puede evolucionar con metodos default sin forzar
    // cambios en todas las clases existentes que ya implementaban el contrato.
    default String registrarLogPromocion() {
        return "LOG -> bono de ascenso calculado para " + getClass().getSimpleName();
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
 * de negocio y permite al compilador validar exhaustividad en switch y pattern matching.
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

final class Desarrollador extends Empleado implements Promocionable {
    private final String lenguajePrincipal;

    Desarrollador(DatosPersona datos, String lenguajePrincipal) {
        super(datos);
        this.lenguajePrincipal = lenguajePrincipal;
    }

    public String lenguajePrincipal() {
        return lenguajePrincipal;
    }

    // Getter estilo JavaBean para contrastar con el acceso moderno via pattern matching.
    public String getLenguaje() {
        return lenguajePrincipal;
    }

    @Override
    public double calcularCompensacionMensual() {
        return "java".equalsIgnoreCase(lenguajePrincipal) ? 7600 : 7000;
    }

    @Override
    public double calcularBonoAscenso() {
        return "java".equalsIgnoreCase(lenguajePrincipal) ? 1800 : 1200;
    }
}

final class Gerente extends Empleado implements Promocionable {
    private final double presupuestoMensual;

    Gerente(DatosPersona datos, double presupuestoMensual) {
        super(datos);
        this.presupuestoMensual = presupuestoMensual;
    }

    public double presupuestoMensual() {
        return presupuestoMensual;
    }

    // Getter legacy para ilustrar el cast manual del enfoque Java 8/11.
    public double getPresupuestoMensual() {
        return presupuestoMensual;
    }

    @Override
    public double calcularCompensacionMensual() {
        return 9000 + (presupuestoMensual * 0.02);
    }

    @Override
    public double calcularBonoAscenso() {
        return presupuestoMensual * 0.05;
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
