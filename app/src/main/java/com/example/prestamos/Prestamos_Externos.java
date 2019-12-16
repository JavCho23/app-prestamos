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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


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
                startActivity(new Intent(Prestamos_Externos.this, detalle_prestamo.class).putExtra("pos",position));
            }
        });

    }

    public void listarEntregas(){

        ListaPrestamo.getLista().clear();
        adaptador.notifyDataSetChanged();

        db.collection("prestamos").whereEqualTo("tipo", true)
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
                                                        Ejemplar ejemplar = doc.toObject(Ejemplar.class);

                                                        ListaEjemplares.add(ejemplar);
                                                    }
                                                }
                                            }
                                        });

                                ListaPrestamo.agregar(prestamo);
                            }
                            adaptador.notifyDataSetChanged();
                        }
                    }


        });
    }


}
