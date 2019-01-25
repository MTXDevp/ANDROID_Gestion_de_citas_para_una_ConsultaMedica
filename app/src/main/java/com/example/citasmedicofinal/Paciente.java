package com.example.citasmedicofinal;

public class Paciente {

    String correo;
    String consulta;
    String dia;
    String hora;

    public Paciente(String correo, String cosulta, String dia, String hora) {
        this.correo = correo;
        this.consulta = cosulta;
        this.dia = dia;
        this.hora = hora;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
