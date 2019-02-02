package com.example.citasmedicofinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;



public class CancelarCita extends AppCompatActivity {

    boolean precisaMostrar = false;
    QueryDocumentSnapshot documentt;
    FirebaseFirestore db;
    String email;
    ListView listview;
    ArrayList<ConsultasReservadas> consultas = new ArrayList<>();
    AdapterListViewConsultasReservadas adap;
    Button botonVolver;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_cita);
        listview = (ListView)findViewById(R.id.listviewConsultas);
        botonVolver = (Button) findViewById(R.id.botonVolver);
        c = this;


        CargarDatos();


        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), MenuUsuario.class);
                startActivity(in);
                finish();
            }
        });

    }

    public void CargarDatos(){

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();

            db.collection("citas")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    if(document.get("Usuario").toString().equals(email)){

                                        precisaMostrar = true;
                                        consultas.add(new ConsultasReservadas(
                                                document.getData().get("Usuario").toString()
                                                , document.getData().get("Consulta").toString()
                                                , document.getData().get("Dia").toString()
                                                , document.getData().get("Hora").toString()
                                        ));
                                    }
                                }

                            } else {
                            }
                            if(precisaMostrar == true){

                                adap = new AdapterListViewConsultasReservadas(
                                        getApplicationContext(), consultas);
                                listview.setAdapter(adap);

                                precisaMostrar = false;
                            }
                        }
                    });
        }
        EventoListView(listview, documentt);
    }

  public void EventoListView(final ListView l, final QueryDocumentSnapshot documentt){

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                ConsultasReservadas itemValue = (ConsultasReservadas) l.getItemAtPosition(position);


                db.collection("citas").whereEqualTo("Consulta", itemValue.getConsulta())
                        .whereEqualTo("Dia", itemValue.getDia())
                        .whereEqualTo("Hora", itemValue.getHora())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           for (QueryDocumentSnapshot document : task.getResult()) {

                                                              // documentt = document;

                                                               ArrayList<ConsultasReservadas> consultas = new ArrayList<>();
                                                               consultas.add(new ConsultasReservadas(
                                                                       document.getData().get("Usuario").toString()
                                                                       , document.getData().get("Consulta").toString()
                                                                       , document.getData().get("Dia").toString()
                                                                       , document.getData().get("Hora").toString()
                                                               ));

                                                               MostrarAlerta(consultas.get(0).getConsulta(),
                                                                       consultas.get(0).getDia(),
                                                                       consultas.get(0).getHora(),
                                                                       c, document);


                                                           }
                                                       }
                                                   }
                                               });
                             }
                     });
  }

  public void MostrarAlerta(String consultaSeleccionada, String botonDiaPulsado, String horaSeleccionada, Context c, final QueryDocumentSnapshot document){

      AlertDialog.Builder builder = new AlertDialog.Builder(c);

      builder.setTitle("¿Desea eliminar la cita?");

      builder.setMessage(" Consulta" + " :  " + consultaSeleccionada + "\n"
              + " Dia           : " + "   " + botonDiaPulsado + "\n"
              + " Hora        :" + "  " + " " + horaSeleccionada)
              .setCancelable(false)
              .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                      db.collection("citas").document(document.getId()).delete();
                      adap = new AdapterListViewConsultasReservadas(
                              getApplicationContext(), consultas);
                      listview.setAdapter(adap);
                      adap.notifyDataSetChanged();
                      finish();
                  }
              })

              .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {

                      finish();
                  }
              });
      AlertDialog alertDialog = builder.create();
      alertDialog.show();
  }

}


