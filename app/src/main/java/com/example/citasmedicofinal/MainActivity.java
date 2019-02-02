package com.example.citasmedicofinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import ai.api.AIListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ai.api.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {

    private AIService mAIService;
    private FirebaseAuth mAuth;
    private EditText cajaEmail;
    private EditText cajaContraseña;
    private TextView txtRegistrarse;
    private Context c;
    private ProgressBar progressBar;
    private TextView txtbarra;
    private TextToSpeech mTextToSpeech;
    Integer counter = 1;
    Button logearse;;
    Button grabar;

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
        logearse = (Button) findViewById(R.id.email_sign_in_button);
        grabar = (Button)findViewById(R.id.botonGrabar);



        final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration( "f1c58c25c0344f98abec9c28c5f5b73e",
        AIConfiguration.SupportedLanguages.Spanish,  ai.api.android.AIConfiguration.RecognitionEngine.System);

        mAIService = AIService.getService(this, config);
        mAIService.setListener(this);


        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("IA", "pulsas boton");
                mAIService.startListening();
            }
        });

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();




        cajaContraseña.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin("l", textView);
                    return true;
                }
                return false;
            }
        });


        logearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pulsaLogin = "l";
                attemptLogin(pulsaLogin, view);
            }
        });

        txtRegistrarse = (TextView) findViewById(R.id.registrarse);
        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pulsaRegistrar = "r";
                attemptLogin(pulsaRegistrar, v);
            }
        });
    }

    /**
     * VALIDACION DE LOS DATOS INTRODUCIDOS EN EL LOGIN
     * @param opcionPulsada opcion del login (IniciarSesion -- Registrarse)
     */
    private void attemptLogin(String opcionPulsada, final View view) {

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
        }else if(TextUtils.isEmpty(password)){
            cajaContraseña.setError(getString(R.string.error_field_required));
            focusView = cajaEmail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {



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
                                        Log.i("CORREO VERIFICADO", "Verificacion de correo :success");
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                                    builder.setTitle("ERROR");

                                    builder.setMessage("Correo en uso")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                        });



        }else if(opcionPulsada.equals("l")) {

            Log.v("totalan", "ENTRAS COMO LOGIN");


            if(cajaEmail.getText().toString().equals("admin@admin.com") && cajaContraseña.getText().toString().equals("administrador")){

                Intent intent = new Intent(getApplicationContext(), ActivityAdmin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }else{

                counter = 1;
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);

                //Llamamiento del hilo para mostrar la barra de carga
                new MyAsyncTask(counter, progressBar, txtbarra).execute(10);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //EN EL CASO DE QUE EL LOGIN SEA CORRECTO INICIALIZAMOS MENU Y FINALIZAMOS EL MAIN

                                    Intent intent = new Intent(getApplicationContext(), MenuUsuario.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {

                                }
                            }
                        });
                }
            }
        }
        //POSIBLE ERROR REQUIRES API ABAJO
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        Log.v("IA", "entra en evento on result");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(result.getFulfillment().getSpeech(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}



