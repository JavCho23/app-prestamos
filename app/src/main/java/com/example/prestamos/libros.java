package com.example.prestamos;

import android.content.Intent;
import android.os.Bundle;

import com.example.prestamos.Clases.Libro;
import com.example.prestamos.Clases.LibroAdaptador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class libros extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<Libro> libros = new ArrayList<>();
    private LibroAdaptador adaptador;
    private Spinner categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            ListView lista;
            lista = findViewById(R.id.lista_general);
            categorias = findViewById(R.id.spinner);
            adaptador = new LibroAdaptador(this,libros);
            lista.setAdapter(adaptador);

            db = FirebaseFirestore.getInstance();

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(libros.this, detalle_libro.class).putExtra("posicion",position));
                }
            });

            categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cargarDatos(categorias.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public void cargarDatos(String categoria) {
            libros.clear();
            adaptador.notifyDataSetChanged();

            db.collection("libros")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                for (DocumentSnapshot doc : list) {
                                    Libro libro = doc.toObject(Libro.class);
                                    libros.add(libro);
                                }

                                adaptador.notifyDataSetChanged();
                            }
                        }
                    });
        }


}
