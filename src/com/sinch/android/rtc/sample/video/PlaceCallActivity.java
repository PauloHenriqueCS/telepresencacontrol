package com.sinch.android.rtc.sample.video;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;

public class PlaceCallActivity extends BaseActivity {

    private Button mCallButton;
    private EditText mCallName;
    final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);


        final WebView wv = (WebView) findViewById(R.id.fragment_2_webview);


        findViewById(R.id.direita).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.loadUrl("http://"+ IpEnum.IP.getCodigo()+"/?direita");
            }
        });

        findViewById(R.id.esquerda).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                wv.loadUrl("http://"+ IpEnum.IP.getCodigo()+"/?esquerda");
            }
        });

        findViewById(R.id.cima).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.loadUrl("http://"+ IpEnum.IP.getCodigo()+"/?cima");
            }
        });

        findViewById(R.id.baixo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.loadUrl("http://"+ IpEnum.IP.getCodigo()+"/?baixo");
            }
        });

        findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.loadUrl("http://"+ IpEnum.IP.getCodigo()+"/?stop");
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {
        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Coloque o usuario para ligar", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (hasPermissions(PlaceCallActivity.this , PERMISSIONS)) {
                switch (v.getId()) {
                    case R.id.callButton:
                        callButtonClicked();
                        break;

                    case R.id.stopButton:
                        stopButtonClicked();
                        break;

                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "App não tem permissões! ", Toast.LENGTH_LONG).show();
                finish();
            }



        }
    };

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
