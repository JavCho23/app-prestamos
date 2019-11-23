package com.example.prestamos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.prestamos.Clases.LibroAdaptador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.prestamos.Clases.Libro;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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
                startActivity( new Intent(MainActivity.this, activity_entregas.class));
            }
        });

        imageLibrosPrestados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, activity_libros_prestados.class));
            }
        });

        imageEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, activity_entregas.class));
            }
        });
    }

}
