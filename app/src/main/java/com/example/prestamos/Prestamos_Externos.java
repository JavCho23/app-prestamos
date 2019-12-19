package com.example.prestamos;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.prestamos.Clases.Ejemplar;
import com.example.prestamos.Clases.ListaPrestamo;
import com.example.prestamos.Clases.Prestamo;
import com.example.prestamos.Clases.PrestamoAdaptador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Prestamos_Externos extends AppCompatActivity {
    private FirebaseFirestore db;
    private PrestamoAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lista;
        lista = findViewById(R.id.lista_entregas);
        adaptador = new PrestamoAdaptador(this, ListaPrestamo.getLista());
        lista.setAdapter(adaptador);

        db = FirebaseFirestore.getInstance();
        listarEntregas();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Prestamos_Externos.this, detalle_prestamo.class).putExtra("pos",position).putExtra("tipo",false));
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        adaptador.notifyDataSetChanged();

    }

    public void listarEntregas(){

        ListaPrestamo.getLista().clear();
        adaptador.notifyDataSetChanged();

        db.collection("prestamos").whereEqualTo("tipo", false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : list) {
                                Prestamo prestamo = doc.toObject(Prestamo.class);
                                final ArrayList<Ejemplar> ListaEjemplares = new ArrayList<Ejemplar>();

                                db.collection("prestamos").document(doc.getId()).collection("ejemplares")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                                    for (DocumentSnapshot doc : list) {
                                                        String P = doc.getId();
                                                        Ejemplar ejemplar = doc.toObject(Ejemplar.class);
                                                        ejemplar.setIdPrestamo(P);
                                                        ListaEjemplares.add(ejemplar);
                                                    }
                                                }
                                            }
                                        });

                                prestamo.setEjemplares(ListaEjemplares);
                                ListaPrestamo.agregar(prestamo);
                            }
                            adaptador.notifyDataSetChanged();
                        }
                    }


        });
    }




}
