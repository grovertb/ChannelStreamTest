package com.gtb.crypt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.gtb.crypt.BE.CanalBE;
import com.gtb.crypt.DAL.CanalDAL;

import tcking.github.com.giraffeplayer.GiraffePlayer;

public class CanalActivity extends AppCompatActivity {
    EditText txtnombre,txturl,txtcolor,txtposicion,txttipo;
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

        txtnombre       = (EditText) findViewById(R.id.txtnombre);
        txturl          = (EditText) findViewById(R.id.txturl);
        txtcolor        = (EditText) findViewById(R.id.txtcolor);
        txtposicion     = (EditText) findViewById(R.id.txtposicion);
        txttipo         = (EditText) findViewById(R.id.txttipo);
        btntest         = (Button) findViewById(R.id.btntest);
        switchestado    = (Switch) findViewById(R.id.switchestado);

        gtCodigo = getIntent().getIntExtra("getCodigo", -1);
        if(gtCodigo > 0){
            txtnombre.setText(getIntent().getStringExtra("getNombre"));
            txturl.setText(getIntent().getStringExtra("getUrl"));
            txtcolor.setText(getIntent().getStringExtra("getColor"));

            txtposicion.setText(""+getIntent().getIntExtra("getPosicion",0));
            txttipo.setText(""+getIntent().getIntExtra("getTipo",0));
            switchestado.setChecked(getIntent().getBooleanExtra("getEstado",false));
        }

        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer type = Integer.parseInt(txttipo.getText().toString());
                if( type == 2 || type == 4){
                    final WebView webservice = new WebView(CanalActivity.this);
                    webservice.getSettings().setJavaScriptEnabled(true);
                    webservice.addJavascriptInterface(new MyJavaScriptInterface(CanalActivity.this), "HTMLOUT");
                    webservice.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            webservice.loadUrl("javascript:window.HTMLOUT.getURL(document.querySelector('iframe').contentDocument.querySelector('#videoplayer>source').attributes.src.value);");
                        }
                    });
                    webservice.setWebChromeClient(new WebChromeClient(){
                        public void onProgressChanged(WebView view, int newProgress) {
                            Toast.makeText(CanalActivity.this, "Cargando: "+newProgress, Toast.LENGTH_SHORT).show();
                        }
                    });
                    webservice.loadUrl(txturl.getText().toString());
                }else{
                    player.onInfo(new GiraffePlayer.OnInfoListener(){
                                      @Override
                                      public void onInfo(int what, int extra) {
                                          if(what == 10002 ){
                                              switchestado.setChecked(true);
                                          }
                                      }
                                  }
                    ).onError(new GiraffePlayer.OnErrorListener() {
                        @Override
                        public void onError(int what, int extra) {
                            switchestado.setChecked(false);
                        }
                    })
                    .play(txturl.getText().toString());
                }

            }
        });
    }

    public class MyJavaScriptInterface{
        Context mContext;
        MyJavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void getURL(final  String url){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("GROVER",url);
                    player.onInfo(new GiraffePlayer.OnInfoListener(){
                                      @Override
                                      public void onInfo(int what, int extra) {
                                          if(what == 10002 ){
                                              switchestado.setChecked(true);
                                          }
                                      }
                                  }
                    ).onError(new GiraffePlayer.OnErrorListener() {
                        @Override
                        public void onError(int what, int extra) {
                            switchestado.setChecked(false);
                        }
                    }).play(url);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            oCanalBE = new CanalBE();
            oCanalBE.setName(txtnombre.getText().toString());
            oCanalBE.setStream(txturl.getText().toString());
            oCanalBE.setColorCard(txtcolor.getText().toString());
            oCanalBE.setPosicion(Integer.parseInt(txtposicion.getText().toString()));
            oCanalBE.setTipo(Integer.parseInt(txttipo.getText().toString()));
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
