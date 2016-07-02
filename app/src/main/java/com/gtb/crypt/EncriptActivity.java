package com.gtb.crypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class EncriptActivity extends AppCompatActivity {
    EditText txtContent,txtresult,txtPassCrypt;
    Button btncript,btndecript;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encript);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtContent = (EditText) findViewById(R.id.txtcontent);
        txtPassCrypt = (EditText) findViewById(R.id.txtPassCrypt);
        txtresult = (EditText) findViewById(R.id.txtresult);
        btncript   = (Button) findViewById(R.id.btncript);
        btndecript = (Button) findViewById(R.id.btndecript);

        btndecript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String decryptmsj = AESCrypt.decrypt (txtPassCrypt.getText().toString(), txtContent.getText().toString());
                    txtresult.setText(decryptmsj);
                }catch (GeneralSecurityException e){
                    e.printStackTrace();
                }
            }
        });
        btncript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String encryptmsj = AESCrypt.encrypt(txtPassCrypt.getText().toString(), txtContent.getText().toString());
                    txtresult.setText(encryptmsj);
                }catch (GeneralSecurityException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
