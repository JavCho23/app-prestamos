package com.example.prestamos.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prestamos.R;

import java.util.ArrayList;

public class EjemplarAdaptador extends BaseAdapter {
    private Context contexto;
    private ArrayList<Ejemplar> lista;

    public EjemplarAdaptador(Context contexto, ArrayList<Ejemplar> lista) {
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
        Ejemplar libro = (Ejemplar) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.item_libro, null);
        TextView titulo = convertView.findViewById(R.id.item_titulo);
        TextView disponibles = convertView.findViewById(R.id.item_disponibles);
        TextView autores = convertView.findViewById(R.id.item_autores);

        titulo.setText(libro.getTitulo());
        autores.setText(libro.getRfid());
        disponibles.setText(libro.isEstado() ? "Disponible": "Prestado");

        return convertView;
    }
    public void clear(){
        lista.clear();
    }
}

