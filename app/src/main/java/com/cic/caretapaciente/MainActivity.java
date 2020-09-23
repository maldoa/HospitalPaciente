package com.cic.caretapaciente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Boolean firstTime = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        setContentView(R.layout.caratula);

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if(isFirstTime()){
                    setContentView(R.layout.activity_info_app);
                }

                else{
                    setContentView(R.layout.activity_info_registro);

                }



                }

        }.start();

    }
    //primera vez arranca la app
    // si es verdad se pasa a la siguiente pantalla

    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
             //   boolean hasBackPressed = data.getBooleanExtra("hasBackPressed");
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
        }
    }


    // se muestra la pantalla del proposito y para avanzar se necesita dar click
    @Override
    public void onClick(View view){
        setContentView(R.layout.activity_info_registro);
    }

    public void onClick2(View view){
        Intent intent = new Intent(MainActivity.this, Registro.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view, "simple_activity_transition");
        startActivity(intent, options.toBundle());
    }
}