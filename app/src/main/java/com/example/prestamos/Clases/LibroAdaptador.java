package com.example.prestamos.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.prestamos.R;

import java.util.ArrayList;

public class LibroAdaptador extends BaseAdapter {
    private Context contexto;
    private ArrayList<Libro> lista;

    public LibroAdaptador(Context contexto, ArrayList<Libro> lista) {
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
        Libro libro = (Libro) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.item_libro, null);
        TextView titulo = convertView.findViewById(R.id.item_titulo);
        TextView autores = convertView.findViewById(R.id.item_autores);
        TextView disponibles = convertView.findViewById(R.id.item_disponibles);

        titulo.setText(libro.getTitulo());
        autores.setText(libro.getAutores());
        disponibles.setText("Disponibles: "+(libro.getCantidad()-libro.getPrestados()));

        return convertView;
    }
    public void clear(){
        lista.clear();
    }
}
