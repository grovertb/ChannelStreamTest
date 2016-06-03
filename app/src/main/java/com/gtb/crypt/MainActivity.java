package com.gtb.crypt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gtb.crypt.ADT.CanalADT;
import com.gtb.crypt.BE.CanalBE;
import com.gtb.crypt.DAL.CanalDAL;
import com.gtb.crypt.UTIL.DataBaseHelper;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    LinearLayout layoutcanales;
    EditText txtResult,txtText;
    Button btnCrypt;
    final String password = "S0p0rt3";

    private CanalADT oCanalAdapter = null;
    private CanalBE oCanalBE;
    private CanalDAL oCanalDAL = new CanalDAL();
    private ListView oListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CanalActivity.class);
                startActivity(intent);
            }
        });

        oListView =(ListView) findViewById(R.id.lstPublicacion);
        oListView.setOnItemLongClickListener(oListViewOnItemLongClickListener);

    }

    AbsListView.OnItemLongClickListener  oListViewOnItemLongClickListener= new AbsListView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
            final Vector<AlertDialog> dialogs = new Vector<AlertDialog>();
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View viewButtons = getLayoutInflater().inflate(R.layout.model_actions, null);
            AlertDialog dialog = builder.create();
            dialog.setTitle("Opciones:");
            dialog.setView(viewButtons);
            dialog.setButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.add(dialog);

            Button btnEditarP = (Button) viewButtons.findViewById(R.id.btnEditarP);
            Button btnEliminarP = (Button) viewButtons.findViewById(R.id.btnEliminarP);
            btnActionsClick(btnEditarP, position, dialogs);
            btnActionsClick(btnEliminarP, position, dialogs);
            dialog.show();
            return true;
        }
    };

    private void btnActionsClick(final Button btn,final Integer index,final Vector<AlertDialog> dialogs){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog dialog = builder.create();
                switch (v.getId()){
                    case R.id.btnEditarP:
                        Intent msgIntent = new Intent(MainActivity.this, CanalActivity.class);
                        msgIntent.putExtra("getCodigo", oCanalAdapter.getItem(index).getCodigo());
                        msgIntent.putExtra("getNombre", oCanalAdapter.getItem(index).getName());
                        msgIntent.putExtra("getUrl", oCanalAdapter.getItem(index).getStream());
                        msgIntent.putExtra("getColor", oCanalAdapter.getItem(index).getColorCard());
                        msgIntent.putExtra("getEstado", oCanalAdapter.getItem(index).getEstado());
                        msgIntent.putExtra("getPosicion", oCanalAdapter.getItem(index).getPosicion());
                        startActivity(msgIntent);
                        for (AlertDialog dialogse : dialogs)
                            if (dialogse.isShowing()) dialogse.dismiss();
                        break;
                    case R.id.btnEliminarP:
                        dialog.setTitle("Eliminar Producto");
                        dialog.setMessage("Esta Seguro que desea eliminar este registro?");
                        dialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                oCanalBE = new CanalBE();
                                oCanalBE.setCodigo(oCanalAdapter.getItem(index).getCodigo());
                                oCanalDAL.delete(oCanalBE);
                                LoadCanalesSQLite();
                                for (AlertDialog dialogse : dialogs)
                                    if (dialogse.isShowing()) dialogse.dismiss();
                            }
                        });
                        dialog.setButton2("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogs.add(dialog);
                        dialog.show();
                        break;
                }
            }
        });
    }


    public void LoadCanalesSQLite(){
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            oCanalDAL.getAll();
            oCanalAdapter = new CanalADT(MainActivity.this,0, oCanalDAL.lstCanales);
            oCanalAdapter.notifyDataSetChanged();
            oListView.setAdapter(oCanalAdapter);
            setListViewHeightBasedOnItems(oListView);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadCanalesSQLite();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, EncriptActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_verificar || id == R.id.action_generar){
            oCanalDAL.getAllObject();
            if(id == R.id.action_verificar){
                /*try {
                    for (int i=0;i<oCanalDAL.resultJSON.getJSONArray("tv").length();i++){
                        JSONObject jsonObjectText = oCanalDAL.resultJSON.getJSONArray("tv").getJSONObject(i);
                        new AsyncTaskGrover().execute(jsonObjectText);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }*/
            }else{
                try {
                    System.out.println(oCanalDAL.resultJSON);
                    String messageCrypt = AESCrypt.encrypt( password, oCanalDAL.resultJSON.toString());
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("CANALES", messageCrypt);
                    clipboard.setPrimaryClip(clip);
                }catch (GeneralSecurityException e){
                    e.printStackTrace();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public class AsyncTaskGrover extends AsyncTask<JSONObject, String, String> {
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Verificando Canales", "Espere...", true);
        @Override
        protected String doInBackground(JSONObject... params) {
            try {
                URL url = new URL(params[0].getString("Url"));
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.connect();
                String respuesta = "";
                return  String.valueOf(urlConn.getResponseCode());
            }catch (JSONException e){
                e.printStackTrace();
            }catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(dialog.isShowing()) dialog.dismiss();
            System.out.println("GROVER");
            System.out.println(s);
        }
    }*/

}
