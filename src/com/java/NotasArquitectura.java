package com.java;

public class NotasArquitectura {
    // Proyecto pensado para Java 17/21 (LTS actuales). No hay build tool aqui,
    // pero el codigo y las notas asumen esas versiones modernas.

    // Diferencia de enfoque: Java 8 (Legacy) vs Java 17/21 (LTS actual)
    // Java 8: revoluciona con lambdas, Streams y Optional, pero mantiene
    // un estilo mas verboso y dependiente de patrones "boilerplate".
    // Java 17/21: consolida la concision y seguridad del modelo de dominio
    // con records, sealed classes, pattern matching y switch expressions.
    // La prioridad pasa a ser productividad, claridad de APIs y eficiencia
    // para ejecucion en nube (LTS estables y con mejoras continuas).

    // JVM y Garbage Collector: gestion de objetos y memoria
    // 1) Los objetos se crean en el heap, normalmente en la zona joven (Eden).
    // 2) La JVM usa TLABs (Thread-Local Allocation Buffers) para que cada hilo
    //    aloque objetos sin bloquear a otros, reduciendo contencion.
    // 3) Cuando Eden se llena, ocurre un "minor GC": se copian los objetos vivos
    //    a Survivor o se promueven a la zona vieja (Old) si sobrevivieron varias
    //    rondas.
    // 4) En Old, el GC (por ejemplo G1 o ZGC) busca pausas cortas, compacta o
    //    reubica objetos para reducir fragmentacion y optimizar el uso del heap.
    // 5) La JVM decide cuando recolectar segun heuristicas (tamanos, presion
    //    de memoria y objetivos de pausa), equilibrando rendimiento y latencia.

    private final String banner = """
    Proyecto pensado para Java 17/21 (LTS actuales). No hay build tool aqui,
    pero el codigo y las notas asumen esas versiones modernas.

    Diferencia de enfoque: Java 8 (Legacy) vs Java 17/21 (LTS actual)
    Java 8: revoluciona con lambdas, Streams y Optional, pero mantiene
    un estilo mas verboso y dependiente de patrones "boilerplate".
    Java 17/21: consolida la concision y seguridad del modelo de dominio
    con records, sealed classes, pattern matching y switch expressions.
    La prioridad pasa a ser productividad, claridad de APIs y eficiencia
    para ejecucion en nube (LTS estables y con mejoras continuas).

    JVM y Garbage Collector: gestion de objetos y memoria
    1) Los objetos se crean en el heap, normalmente en la zona joven (Eden).
    2) La JVM usa TLABs (Thread-Local Allocation Buffers) para que cada hilo
        aloque objetos sin bloquear a otros, reduciendo contencion.
    3) Cuando Eden se llena, ocurre un "minor GC": se copian los objetos vivos
    a Survivor o se promueven a la zona vieja (Old) si sobrevivieron varias
    rondas.
    4) En Old, el GC (por ejemplo G1 o ZGC) busca pausas cortas, compacta o
    reubica objetos para reducir fragmentacion y optimizar el uso del heap.
    5) La JVM decide cuando recolectar segun heuristicas (tamanos, presion
    de memoria y objetivos de pausa), equilibrando rendimiento y latencia.

    La transicion de Java 8 a las versiones 17 y 21 no es solo una actualizacion de "parches"; es un cambio de filosofia.
    Mientras que Java 8 fue la revolucion funcional, Java 17 y 21 representan la era de la concision, la eficiencia en la
    nube y la productividad del desarrollador.

    Aqui tienes el desglose de este cambio de enfoque:

    1. Modelado de Datos: De la Verbosidad a la Inmutabilidad
    En Java 8, crear un simple objeto de datos (DTO) requeria decenas de lineas de codigo (getters, setters, equals,
    hashCode, toString), o depender de librerias externas como Lombok.

    Enfoque Java 17/21: Se prioriza la inmutabilidad y la brevedad con los Records.
    Un record define el estado y Java genera el resto automaticamente.
    Aparecen las Sealed Classes, que permiten controlar que clases pueden extender de otras, ideal para definir
    jerarquias de dominio cerradas y seguras.

    2. Sintaxis y Expresividad: "Menos es Mas"
    Java siempre fue criticado por ser "muy hablado" (verboso). Las versiones actuales han adoptado caracteristicas
    de lenguajes modernos como Kotlin o Scala.

    Pattern Matching: En Java 8, usabas instanceof y luego tenias que hacer un cast manual. En Java 17/21, el cast
    ocurre en la misma linea.

    Switch Expressions: El switch ya no es solo una estructura de control ruidosa; ahora puede devolver valores y usar
    "arrow syntax" (->), eliminando el riesgo de olvidar un break.

    Text Blocks: Se acabo el sufrir concatenando Strings con + y \n para escribir un JSON o un SQL; ahora usas triple
    comilla.

    3. Rendimiento y Concurrencia: El Gran Salto (Project Loom)
    Este es quizas el cambio mas profundo a nivel de arquitectura.

    Java 8: Se enfoco en el paralelismo con Streams y CompletableFuture, pero seguiamos atados a los hilos del Sistema
    Operativo (costosos y limitados).

    Java 21 (Virtual Threads): Introduce hilos ligeros que permiten ejecutar millones de tareas concurrentes con un consumo
    de memoria minimo. Esto cambia las reglas del juego para aplicaciones que escalan en la nube, haciendo que el modelo
    "un hilo por solicitud" vuelva a ser eficiente sin la complejidad de la programacion reactiva.

    4. Gestion de Memoria y Modernizacion del Runtime
    G1GC como estandar: Aunque Java 8 lo tenia, en las versiones actuales el Garbage Collector G1 es mucho mas eficiente,
    y aparecen opciones como ZGC, diseniado para pausar milisegundos incluso en montones (heaps) de varios terabytes.

    Modularidad: Desde Java 9, el JDK esta modularizado. Esto significa que puedes crear imagenes de ejecucion minimas
    (usando jlink), ideales para contenedores Docker donde el tamanio importa.
    """;

    public void imprimirNotas() {
        System.out.println(banner);
    }
}
