package com.java;

public class ModeladoDatos {
    public static void main(String[] args) {

        // Text block para banner de introduccion
        String encabezado = """
        ==============================================
        MODELADO DE DATOS PRUEBA Stiven Moscoso
        ==============================================""";

        System.out.println(encabezado);
        
        EmpresaR empresa = new EmpresaR("Stiven S.A.", "1234567-1", 2026);
        System.out.println("Empresa: " + empresa.nombre() + ", NIT: " + empresa.nit() + ", Fundada: " + empresa.anoFundacion());
        
        double salarioBase = calcularSalarioFinal(2000000, 500000);
        System.out.println("Salario Final: " + salarioBase);

        // Definiendo los atributos (variables de instancia) de la clase Empleado
        class Empleado {
            private byte tipoByte;
            private short tipoShort;
            private int tipoInt;
            private long tipoLong;
            private float tipoFloat;
            private double tipoDouble;
            private char tipoChar;
            private boolean tipoBoolean;
            private String nombre;

        public Empleado(byte tipoByte, short tipoShort, int tipoInt, long tipoLong, float tipoFloat, double tipoDouble, char tipoChar, boolean tipoBoolean, String nombre) {
                this.tipoByte = tipoByte;
                this.tipoShort = tipoShort;
                this.tipoInt = tipoInt;
                this.tipoLong = tipoLong;
                this.tipoFloat = tipoFloat;
                this.tipoDouble = tipoDouble;
                this.tipoChar = tipoChar;
                this.tipoBoolean = tipoBoolean;
                this.nombre = nombre;
            }

            public byte getTipoByte() { return tipoByte; }
            public short getTipoShort() { return tipoShort; }
            public int getTipoInt() { return tipoInt; }
            public long getTipoLong() { return tipoLong; }
            public float getTipoFloat() { return tipoFloat; }
            public double getTipoDouble() { return tipoDouble; }
            public char getTipoChar() { return tipoChar; }
            public boolean isTipoBoolean() { return tipoBoolean; }
            public String getNombre() { return nombre; }
        }
    }

    /*
     * Orden de ejecucion:
     * 1) Parentesis
     * 2) Multiplicacion
     * 3) Suma/Resta
     */

    public static double calcularSalarioFinal(double salarioBase, double bonoMensual) {
        return (salarioBase + (bonoMensual * 1.10)) - (salarioBase * 0.05);
    }

    /*
     * Uso del modulo (%)
     * Si el ID es par, recibe bono extra
     */
    public static boolean tieneBonoExtra(int idEmpleado) {
        return idEmpleado % 2 == 0; // Ejemplo: empleados con ID par tienen bono extra
    }

    /*
     * Logica booleana compleja
     * Orden de precedencia:
     * 1. !
     * 2. &&
     * 3. ||
     */
    public static boolean validarElegibilidadBono(int puntajeTest, int edad, int idSede, boolean esActivo) {
        return (puntajeTest > 85 && edad < 30) || (idSede == 1 && !esActivo);
    }

    /*
     * Asignacion compuesta
     * Actualizamos el bono sumando un valor extra
     */
    public static double actualizarBono(double bonoMensual, double incremento) {
        bonoMensual += incremento; // Bono actualizado con incremento
        return bonoMensual;
    }
}

record EmpresaR(String nombre, String nit, int anoFundacion) {}
