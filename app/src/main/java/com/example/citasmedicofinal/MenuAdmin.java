package com.example.citasmedicofinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class MenuAdmin extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Spinner sp;
    ListView listview;
    AdapterListViewFiltroBusqueda adap;
    ArrayList<Paciente> busquedaPaciente = new ArrayList<>();
    ArrayList<Paciente> filtroCorreo = new ArrayList<>();
    ArrayList<Paciente> filtroConsulta = new ArrayList<>();
    ArrayList<Paciente> filtroHoras = new ArrayList<>();
    ArrayList<Paciente> filtroDias = new ArrayList<>();

    TextView cajaCotizacion;
    EditText editTextFiltro;
    Button botonMenuAdmin;
    Button btnRealizarBusqueda;
    String[] consultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        consultas = getResources().getStringArray(R.array.consultas);

        btnRealizarBusqueda = (Button)findViewById(R.id.botonRealizarBusqueda);
        botonMenuAdmin = (Button)findViewById(R.id.botonVolverAdmin);
        sp = (Spinner) findViewById(R.id.spinnerBuscarClientes);
        editTextFiltro = (EditText) findViewById(R.id.editTextFiltro);
        cajaCotizacion = (TextView) findViewById(R.id.cajaCotizacion);
        listview = (ListView)findViewById(R.id.listViewFiltro);


        crearSpinner();
        botonMenuAdmin.setOnClickListener(this);
        btnRealizarBusqueda.setOnClickListener(this);
    }

    public void crearSpinner(){

    }

    @Override
    public void onClick(View v) {


        if(v.getId()== R.id.botonVolverAdmin){
            Intent in = new Intent(getApplicationContext(), MenuUsuario.class);
            startActivity(in);
            finish();

        }else if(v.getId() == R.id.botonRealizarBusqueda){

            if(editTextFiltro.getText().toString().equals("")){

                editTextFiltro.setError(String.valueOf(R.string.error_caja_vacia));

            }else{

                cargarDatos();
            }
        }
    }

    /**
     * Cargamos todos los datos del JSON y cargamos un adaptador del listview uno datos
     * u otros dependiendo de las condiciones
     */
    public void cargarDatos(){

        db.collection("citas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        busquedaPaciente.add(new Paciente(
                                document.getData().get("Usuario").toString()
                                , document.getData().get("Consulta").toString()
                                , document.getData().get("Dia").toString()
                                , document.getData().get("Hora").toString()
                        ));
                    }
                    String condicionFiltro = sp.getSelectedItem().toString();
                    String datoaBuscar = editTextFiltro.getText().toString();

                    for (int i = 0; i < busquedaPaciente.size() ; i++) {

                        if(condicionFiltro.equals("Consulta")) {

                            if (busquedaPaciente.get(i).getConsulta().equals(datoaBuscar)) {

                                filtroConsulta.add(CargarPacienteFiltro(i));
                                adap = new AdapterListViewFiltroBusqueda(getApplicationContext(), filtroConsulta);
                                listview.setAdapter(adap);
                                cajaCotizacion.setText(String.valueOf(filtroConsulta.size()));
                                }
                            } else if (condicionFiltro.equals("Dia")) {

                                if(busquedaPaciente.get(i).getDia().equals(datoaBuscar.toUpperCase())){

                                    filtroDias.add(CargarPacienteFiltro(i));

                                    adap = new AdapterListViewFiltroBusqueda(getApplicationContext(), filtroDias);
                                    listview.setAdapter(adap);
                                    cajaCotizacion.setText(String.valueOf(filtroDias.size()));
                                }

                            } else if (condicionFiltro.equals("Hora")) {

                                if(busquedaPaciente.get(i).getHora().equals(datoaBuscar)){


                                    filtroHoras.add(CargarPacienteFiltro(i));

                                    adap = new AdapterListViewFiltroBusqueda(getApplicationContext(), filtroHoras);
                                    listview.setAdapter(adap);
                                    cajaCotizacion.setText(String.valueOf(filtroHoras.size()));
                                }

                            } else if (condicionFiltro.equals("Correo")) {

                            if(busquedaPaciente.get(i).getCorreo().equals(datoaBuscar)){

                                filtroCorreo.add(CargarPacienteFiltro(i));

                                adap = new AdapterListViewFiltroBusqueda(getApplicationContext(), filtroCorreo);
                                listview.setAdapter(adap);
                                cajaCotizacion.setText(String.valueOf(filtroCorreo.size()));
                                 }

                            }

                    }//final for
                }else{
                }
            }
        });
    }

    public void ModificarListView(){

    }

    /**
     * Metodo que devuelve el paciente correspondiente a las condiciones anteriores al
     * llamamiento de este metodo
     * @param i indice del for que recorre el mapeo de pacientes
     * @return retorna el objeto paciente
     */
    public Paciente CargarPacienteFiltro(int i){
        Paciente p  = new Paciente(
                busquedaPaciente.get(i).getCorreo(),
                busquedaPaciente.get(i).getConsulta(),
                busquedaPaciente.get(i).getDia(),
                busquedaPaciente.get(i).getHora());
        return p;
    }
}
