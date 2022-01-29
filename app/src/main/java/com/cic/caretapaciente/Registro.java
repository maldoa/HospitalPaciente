package com.cic.caretapaciente;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cic.caretapaciente.Login;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
public class Registro extends AppCompatActivity {

    private Button botonSiguiente1;
    private TextInputEditText idPaciente;
    private SharedPreferences myPreferences;
    static int inactivo = Color.parseColor("#e6e6e6");
    static String BASE_URL = "";
    private Retrofit mRestAdapter;
    private Login login;
    private ImageView mLogoView;
    private EditText mUserIdView, mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextInputLayout mFloatLabelUserId;
    private TextInputLayout mFloatLabelPassword;
    public static final String ID = "ID_PACIENTE";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRestAdapter = new Retrofit.Builder()

                .baseUrl("http://67.205.112.170/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        login = mRestAdapter.create(Login.class);

        mFloatLabelUserId = (TextInputLayout) findViewById(R.id.float_label_user_id);
        mFloatLabelPassword = (TextInputLayout) findViewById(R.id.float_label_password);
        mLogoView = (ImageView) findViewById(R.id.image_logo);
        mUserIdView = (EditText) findViewById(R.id.user_id);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        sharedpreferences =    getSharedPreferences("Datos",Context.MODE_PRIVATE);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ( id == EditorInfo.IME_NULL) {
                    if (!isOnline()) {
                        showLoginError(getString(R.string.error_network));
                        return false;
                    }
                    attemptLogin(textView);
                    return true;
                }
                return false;
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.e("Weeoe","click on");
                if (!isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                Log.e("Weeoe","click on2");
                attemptLogin(view);

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void attemptLogin(View view) {

        // Reset errors.
        mFloatLabelUserId.setError(null);

        mFloatLabelPassword.setError(null);

        // Store values at the time of the login attempt.
        String userId = mUserIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;



        // Set the color
        LinearLayout layout = (LinearLayout) findViewById(R.id.registro_activity);

        //    layout.setBackground(ContextCompat.getDrawable(context, R.drawable.ready));

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelPassword;
            cancel = true;
            layout.setBackgroundResource(R.color.colorAccent);
        } else if (!isPasswordValid(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_invalid_password));
            focusView = mFloatLabelPassword;
            cancel = true;
            layout.setBackgroundResource(R.color.colorAccent);
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelUserId;
            cancel = true;
            layout.setBackgroundResource(R.color.colorAccent);
        } else if (!isUserIdValid(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_invalid_user_id));
            focusView = mFloatLabelUserId;
            cancel = true;
            layout.setBackgroundResource(R.color.colorAccent);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            layout.setBackgroundResource(R.color.colorAccent);
        } else {
            // Mostrar el indicador de carga y luego iniciar la petición asíncrona.
            Log.e("Weeoe","click on3");
            showProgress(true);
            layout.setBackgroundResource(R.color.colorPrimary);
            Log.e("Weeoe",""+userId+" "+getSHA256(password));
            Call<Usuario> loginCall = login.login(new LoginBody(userId, getSHA256(password) ));
            loginCall.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    // Mostrar progreso
                    showProgress(false);

                    // Procesar errores
                    if (!response.isSuccessful()) {
                        String error = "Ha ocurrido un error. Contacte al administrador";
                        if (response.errorBody()
                                .contentType()
                                .subtype()
                                .equals("json")) {
                            //ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                            //error = apiError.getMessage();
                            layout.setBackgroundResource(R.color.colorAccent);
                            Log.d("LoginActivity", error);
                        } else {
                            try {
                                // Reportar causas de error no relacionado con la API
                                Log.d("LoginActivity", response.errorBody().string());
                                layout.setBackgroundResource(R.color.colorAccent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        showLoginError(error);
                        return;
                    }

                    // Guardar afiliado en preferencias
                    Log.d("Activity", ""+response.body().getMensaje()+" "+response.body().getRespuesta()+" "+response.body().getId()+" "+response.body().getError());
                    Log.d("Activity",""+response.headers());
                    // Ir a la citas médicas
                    if (response.body().getRespuesta() == 0)
                    {
                        int id_u = response.body().getId();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("ID_PACIENTE",id_u);
                        editor.commit();
                        Log.d("id_usuario",""+id_u);
                        showAppointmentsScreen(view);
                    }
                    else{
                        layout.setBackgroundResource(R.color.colorAccent);
                        mFloatLabelUserId.setError("Agregue un usuario valido");
                        mFloatLabelPassword.setError("Agregue una contraseña valida");
                        Toast.makeText(getApplicationContext(), response.body().getMensaje()+"" , Toast.LENGTH_SHORT).show();
                    }
                }



                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    showProgress(false);
                    showLoginError(t.getMessage());
                }
            });
        }
    }

    private boolean isUserIdValid(String userId) {
        return userId.length() >= 1;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    private void showAppointmentsScreen(View view) {
       // startActivity(new Intent(this, AppointmentsActivity.class));

        Toast.makeText(getApplicationContext(), "Se inicio sesión " , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Registro.this, MainActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Registro.this, view, "simple_activity_transition");
        startActivity(intent, options.toBundle());
        //finish();
    }

    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getSHA256(String contrasenia) {
        try {
            String password = contrasenia;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = password.getBytes();
            byte[] hash = md.digest(passwordBytes);
            String passwordHash = Base64.getEncoder().encodeToString(hash);
            return passwordHash;
        } catch (NoSuchAlgorithmException ex) {
            Log.d("LoginActivity", ex.toString());
        }
        return null;
    }
}