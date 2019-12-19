package com.example.prestamos.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prestamos.R;

import java.util.ArrayList;

public class PrestamoAdaptador extends BaseAdapter {
    private Context contexto;
    private ArrayList<Prestamo> lista;

    public PrestamoAdaptador(Context contexto, ArrayList<Prestamo> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Prestamo prestamo = (Prestamo) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.item_prestamo, null);
        TextView nombre = convertView.findViewById(R.id.item_nombre);

        nombre.setText(prestamo.getNombre() + " " +  prestamo.getFecha());

        return convertView;
    }
    public void clear(){
        lista.clear();
    }
}
