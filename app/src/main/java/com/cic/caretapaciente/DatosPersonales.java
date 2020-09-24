package com.cic.caretapaciente;

public class DatosPersonales {
    private String nombreCompleto;
    private int edad;
    private String telefono;
    private int peso;
    private int altura;
    private String fechaInicio;
    private String calleDomicilio;
    private String colonia;
    private int cp;


    public DatosPersonales(){
        this.nombreCompleto="";
        this.edad= 0;
        this.telefono="";
        this.peso=0;
        this.altura=0;
        this.fechaInicio="";
        this.calleDomicilio="";
        this.colonia="";
        this.cp=0;
    }

    public DatosPersonales(String nombreCompleto,int edad, String telefono, int peso, int altura, String fechaInicio, String calleDomicilio, String colonia, int cp)
    {
        this.nombreCompleto=nombreCompleto;
        this.edad= edad;
        this.telefono= telefono;
        this.peso= peso;
        this.altura= altura;
        this.fechaInicio= fechaInicio;
        this.calleDomicilio= calleDomicilio;
        this.colonia= colonia;
        this.cp= cp;
    }



   // Geters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getPeso() {
        return peso;
    }

    public int getAltura() {
        return altura;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getCalleDomicilio() {
        return calleDomicilio;
    }

    public String getColonia() {
        return colonia;
    }

    public int getCp() {
        return cp;
    }



    //Seters
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setCalleDomicilio(String calleDomicilio) {
        this.calleDomicilio = calleDomicilio;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }
}
