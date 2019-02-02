package com.example.citasmedicofinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SacarCita extends AppCompatActivity{


    String uid;
    ListView listview;
    Spinner sp;
    String [] arrayConsultas;
    ArrayList<Button> botones = new ArrayList<>();
    String email;
    FirebaseFirestore db;
    ArrayList<PacienteFinal> citasPaciente = new ArrayList<>();
    ArrayList<String> contenidoBotones = new ArrayList<>();
    ArrayList<PacienteFinal> citasMedicoCabezera = new ArrayList<>();
    ArrayAdapter<String> adapter;


    Button botonLunes;
    Button botonMartes;
    Button botonMiercoles;
    Button botonJueves;
    Button botonViernes;
    Button botonConfirmarCita;
    Button botonVolver;
    TextView cajaMostrarConsultaSeleccionada;

    /**
     * PARA QUE LAS CITAS Y DIAS DISPONIBLES SE RECARGEN DEBES DE VOLVER ATRAS Y VOLVER A ENTRAR
     * PENDIENTE DE ARREGLAR EN VERSIONES POSTERIORES
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacar_cita);

        botonLunes = (Button) findViewById(R.id.botonLunes2);
        botonMartes = (Button) findViewById(R.id.botonMartes2);
        botonMiercoles = (Button) findViewById(R.id.botonMiercoles2);
        botonJueves = (Button) findViewById(R.id.botonJueves2);
        botonViernes = (Button) findViewById(R.id.botonViernes2);
        botonConfirmarCita = (Button) findViewById(R.id.botonConfirmarCita);
        botonVolver  = (Button) findViewById(R.id.botonVolver);
        cajaMostrarConsultaSeleccionada = (TextView) findViewById(R.id.cajaMostrarCitaSeleccionada);

        botones.add(botonLunes);
        botones.add(botonMartes);
        botones.add(botonMiercoles);
        botones.add(botonJueves);
        botones.add(botonViernes);
        botones.add(botonConfirmarCita);

        for (int i = 0; i <botones.size() ; i++) {

            botones.get(i).setEnabled(false);
        }
        listview = findViewById(R.id.listView);
        sp = findViewById(R.id.spinner2);

        /**
         * CARGAMOS UN ARRAYLIST CON LOS DATOS DE NUESTRO JSON
         */
        db = FirebaseFirestore.getInstance();
        cargarDatos();
        getActualUsurio();

        /**
         * Accedemos al array del listView y lo almacenamos en array
         */
        arrayConsultas = getResources().getStringArray(R.array.consultas);

        /**
         * CREACION DE ADAPTER QUE RELLENA EL LISRVIEW
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayConsultas);
        listview.setAdapter(adapter);


        /**
         * EVENTO SALIR
         */
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), MenuUsuario.class);
                startActivity(in);
                finish();
            }
        });


        ResetColorBotones();

        /**
         * EVENTO LISTVIEW
         */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                //Debes de pulsar en el listView para poder inicializar el evento de boton
                switch (position){

                    case 0://consulta a MEDICO CABEZERA
                        ResetColorBotones();

                        for (int i = 0; i <almacenDatos.getPacientes().size() ; i++) {

                            if(almacenDatos.getPacientes().get(i).consulta.equals("Médico Cabecera")){

                                for (int x = 0; x <botones.size() ; x++) {

                                    botones.get(x).setEnabled(false);
                                }
                            }
                        }
                        botonConfirmarCita.setEnabled(false);
                        mostrarHorasNoDisponibles("Médico Cabecera");
                        mostrarDiasNoDisponibles("Médico Cabecera");

                        String consultaSeleccionada = (String) listview.getItemAtPosition(position);
                        eventoBotonDia(consultaSeleccionada);
                        break;


                    case 1://consulta a ENFERMERIA
                        ResetColorBotones();

                        for (int i = 0; i <almacenDatos.getPacientes().size() ; i++) {

                            if(almacenDatos.getPacientes().get(i).consulta.equals("Enfermería")){

                                for (int x = 0; x <botones.size() ; x++) {

                                    botones.get(x).setEnabled(false);
                                }
                            }
                        }

                        botonConfirmarCita.setEnabled(false);
                        mostrarHorasNoDisponibles("Enfermería");
                        mostrarDiasNoDisponibles("Enfermería");

                        String consultaSeleccionada2 = (String) listview.getItemAtPosition(position);
                        eventoBotonDia(consultaSeleccionada2);
                        break;

                    case 2://consulta a PSICOLOGO
                        ResetColorBotones();
                        for (int i = 0; i <almacenDatos.getPacientes().size() ; i++) {

                            if(almacenDatos.getPacientes().get(i).consulta.equals("Psicólogo")){

                                for (int x = 0; x <botones.size() ; x++) {

                                    botones.get(x).setEnabled(false);
                                }
                            }
                        }
                        mostrarHorasNoDisponibles("Psicólogo");
                        mostrarDiasNoDisponibles("Psicólogo");

                        String consultaSeleccionada3 = (String) listview.getItemAtPosition(position);
                        eventoBotonDia(consultaSeleccionada3);

                        break;
                }
            }
        });
    }

    public void mostrarHorasNoDisponibles(String Consulta) {

        //cajaMostrarConsultaSeleccionada.setText("");

        cajaMostrarConsultaSeleccionada.setText("3.Seleccione hora para " + Consulta);

        final ArrayList<String> dias = new ArrayList<>();

        //ERROR XQ ELIMINA LOS DIAS PARA TODOS LOS CLIENTES LMAO-------------------->

        for (int j = 3; j <= 20 ; j++) {

            dias.add(String.valueOf(j));
        }
        for (int i = 0; i <almacenDatos.getPacientes().size() ; i++) {

            for (int d = 0; d <dias.size() ; d++) {

                if(almacenDatos.getPacientes().get(i).hora.equals(String.valueOf(dias.get(d)))){

                    dias.remove(dias.get(d));

                }else{

                }
            }
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dias);
        sp.setAdapter(adapter);
    }

    /**
     * Cargamos los datos del json y actualizamos spinner
     */
    public void cargarDatos(){

        citasPaciente = new ArrayList<>();

        db.collection("citas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        citasPaciente.add(new PacienteFinal(
                                document.getData().get("Usuario").toString()
                                , document.getData().get("Consulta").toString()
                                , document.getData().get("Dia").toString()
                                , document.getData().get("Hora").toString()
                        ));

                        almacenDatos.setPacientes(citasPaciente);
                    }
                }else{
                }
                for (int i = 0; i <botones.size() ; i++) {

                    String contenido = botones.get(i).getText().toString();
                    System.out.println(contenido);
                    contenidoBotones.add(contenido);
                }
            }
        });
    }

    /**
     * Metodo que activa los eventos en todos los botones de los dias de la semana
     *
     * @param consultaSeleccionada Consulta selecionada en el listview
     */
    public void  eventoBotonDia(final String consultaSeleccionada){

        final int[] i = new int[1];
        final String[] contenido = {""};


        for (i[0] = 0; i[0] <botones.size()-1 ; i[0]++) {

            final int finalI = i[0];

            botones.get(i[0]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Button b = (Button)v;
                    b.setBackgroundColor(Color.parseColor("#FF0000"));
                    for (int j = 0; j < botones.size(); j++) {
                        botones.get(j).setEnabled(false);
                    }
                    contenido[0] = botones.get(finalI).getText().toString();
                    grabarDatos(contenido[0], consultaSeleccionada, v);;
                }
            });
        }
    }

    /**
     *
     * @param botonDiaPulsado Boton del dia de la semana
     * @param consultaSeleccionada Consulta seleccionada en el listview
     * @param v Vista del evento, necesaria para utilizar el AlertDialog despues de guardar la cita en la base de datos
     */
    public void grabarDatos(final String botonDiaPulsado, final String consultaSeleccionada, View v){

        botones.get(botones.size()-1).setEnabled(true);
        botonConfirmarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < botones.size(); i++) {
                    botones.get(i).setEnabled(false);
                }

                String horaSeleccionada = sp.getSelectedItem().toString();

                Map<String, Object> cita = new HashMap<>();
                cita.put("Usuario", email);
                cita.put("Consulta", consultaSeleccionada);
                cita.put("Dia", botonDiaPulsado);
                cita.put("Hora", horaSeleccionada);

                db.collection("citas").add(cita);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Cita Sacada con Éxito");

                builder.setMessage(" Consulta" + " :  " + consultaSeleccionada + "\n"
                        + " Dia           : " + "  " + botonDiaPulsado + "\n"
                        + " Hora        :" + "  " + " " + horaSeleccionada)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                finish();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void ResetColorBotones(){
        for (int i = 0; i <botones.size() ; i++) {

            botones.get(i).setBackgroundColor(Color.parseColor("#ffffff"));
                botones.get(i).setEnabled(true);
        }
    }
    /**
     * Recorremos los datos del JSON para indicar citas no disponibles mediante color rojo
     * @param Consulta Consulta del listview clockeado
     */
    public void mostrarDiasNoDisponibles(String Consulta){

        for (int i = 0; i < almacenDatos.getPacientes().size() ; i++) {

            if(almacenDatos.getPacientes().get(i).usuario.equals(email)){

            if( almacenDatos.getPacientes().get(i).consulta.equals(Consulta)){

                citasMedicoCabezera.add(new PacienteFinal( almacenDatos.getPacientes().get(i).usuario,
                        almacenDatos.getPacientes().get(i).consulta,  almacenDatos.getPacientes().get(i).dia
                        ,  almacenDatos.getPacientes().get(i).hora));

                if( almacenDatos.getPacientes().get(i).dia.equals("L")){

                    botonLunes.setBackgroundColor(Color.parseColor("#FF0000"));
                    botonLunes.setEnabled(false);
                }
                if( almacenDatos.getPacientes().get(i).dia.equals("MA")){

                    botonMartes.setBackgroundColor(Color.parseColor("#FF0000"));
                    botonMartes.setEnabled(false);
                }
                if ( almacenDatos.getPacientes().get(i).dia.equals("MI")){

                    botonMiercoles.setBackgroundColor(Color.parseColor("#FF0000"));
                    botonMiercoles.setEnabled(false);
                }
                if( almacenDatos.getPacientes().get(i).dia.equals("J")){

                    botonJueves.setBackgroundColor(Color.parseColor("#FF0000"));
                    botonJueves.setEnabled(false);
                }
                if( almacenDatos.getPacientes().get(i).dia.equals("V")){

                    botonViernes.setBackgroundColor(Color.parseColor("#FF0000"));
                    botonViernes.setEnabled(false);
                    }
                }//final if consulta
            }//final usuario en concreto
        }
    }


    public void getActualUsurio(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            uid = user.getUid();
        }else{

        }
    }
}
