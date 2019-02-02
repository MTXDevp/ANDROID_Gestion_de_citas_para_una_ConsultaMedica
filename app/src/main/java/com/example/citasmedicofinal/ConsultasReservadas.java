package com.example.citasmedicofinal;

public class ConsultasReservadas {

    String id;
    String consulta;
    String dia;
    String hora;

    public ConsultasReservadas(String id, String consulta, String dia, String hora) {
        this.id = id;
        this.consulta = consulta;
        this.dia = dia;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
