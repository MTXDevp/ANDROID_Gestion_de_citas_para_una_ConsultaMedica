package com.example.citasmedicofinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private EditText cajaEmail;
    private EditText cajaContraseña;
    private TextView txtRegistrarse;
    private Context c;
    private ProgressBar progressBar;
    private TextView txtbarra;
    Integer counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = this;
        cajaEmail = (EditText) findViewById(R.id.email);
        cajaContraseña = (EditText) findViewById(R.id.password);
        txtbarra = (TextView)findViewById(R.id.txtbarra);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        progressBar.setMax(10);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        cajaContraseña.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin("");
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cajaEmail.equals("prueba2DAM1819@gmail.com")){
                    Intent intent = new Intent(getApplicationContext(), MenuAdmin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                String pulsaLogin = "l";
                attemptLogin(pulsaLogin);
            }
        });

        txtRegistrarse = (TextView) findViewById(R.id.registrarse);
        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pulsaRegistrar = "r";
                attemptLogin(pulsaRegistrar);
            }
        });
    }

    /**
     * VALIDACION DE LOS DATOS INTRODUCIDOS EN EL LOGIN
     * @param opcionPulsada opcion del login (IniciarSesion -- Registrarse)
     */
    private void attemptLogin(String opcionPulsada) {

        cajaEmail.setError(null);
        cajaContraseña.setError(null);

        String email = cajaEmail.getText().toString();
        String password = cajaContraseña.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            cajaContraseña.setError(getString(R.string.error_invalid_password));
            focusView = cajaContraseña;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            cajaEmail.setError(getString(R.string.error_field_required));
            focusView = cajaEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            cajaEmail.setError(getString(R.string.error_invalid_email));
            focusView = cajaEmail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            counter = 1;
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);

            //Llamamiento del hilo para mostrar la barra de carga
            new MyAsyncTask(counter, progressBar, txtbarra).execute(10);

        if(opcionPulsada.equals("r")){


            //Checkeamos la autentificacion mediante correo electrinico
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    if(user.isEmailVerified()){
                                        Log.d("CORREO VERIFICADO", "Verificacion de correo :success");
                                    }
                                } else {
                                    Log.w("REGISTRO FAIL", "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });



        }else if(opcionPulsada.equals("l")) {


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("LOGIN OK", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //EN EL CASO DE QUE EL LOGIN SEA CORRECTO INICIALIZAMOS MENU Y FINALIZAMOS EL MAIN

                                    Intent intent = new Intent(getApplicationContext(), MenuUsuario.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Log.w("LOGIN FAIL", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
        }
        }
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }
}



