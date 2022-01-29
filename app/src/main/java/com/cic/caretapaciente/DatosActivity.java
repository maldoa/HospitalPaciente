package com.cic.caretapaciente;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatosActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private EditText idPaciente,saturacionOxigeno, temperatura,  capnografia, frecCardiaca, frecRespiratoria, alerta, preArtSistolica, preArtDiastolica;
    private Button send;

    private DatosRepository datosRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_signos);
        SharedPreferences sharedPref = getSharedPreferences("Datos",Context.MODE_PRIVATE);
        int user_value = sharedPref.getInt("ID_PACIENTE", 1);
        Log.d("id_usuario",""+user_value);
        idPaciente = findViewById(R.id.activity_id_paciente);
        saturacionOxigeno = findViewById(R.id.activity_saturacion_oxigeno);
        temperatura = findViewById(R.id.activity_temperatura);
        //capnografia = findViewById(R.id.activity_capnografia);
        frecCardiaca = findViewById(R.id.activity_frec_cardiaca);
        frecRespiratoria = findViewById(R.id.activity_frec_respiratoria);
        //alerta = findViewById(R.id.activity_alerta);
        preArtSistolica = findViewById(R.id.activity_pres_art_sistolica);
        preArtDiastolica = findViewById(R.id.activity_pres_art_diastolica);
        send = findViewById(R.id.activity_comments_send);

        datosRepository = DatosRepository.getInstance();
        idPaciente.setText(""+user_value);
        send.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Datos d1 = new Datos(
                        Integer.parseInt(idPaciente.getText().toString()),
                        Float.parseFloat(saturacionOxigeno.getText().toString()),
                        Float.parseFloat(temperatura.getText().toString()),
                        0,
                        Integer.parseInt(frecCardiaca.getText().toString()),
                        Integer.parseInt(frecRespiratoria.getText().toString()),
                        //Boolean.parseBoolean(alerta.getText().toString()),
                        false,
                        Integer.parseInt(preArtSistolica.getText().toString()),
                        Integer.parseInt(preArtDiastolica.getText().toString())
                );

                Datos d = new  Datos(
                        1,
                        92.0,
                        36.1,
                        40,
                        66,
                        18,
                        true,
                        83,
                        66
                );


                datosRepository.getCommentsService().createComment(d1).enqueue(new Callback<Datos>() {


                    @Override
                    public void onResponse(Call<Datos> call, Response<Datos> r) {
                        Toast.makeText(getApplicationContext(), "Actualización de los datos", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Datos> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error de envío de datos " , Toast.LENGTH_SHORT).show();
                    }
                });

                return;







            }
        });
    }
}