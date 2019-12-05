package com.example.prestamos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView imageLibros = findViewById(R.id.imageLibros);
        ImageView imagePrestamo = findViewById(R.id.imagePrestamo);
        ImageView imageLibrosPrestados = findViewById(R.id.imageLibrosPrestados);
        ImageView imageEntregas = findViewById(R.id.imageEntregas);

        imageLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, libros.class));
            }
        });

        imagePrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, carrito_prestamo.class));
            }
        });

        imageLibrosPrestados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, Prestamos_Internos.class));
            }
        });

        imageEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, Prestamos_Externos.class));
            }
        });
    }

}
