package com.java;

/**
 * Record inmutable que representa al empleado persistido en la tabla coders.
 * El controlador puede usar este DTO para mover datos hacia y desde el DAO
 * sin exponer setters ni estados intermedios inconsistentes.
 */
public record Empleado(
    Integer id,
    String nombre,
    String correo,
    String lenguajePrincipal,
    double salarioMensual
) {

    public Empleado {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo cuando exista");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (lenguajePrincipal == null || lenguajePrincipal.isBlank()) {
            throw new IllegalArgumentException("El lenguaje principal es obligatorio");
        }
        if (salarioMensual < 0) {
            throw new IllegalArgumentException("El salario mensual no puede ser negativo");
        }
    }
}
