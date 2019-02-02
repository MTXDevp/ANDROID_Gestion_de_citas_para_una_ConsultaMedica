package com.example.citasmedicofinal;

import java.util.ArrayList;

public class almacenDatos {

    static ArrayList<PacienteFinal>pacientes = new ArrayList<>();

    public almacenDatos(){
    }

    public static ArrayList<PacienteFinal> getPacientes() {
        return pacientes;
    }

    public static void setPacientes(ArrayList<PacienteFinal> pacientes) {
        almacenDatos.pacientes = pacientes;
    }
}

