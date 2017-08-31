package com.sinch.android.rtc.sample.video;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    private Button mLoginButton;
    private EditText mLoginName;
    private ProgressDialog mSpinner;
    private Button Bsettings;

    FileInputStream fas = null;
    private String FILENAME = "ArduinoIP.txt";
    private String line;
    InputStreamReader isr = null;
    OutputStreamWriter out = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        try {
            fas = getApplicationContext().openFileInput(FILENAME);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fas == null)
        {
            try {
                out = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE));
                out.write("127.0.0.1");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        else {
            isr = new InputStreamReader(fas);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            IpEnum.IP.setCodigo(sb.toString());
        }

        mLoginName = (EditText) findViewById(R.id.loginName);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions(LoginActivity.this , PERMISSIONS)) {
                    loginClicked();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "App não tem permissões! ", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        Bsettings = (Button) findViewById(R.id.bsettings);
        Bsettings.setEnabled(true);
        Bsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasPermissions(LoginActivity.this , PERMISSIONS)) {
                    Intent vsettings = new Intent(LoginActivity.this, Settings.class);
                    startActivity(vsettings);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "App não tem permissões! ", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        final WebView wv = (WebView) findViewById(R.id.fragment_2_webview);


        findViewById(R.id.direita).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.loadUrl("http://" + IpEnum.IP.getCodigo() + "?direita");


            }
        });

        findViewById(R.id.esquerda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wv.loadUrl("http://" + IpEnum.IP.getCodigo() + "/?esquerda");
            }
        });

        findViewById(R.id.cima).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wv.loadUrl("http://" + IpEnum.IP.getCodigo() + "/?cima");
            }
        });

        findViewById(R.id.baixo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wv.loadUrl("http://" + IpEnum.IP.getCodigo() + "/?baixo");
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wv.loadUrl("http://" + IpEnum.IP.getCodigo() + "/?stop");
            }
        });

    }

    protected void onServiceConnected() {
        mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void loginClicked() {
        String userName = mLoginName.getText().toString();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Escreva seu nome", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logando");
        mSpinner.setMessage("Aguarde...");
        mSpinner.show();
    }



    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}