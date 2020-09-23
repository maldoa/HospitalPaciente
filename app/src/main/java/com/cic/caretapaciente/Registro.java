package com.cic.caretapaciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }
    @Override
    public void onClick(View view){
        setContentView(R.layout.activity_registro_2);
    }

    public void onClick2(View view){
        setContentView(R.layout.activity_registro_3);
    }

}