package com.riwi.talent.model;

/**
 * Record de lectura para consultas de reporte.
 *
 * <p>Con Java 17+, un record encaja muy bien con JDBC moderno porque el mapeo
 * queda concentrado en una sola linea y el objeto resultante ya nace inmutable.
 * Frente al POJO clasico de Java 8 con atributos, constructor vacio, setters,
 * getters y posibles estados parciales, este enfoque reduce codigo repetitivo,
 * hace mas simple el mantenimiento y vuelve mas evidente que el reporte solo
 * transporta datos de lectura sin logica mutable.
 */
public record EmpleadoReporte(
    int id,
    String nombre,
    String correo,
    String lenguajePrincipal,
    double salarioMensual,
    String categoriaSalarial,
    String aliasCorporativo
) {
}
