package com.java;

public class ModeladoDatos{
    //definiendo los atributos (variables de instancia) de la clase Empleado
    class Empleado{
    private byte tipoByte;
    private short tipoShort;
    private int tipoInt;
    private long tipoLong;
    private float tipoFloat;
    private double tipoDouble;
    private char tipoChar;
    private boolean tipoBoolean;
    private String nombre;

    public Empleado(byte tipoByte, short tipoShort, int tipoInt, long tipoLong, float tipoFloat,
                    double tipoDouble, char tipoChar, boolean tipoBoolean, String nombre) {
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