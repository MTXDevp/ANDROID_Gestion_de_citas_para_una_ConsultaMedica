package com.example.citasmedicofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListViewFiltroBusqueda extends BaseAdapter {

    ArrayList<Paciente> datosPaciente;
    Context contexto;
    LayoutInflater layoutInflater;

    public AdapterListViewFiltroBusqueda(Context context, ArrayList<Paciente> datosPaciente){
        super();
        this.contexto = context;
        this.datosPaciente = datosPaciente;
        layoutInflater = layoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datosPaciente.size();
    }

    @Override
    public Object getItem(int position) {
        return datosPaciente.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(contexto).
            inflate(R.layout.row_view_datos_paciente, parent, false);

            TextView cajaCorreo = (TextView)convertView.findViewById(R.id.cajaCorreoFiltro);
            TextView cajaConsulta = (TextView)convertView.findViewById(R.id.cajaConsultaFiltro);
            TextView cajaDia = (TextView)convertView.findViewById(R.id.cajaDiaFiltro);
            TextView cajaHora = (TextView)convertView.findViewById(R.id.cajaHoraFiltro);

            Paciente p = (Paciente) getItem(position);

            cajaCorreo.setText(p.getCorreo());
            cajaConsulta.setText(p.getConsulta());
            cajaDia.setText(p.getDia());
            cajaHora.setText(p.getHora());

        }
        return convertView;
    }
}
