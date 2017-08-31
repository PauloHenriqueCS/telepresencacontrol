package com.sinch.android.rtc.sample.video;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by pcandido on 29/09/2016.
 */
public class Settings extends BaseActivity {
    private Button Bsalvar;
    private EditText texto;
    private TextView IpSalvo;
    private String ipSalvo;
    private String text;
    private String FILENAME = "ArduinoIP.txt";
    private String line;
    FileInputStream fas = null;
    OutputStreamWriter out = null;
    InputStreamReader isr = null;
    StringBuilder sb = null;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        try {
            fas = getApplicationContext().openFileInput(FILENAME);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        isr = new InputStreamReader(fas);
        BufferedReader bufferedReader = new BufferedReader(isr);
        sb = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                ipSalvo = sb.toString();
                isr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        IpSalvo = (TextView) findViewById(R.id.ipSalvo);
        IpSalvo.setText("IP Salvo: "+ ipSalvo);
            Bsalvar = (Button) findViewById(R.id.bsalvar);
            Bsalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    texto = (EditText) findViewById(R.id.iptext);
                    text = texto.getText().toString();
                    if (text.equals("")) {
                        Toast.makeText(getApplicationContext(), "IP: " + ipSalvo + " não alterado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            out = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE));
                            out.write(text);
                            out.close();
                            Toast.makeText(getApplicationContext(), "IP Salvo: " + text, Toast.LENGTH_SHORT).show();
                            IpEnum.IP.setCodigo(text);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("Exception", "File write failed: " + e.toString());
                            Toast.makeText(getApplicationContext(), "Erro ao salvar IP", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    }
                }
            });
        }

    }