package com.example.citasmedicofinal;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * HILO QUE EJECUTARA UNA BARRA DE CARGA EN EL BACKGROUND MIENTRAS NOS LOGEAMOS O REGISTRAMOS
 * EN NUESTRA APLICACIÓN
 */
class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

    int counter;
    ProgressBar pb;
    TextView txtbarra;

    MyAsyncTask(int conunter, ProgressBar pb, TextView txtbarra) {


        this.counter = conunter;
        this.pb = pb;
        this.txtbarra = txtbarra;
    }

    @Override
    protected String doInBackground(Integer... params) {
        for (; counter <= params[0]; counter++) {
            try {
                Thread.sleep(1000);
                publishProgress(counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf("Error iniciando Sesión");
    }
    @Override
    protected void onPostExecute(String result) {
        this.pb.setVisibility(View.GONE);
        txtbarra.setText(result);
    }
    @Override
    protected void onPreExecute() {
        txtbarra.setText(R.string.hilo_cargando);
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        txtbarra.setText(R.string.hilo_iniciando);
        this.pb.setProgress(values[0]);
    }
}
