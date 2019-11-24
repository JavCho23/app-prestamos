package com.example.prestamos;

import android.os.Bundle;

import com.example.prestamos.Clases.Carrito;
import com.example.prestamos.Clases.EjemplarAdaptador;
import com.example.prestamos.Clases.Libro;
import com.example.prestamos.Clases.ListaLibros;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class detalle_libro extends AppCompatActivity {
    private EjemplarAdaptador adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView ejemplares = findViewById(R.id.lista_ejemplares);
        TextView autores = findViewById(R.id.datos_libro_autores);
        TextView titulo = findViewById(R.id.datos_libro_titulo);

        final Libro libro =  ListaLibros.getLibro(getIntent().getIntExtra("pos",0));
        autores.setText(libro.getAutores());
        titulo.setText(libro.getTitulo());

         adaptador = new EjemplarAdaptador(this,libro.getEjemplares());
         ejemplares.setAdapter(adaptador);

        ejemplares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Carrito.agregar(libro.getEjemplares().get(position));
                libro.getEjemplares().remove(position);
                adaptador.notifyDataSetChanged();
                Toast.makeText(detalle_libro.this, "Agregado al carrito", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
