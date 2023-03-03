package com.cic.caretapaciente;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import java.util.UUID;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Mediciones extends Activity {

    private static final String TAG = "BlueTest5-MainActivity";
    private int mMaxChars = 50000;//Default
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;
    String s;
    SpannableString ss2;

    private boolean mIsUserInitiatedDisconnect = false;

    // All controls here
    private TextView mTxtReceive, oxigeno, temperatura,fcardiaca, frespiratoria,  id_careta, preArtSistolica, preArtDiastolica;
    private Handler handler;



    private boolean mIsBluetoothConnected = false;

    private BluetoothDevice mDevice;

    private ProgressDialog progressDialog;

    private Button send;

    private int progressBarStatus = 0;
    private DatosRepository datosRepository;
    private Handler progressBarHandler = new Handler();
    ProgressDialog progressBar;
    private long key = 0;

    private String oxigenos;
    private String frespiratorias;
    private String temperaturas;
    private String fcaridacas;
    private String sistolica;
    private String diastolica;
    private  int user_value;
    static int CODIGO_PERMISOS_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signos);
        ActivityHelper.initialize(this);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(devicelist.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(devicelist.DEVICE_UUID));
        mMaxChars = b.getInt(devicelist.BUFFER_SIZE);
        Log.d(TAG, "Ready");
        setContentView(R.layout.signos);
        mTxtReceive = (TextView) findViewById(R.id.txtReceive);
        id_careta = (TextView) findViewById(R.id.activity_id_paciente);
        oxigeno = (TextView) findViewById(R.id.activity_saturacion_oxigeno);
        temperatura = (TextView) findViewById(R.id.activity_temperatura);
        fcardiaca = (TextView) findViewById(R.id.activity_frec_cardiaca);
        frespiratoria = (TextView) findViewById(R.id.activity_frec_respiratoria);
        preArtSistolica = findViewById(R.id.activity_pres_art_sistolica);
        preArtDiastolica = findViewById(R.id.activity_pres_art_diastolica);
        send = findViewById(R.id.activity_comments_send_2);
        datosRepository = DatosRepository.getInstance();



    }


    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;
        private boolean chkReceiveText = true;
        private static final String TAG = "MyActivity";
        private StringBuilder recDataString = new StringBuilder();
        private OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            InputStream inputStream;
            SharedPreferences sharedPref = getSharedPreferences("Datos",Context.MODE_PRIVATE);
            user_value = sharedPref.getInt("ID_PACIENTE", 1);
            Log.d("id_usuario",""+user_value);
            try {
                String sbuffer = "-1";
                byte[] bytes = sbuffer.toString().getBytes(Charset.defaultCharset());
                write(bytes);



                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];



                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;

                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String[] strInput = {new String(buffer, 0, i)};
                        recDataString.append(strInput[0]);
                        int endOfLineIndex = recDataString.indexOf("~");
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);

                        final int limite = 24;
                        if (strInput[0].charAt(0) == '#') {
                            mTxtReceive.post(new Runnable() {
                                @Override
                                public void run() {
                                    int longitud = strInput[0].length();
                                    if (longitud > limite) {
                                        strInput[0] = strInput[0].replaceAll("#"," ");
                                        strInput[0] = strInput[0].replaceAll("~"," ");

                                        String[] value = strInput[0].split(",");

                                        oxigenos = value[0];
                                        frespiratorias = value[1];
                                        temperaturas = value[3];
                                        fcaridacas = value[4];
                                        sistolica = value[2];
                                        diastolica = value[5];




                                                oxigeno.setText(oxigenos);
                                                frespiratoria.setText(frespiratorias);
                                                temperatura.setText(temperaturas);
                                                fcardiaca.setText(fcaridacas);
                                        preArtSistolica.setText(diastolica);
                                        preArtDiastolica.setText(sistolica);

                                        id_careta.setText("" + user_value);
                                                progressBar = new ProgressDialog(v.getContext(), R.style.MyAlertDialogStyle);
                                                progressBar.setCancelable(false);
                                                s = "Midiendo sus signos vitales, por favor no se quite el equipo (" + progressBarStatus + "%)";

                                                //progressBar.setMessage("Midiendo sus signos vitales, por favor no se quite el equipo");
                                                ss2 = new SpannableString(s);
                                                ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
                                                ss2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss2.length(), 0);
                                                progressBar.setMessage(ss2);

                                                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                progressBar.setProgress(0);
                                                progressBar.setMax(100);
                                                progressBar.show();

                                                //reset progress bar and filesize status
                                                progressBarStatus = 0;
                                                key = 0;

                                                new Thread(new Runnable() {
                                                    public void run() {
                                                        while (progressBarStatus < 100) {
                                                            // performing operation
                                                            s = "Midiendo sus signos vitales, por favor no se quite el equipo (" + progressBarStatus + "%)";

                                                            //progressBar.setMessage("Midiendo sus signos vitales, por favor no se quite el equipo");
                                                            progressBarStatus = doOperation();
                                                            ss2 = new SpannableString(s);
                                                            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
                                                            ss2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss2.length(), 0);
                                                            progressBar.setMessage(ss2);
                                                            try {
                                                                Thread.sleep(100);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            // Updating the progress bar
                                                            progressBarHandler.post(new Runnable() {
                                                                public void run() {
                                                                    progressBar.setProgress(progressBarStatus);
                                                                }
                                                            });
                                                        }
                                                        // performing operation if file is downloaded,
                                                        if (progressBarStatus >= 100) {
                                                            // sleeping for 1 second after operation completed
                                                            try {
                                                                Thread.sleep(1000);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            // close the progress bar dialog
                                                            progressBar.dismiss();
                                                        }
                                                    }
                                                }).start();

                                    } //longitud
                                    else {
                                        Log.wtf(TAG,"CERO: "+ strInput[0]);
                                    }

                                } //run

                            }); //receive
                        } //if string
                    } // input > 0


                    //
                } // while



            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (StringIndexOutOfBoundsException e){
                Log.wtf(TAG,"VALUE: "+ "Out of bounds");
                Log.wtf(TAG,"VALUE: "+ mTxtReceive);
                e.printStackTrace();

                ;
            }
                }
            });


        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        1, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(2);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        public void stop() {
            bStop = true;
        }

    }





    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                Toast.makeText(getApplicationContext(), "No se pudo conectar.", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
// TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Mediciones.this, "Espera", "Conectando");
        }

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "No se pudo conectar.", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            } else {
                msg("Conectado");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }

            progressDialog.dismiss();
        }

    }

    public int doOperation() {
        while (key < 5) {
            datosRepository = DatosRepository.getInstance();

            Datos d1 = new Datos(
                    Integer.parseInt(id_careta.getText().toString()),
                    Float.parseFloat(oxigeno.getText().toString()),
                    Float.parseFloat(temperatura.getText().toString()),
                    0,
                    Integer.parseInt(fcardiaca.getText().toString()),
                    Integer.parseInt(frespiratoria.getText().toString()),
                    false,
                    Integer.parseInt(preArtSistolica.getText().toString()),
                    Integer.parseInt(preArtDiastolica.getText().toString())
            );

            datosRepository.getCommentsService().createComment(d1).enqueue(new Callback<Datos>() {


                @SuppressLint("ResourceType")
                @Override
                public void onResponse(Call<Datos> call, Response<Datos> r) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Por favor mantenga la misma postura", Toast.LENGTH_SHORT);
                    TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
                    toast.show();
                }

                @Override
                public void onFailure(Call<Datos> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error de envío de datos ", Toast.LENGTH_SHORT).show();
                }
            });
            key++;
            return (int) ((100/5)*key);

        }
        return 100;
    }

}