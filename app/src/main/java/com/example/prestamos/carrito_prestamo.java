package com.example.prestamos;

import android.content.Intent;
import android.os.Bundle;

import com.example.prestamos.Clases.Carrito;
import com.example.prestamos.Clases.EjemplarAdaptador;
import com.example.prestamos.Clases.Libro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class carrito_prestamo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_prestamo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Button hacerPrestamo = findViewById(R.id.hacer_pedido);
        ListView prestamo = findViewById(R.id.lista_prestamo);
        final EjemplarAdaptador adaptador = new EjemplarAdaptador(this, Carrito.getLista());
        prestamo.setAdapter(adaptador);

        if (!Carrito.getLista().isEmpty()) hacerPrestamo.setEnabled(true); ;


        prestamo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Carrito.eliminar(Carrito.getEjemplar(position));
                Toast.makeText(carrito_prestamo.this, "Quitado del carrito", Toast.LENGTH_SHORT).show();
                adaptador.notifyDataSetChanged();
                if (Carrito.getLista().isEmpty()) hacerPrestamo.setEnabled(false); ;

            }
        });
        hacerPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(carrito_prestamo.this, activity_prestamos.class));
                onBackPressed();
            }
        });
    }

}
