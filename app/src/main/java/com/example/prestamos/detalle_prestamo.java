package com.example.prestamos;

import android.os.Bundle;

import com.example.prestamos.Clases.ListaPrestamo;
import com.example.prestamos.Clases.Prestamo;
import com.example.prestamos.Clases.PrestamoAdaptador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class detalle_prestamo extends AppCompatActivity {
    private PrestamoAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_prestamo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            TextView dni = findViewById(R.id.detalle_dni);
            TextView nombre = findViewById(R.id.detalle_nombre);
            TextView procedencia = findViewById(R.id.detalle_procedencia);
            TextView telefono = findViewById(R.id.procedencia_telefono);
            TextView correo = findViewById(R.id.detalle_correo);
            TextView fecha = findViewById(R.id.fechaPrestamo);

        final Prestamo prestamo = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0));
            dni.setText(prestamo.getDni());
            nombre.setText(prestamo.getNombre());
            procedencia.setText(prestamo.getProcedencia());
            telefono.setText(prestamo.getTelefono());
            correo.setText(prestamo.getCorreo());



    }

}
