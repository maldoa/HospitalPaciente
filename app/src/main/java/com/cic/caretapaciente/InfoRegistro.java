package com.cic.caretapaciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InfoRegistro extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_registro);
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(InfoRegistro.this, Registro.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(InfoRegistro.this, view, "simple_activity_transition");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("hasBackPressed",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


}