package com.cic.caretapaciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    // Datos peronales
    private TextInputEditText nombreCompleto;
    private TextInputEditText edad;
    private TextInputEditText telefono;
    private TextInputEditText peso;
    private TextInputEditText altura;
    private TextInputEditText fechaInicio;
    private TextInputEditText calleDomicilio;
    private TextInputEditText colonia;
    private TextInputEditText cp;

    //boton parte 1
    private Button siguiente1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // inicializar layout 1
        nombreCompleto = (TextInputEditText) findViewById(R.id.campoNombreCompleto);
        edad = (TextInputEditText) findViewById(R.id.campoEdad);
        telefono  = (TextInputEditText) findViewById(R.id.campoTelefono);
        peso = (TextInputEditText) findViewById(R.id.campoPeso);
        altura = (TextInputEditText) findViewById(R.id.campoAltura);
        fechaInicio = (TextInputEditText) findViewById(R.id.campoFechainicio);
        calleDomicilio = (TextInputEditText) findViewById(R.id.campoCalleNumero);
        colonia = (TextInputEditText) findViewById(R.id.campoColonia);
        cp = (TextInputEditText) findViewById(R.id.campoCP);

        siguiente1 = (Button) findViewById(R.id.botonSiguiente1);




    }






    // llamar layout 2
    @Override
    public void onClick(View view){
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(nombreCompleto.getText().toString());
        boolean bs = ms.matches();
        if (bs == false) {
            nombreCompleto.setError("error type");
            Toast.makeText(getApplicationContext(), "Please enter something !", Toast.LENGTH_LONG).show();
        }
        else {
            setContentView(R.layout.activity_registro_2);
        }
    }

    public void onClick2(View view){
        setContentView(R.layout.activity_registro_3);
    }

}