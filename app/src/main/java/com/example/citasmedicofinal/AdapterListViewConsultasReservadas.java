package com.example.citasmedicofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListViewConsultasReservadas extends BaseAdapter {


    ArrayList<ConsultasReservadas> consultasReservadas;
    Context contexto;
    LayoutInflater layoutInflater;

    public AdapterListViewConsultasReservadas(Context context, ArrayList<ConsultasReservadas> consultasReservadas){
        super();
        this.contexto = context;
        this.consultasReservadas = consultasReservadas;
        layoutInflater = layoutInflater.from(context);
    }


    @Override
    public int getCount() {

        return consultasReservadas.size();
    }

    @Override
    public Object getItem(int position) {
        return consultasReservadas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(contexto).
            inflate(R.layout.row_view_consultas, parent, false);
            TextView muestraConsulta = (TextView)convertView.findViewById(R.id.muestraConsulta);
            TextView muestraDia = (TextView)convertView.findViewById(R.id.muestraDia);
            TextView muestraHora= (TextView)convertView.findViewById(R.id.muestraHora);

            ConsultasReservadas c = (ConsultasReservadas) getItem(position);

            muestraConsulta.setText(c.getConsulta());
            muestraDia.setText(c.getDia());
            muestraHora.setText(c.getHora());

        }
        return convertView;
    }

}
