package com.example.prestamos;

import android.content.Intent;
import android.os.Bundle;

import com.example.prestamos.Clases.Ejemplar;
import com.example.prestamos.Clases.ListaPrestamoInterno;
import com.example.prestamos.Clases.Prestamo;
import com.example.prestamos.Clases.PrestamoAdaptador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Prestamos_Internos extends AppCompatActivity {
    private FirebaseFirestore db;
    private PrestamoAdaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros_prestados);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lista;
        lista = findViewById(R.id.lista_prestados);
        adaptador = new PrestamoAdaptador(this, ListaPrestamoInterno.getLista());
        lista.setAdapter(adaptador);

        db = FirebaseFirestore.getInstance();
        listarEntregas();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Prestamos_Internos.this, detalle_prestamo.class).putExtra("pos",position).putExtra("tipo",true));
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

        ListaPrestamoInterno.getLista().clear();
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
                                                        ejemplar.setIdPrestamo(doc.getId());

                                                        ListaEjemplares.add(ejemplar);
                                                    }
                                                }
                                            }
                                        });
                                prestamo.setEjemplares(ListaEjemplares);
                                ListaPrestamoInterno.agregar(prestamo);
                            }
                            adaptador.notifyDataSetChanged();
                        }
                    }


                });
    }

}
