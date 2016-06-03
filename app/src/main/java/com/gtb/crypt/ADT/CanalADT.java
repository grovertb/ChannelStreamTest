package com.gtb.crypt.ADT;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gtb.crypt.BE.CanalBE;
import com.gtb.crypt.R;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by GroverTB on 5/05/2016.
 */
public class CanalADT extends ArrayAdapter<CanalBE> {
    public CanalADT(Context context, int resource, ArrayList<CanalBE> objects) {
        super(context,resource, objects);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final CanalBE oCanalBE  = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.model_canal, parent, false);
        }

        TextView oLblNombre = (TextView) convertView.findViewById(R.id.ModTxtNombre);
        Switch oSwitchEstado = (Switch) convertView.findViewById(R.id.ModSwitchEstado);
        try{
            oLblNombre.setText(oCanalBE.getName());
            oSwitchEstado.setChecked(oCanalBE.getEstado());

            if(!oCanalBE.getColorCard().equals("")){
                oLblNombre.setTextColor(Color.parseColor(oCanalBE.getColorCard()));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
