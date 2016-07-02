package com.gtb.crypt.DAL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gtb.crypt.BE.CanalBE;
import com.gtb.crypt.UTIL.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by GroverTB on 5/05/2016.
 */
public class CanalDAL {
    public ArrayList<CanalBE> lstCanales = null;
    public  JSONObject resultJSON = new JSONObject();

    public void getAll(){
        Cursor ocursor=null;
        CanalBE oCanalBE=null;

        try{
            //Construyendo una consulta
            ocursor = DataBaseHelper.myDataBase.query(
                    "canal", //a. table
                    null, //b. column names
                    null, // c. selections usu_codigo=?
                    null, // d. selections args  { String.valueOf(id) }
                    null, // e. group by
                    null, // f. having
                    "Posicion",// g. order by
                    null); // h. limit
            lstCanales=new ArrayList<CanalBE>();
            lstCanales.clear();
            if(ocursor.moveToFirst()){
                do{
                    boolean value = ocursor.getInt(ocursor.getColumnIndex("Estado")) > 0;

                    oCanalBE=new CanalBE();
                    oCanalBE.setCodigo(ocursor.isNull(ocursor.getColumnIndex("Codigo")) ? 0 : ocursor.getInt(ocursor.getColumnIndex("Codigo")));
                    oCanalBE.setName(ocursor.isNull(ocursor.getColumnIndex("name")) ? "" : ocursor.getString(ocursor.getColumnIndex("name")));
                    oCanalBE.setStream(ocursor.isNull(ocursor.getColumnIndex("stream")) ? "" : ocursor.getString(ocursor.getColumnIndex("stream")));
                    oCanalBE.setColorCard(ocursor.isNull(ocursor.getColumnIndex("colorCard")) ? "" : ocursor.getString(ocursor.getColumnIndex("colorCard")));
                    oCanalBE.setEstado(!ocursor.isNull(ocursor.getColumnIndex("Estado")) && value);
                    oCanalBE.setTipo(ocursor.isNull(ocursor.getColumnIndex("Tipo")) ? 0 : ocursor.getInt(ocursor.getColumnIndex("Tipo")));
                    oCanalBE.setPosicion(ocursor.isNull(ocursor.getColumnIndex("Posicion")) ? 0 : ocursor.getInt(ocursor.getColumnIndex("Posicion")));

                    lstCanales.add(new CanalBE(oCanalBE.getCodigo(),
                            oCanalBE.getName(),
                            oCanalBE.getStream(),
                            oCanalBE.getColorCard(),
                            oCanalBE.getEstado(),
                            oCanalBE.getTipo(),
                            oCanalBE.getPosicion()
                    ));
                }while(ocursor.moveToNext());
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(ocursor !=null)
                ocursor.close();
        }
    }

    public void insert(CanalBE oCanalBE){
        try{
            ContentValues cv = new ContentValues();
            //cv.put("Codigo", oPublicacionBE.getCodigo());
            cv.put("name", oCanalBE.getName());
            cv.put("stream", oCanalBE.getStream());
            cv.put("colorCard", oCanalBE.getColorCard());
            cv.put("Estado", oCanalBE.getEstado());
            cv.put("Tipo", oCanalBE.getTipo());
            cv.put("Posicion", oCanalBE.getPosicion());
            DataBaseHelper.myDataBase.insert("canal", "Codigo", cv);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void delete(CanalBE oCanalBE){
        try{
            DataBaseHelper.myDataBase.delete("canal", "Codigo = ?", new String[]{String.valueOf(oCanalBE.getCodigo())});
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void update(CanalBE oCanalBE){
        try{
            ContentValues cv = new ContentValues();
            cv.put("name", oCanalBE.getName());
            cv.put("stream", oCanalBE.getStream());
            cv.put("colorCard", oCanalBE.getColorCard());
            cv.put("Estado", oCanalBE.getEstado());
            cv.put("Tipo", oCanalBE.getTipo());
            cv.put("Posicion", oCanalBE.getPosicion());
            DataBaseHelper.myDataBase.update("canal", cv, "Codigo = ?", new String[]{String.valueOf(oCanalBE.getCodigo())});
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void getAllObject(){
        Cursor ocursor=null;
        JSONArray resultSet = new JSONArray();
        try{
            //Construyendo una consulta
            ocursor = DataBaseHelper.myDataBase.query(
                    "canal", //a. table
                    null, //b. column names
                    "Estado=1", // c. selections usu_codigo=?
                    null, // d. selections args  { String.valueOf(id) }
                    null, // e. group by
                    null, // f. having
                    "Posicion",// g. order by
                    null); // h. limit
            if(ocursor.moveToFirst()){
                do {
                    int totalColumn = ocursor.getColumnCount();
                    JSONObject rowObject = new JSONObject();
                    for (int i = 0; i < totalColumn; i++) {
                        if (ocursor.getColumnName(i) != null) {
                            try{
                                if( ocursor.getString(i) != null ){
                                    rowObject.put(ocursor.getColumnName(i) ,  ocursor.getString(i) );
                                }else{
                                    rowObject.put( ocursor.getColumnName(i) ,  "" );
                                }
                            }catch( Exception e ){
                                Log.d("TAG_NAME", e.getMessage()  );
                            }
                        }
                    }
                    resultSet.put(rowObject);
                }while(ocursor.moveToNext());
                resultJSON.put("tv",resultSet);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(ocursor !=null)
                ocursor.close();
        }
    }

}
