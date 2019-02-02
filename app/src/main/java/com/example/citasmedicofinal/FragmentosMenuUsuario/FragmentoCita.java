package com.example.citasmedicofinal.FragmentosMenuUsuario;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.citasmedicofinal.R;
import com.example.citasmedicofinal.SacarCita;

public class FragmentoCita extends Fragment {

    Button botonSacarCita;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public FragmentoCita() {
    }

    public static FragmentoCita newInstance(String param1, String param2) {
        FragmentoCita fragment = new FragmentoCita();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragmento_cita, container, false);
        botonSacarCita = (Button) v.findViewById(R.id.botonSacarCita);
        botonSacarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SacarCita.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //FINISH?
            }
        });
        return v;
    }

    public Button getBotonSacarCita() {
        return botonSacarCita;
    }

    public void setBotonSacarCita(Button botonSacarCita) {
        this.botonSacarCita = botonSacarCita;
    }
}
