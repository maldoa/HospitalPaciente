package com.cic.caretapaciente;

import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class Mediciones extends Activity {

    private static final String TAG = "BlueTest5-MainActivity";
    private int mMaxChars = 50000;//Default
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;


    private boolean mIsUserInitiatedDisconnect = false;

    // All controls here
    private TextView mTxtReceive, oxigeno, CO2,temperatura,fcardiaca, frespiratoria, parterial;
    private Button mBtnClearInput;
    private ScrollView scrollView;
    private CheckBox chkScroll;
    private CheckBox chkReceiveText;


    private boolean mIsBluetoothConnected = false;

    private BluetoothDevice mDevice;

    private ProgressDialog progressDialog;

    private TextInputEditText id_careta;


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
       // chkScroll = (CheckBox) findViewById(R.id.chkScroll);
       // chkReceiveText = (CheckBox) findViewById(R.id.chkReceiveText);
       // scrollView = (ScrollView) findViewById(R.id.viewScroll);
        //mBtnClearInput = (Button) findViewById(R.id.btnClearInput);
        //mTxtReceive.setMovementMethod(new BaseMovementMethod());
        id_careta = (TextInputEditText) findViewById(R.id.idPaciente);
        oxigeno = (TextView) findViewById(R.id.oxigeno);
        temperatura = (TextView) findViewById(R.id.temperatura);
        fcardiaca = (TextView) findViewById(R.id.fcardiaca);
        CO2 = (TextView) findViewById(R.id.CO2);
        parterial = (TextView) findViewById(R.id.parterial);
        frespiratoria = (TextView) findViewById(R.id.frespiratoria);




    }

    //write json
    public void writeJSON() {
        JSONObject object = new JSONObject();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        try {
            object.put("id_careta", new Integer(id_careta.getText().toString()));
            object.put("fecha_medicion", c);
            object.put("saturacion_oxigeno", new Double(oxigeno.getText().toString()));
            object.put("temperatura", new Double(temperatura.getText().toString()));
            object.put("frec_cardiaca", new Integer(fcardiaca.getText().toString()));
            object.put("ferc_respiratoria", new Integer(frespiratoria.getText().toString()));
            //TODO  PRESION ARTERIAL


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
    }


    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;
        private boolean chkReceiveText = true;
        private static final String TAG = "MyActivity";
        private StringBuilder recDataString = new StringBuilder();

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;

            try {
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
                        /*
                         * If checked then receive text, better design would probably be to stop thread if unchecked and free resources, but this is a quick fix
                         */
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
                                        String oxigenos = value[0];
                                        String frespiratorias = value[1];
                                        String temperaturas = value[3];
                                        String fcaridacas = value[4];
                                        String parterials = value[5];
                                        String CO2s = value[2];
                                        //oxigeno.append(strInput);
                                        //SystemClock.sleep(500);
                                        oxigeno.append(oxigenos);
                                        frespiratoria.append(frespiratorias);
                                        temperatura.append(temperaturas);
                                        fcardiaca.append(fcaridacas);
                                        parterial.append(parterials);
                                        CO2.append(CO2s);
                                        Log.wtf(TAG,"VALUE: " + oxigenos+" length "+strInput[0].length());
                                        Log.wtf(TAG,"CERO: "+ strInput[0]);

                                    }
                                    else {
                                        Log.wtf(TAG,"VALUE: "+ "AAAA");
                                        Log.wtf(TAG,"TODO: "+ strInput);
                                        Log.wtf(TAG,"CERO: "+ strInput[0]);


                                    }

/*
                                    int txtLength = mTxtReceive.getEditableText().length();
                                    if(txtLength > mMaxChars){
                                        mTxtReceive.getEditableText().delete(0, txtLength - mMaxChars);
                                    }
*/



                                }

                            });

                            /*
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mTxtReceive.setText(""); // this code will be executed after 2 seconds
                                }
                            }, 1000);
                            */
                        }


                    }


                    Thread.sleep(500);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            oxigeno.setText("");
                            frespiratoria.setText("");
                            temperatura.setText("");
                            fcardiaca.setText("");
                            parterial.setText("");
                            CO2.setText("");
                            // mTxtReceive.setText("");

                        }
                    });

                    //
                }
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (StringIndexOutOfBoundsException e){
                Log.wtf(TAG,"VALUE: "+ "Out of bounds");
                Log.wtf(TAG,"VALUE: "+ mTxtReceive);
                e.printStackTrace();

                ;
            }


        }

        public void stop() {
            bStop = true;
        }

    }

/*
    void runOnUiThread(new Runnable()) {
        @Override
        public void run() {
            //Cambiar controles
        }
    });*/



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
            progressDialog = ProgressDialog.show(Mediciones.this, "Espera", "Conectando");// http://stackoverflow.com/a/11130220/1287554
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
// Unable to connect to device
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
                finish();
            } else {
                msg("Conectado");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }

            progressDialog.dismiss();
        }

    }
}