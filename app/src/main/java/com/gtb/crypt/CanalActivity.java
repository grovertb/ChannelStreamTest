package com.gtb.crypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.gtb.crypt.BE.CanalBE;
import com.gtb.crypt.DAL.CanalDAL;

import tcking.github.com.giraffeplayer.GiraffePlayer;

public class CanalActivity extends AppCompatActivity {
    EditText txtnombre,txturl,txtcolor,txtposicion;
    Button btntest;
    Switch switchestado;
    private CanalBE oCanalBE;
    private CanalDAL oCanalDAL = new CanalDAL();
    Integer gtCodigo;
    GiraffePlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        player = new GiraffePlayer(CanalActivity.this);

        txtnombre   = (EditText) findViewById(R.id.txtnombre);
        txturl      = (EditText) findViewById(R.id.txturl);
        txtcolor    = (EditText) findViewById(R.id.txtcolor);
        txtposicion   = (EditText) findViewById(R.id.txtposicion);
        btntest     = (Button) findViewById(R.id.btntest);
        switchestado = (Switch) findViewById(R.id.switchestado);

        gtCodigo = getIntent().getIntExtra("getCodigo", -1);
        if(gtCodigo > 0){
            txtnombre.setText(getIntent().getStringExtra("getNombre"));
            txturl.setText(getIntent().getStringExtra("getUrl"));
            txtcolor.setText(getIntent().getStringExtra("getColor"));

            txtposicion.setText(""+getIntent().getIntExtra("getPosicion",0));
            switchestado.setChecked(getIntent().getBooleanExtra("getEstado",false));
        }

        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player
                .onInfo(new GiraffePlayer.OnInfoListener(){
                        @Override
                        public void onInfo(int what, int extra) {
                            if(what == 10002 ){
                                switchestado.setChecked(true);
                            }
                        }
                    }
                )
                .onError(new GiraffePlayer.OnErrorListener() {
                    @Override
                    public void onError(int what, int extra) {
                        switchestado.setChecked(false);
                    }
                })
                .play(txturl.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            oCanalBE = new CanalBE();
            oCanalBE.setName(txtnombre.getText().toString());
            oCanalBE.setStream(txturl.getText().toString());
            oCanalBE.setColorCard(txtcolor.getText().toString());
            oCanalBE.setPosicion(Integer.parseInt(txtposicion.getText().toString()));
            oCanalBE.setEstado(switchestado.isChecked());
            if(gtCodigo > 0){
                oCanalBE.setCodigo(gtCodigo);
                oCanalDAL.update(oCanalBE);
            }else{
                oCanalDAL.insert(oCanalBE);
            }
            finish();
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
    }
}
