package com.example.prestamos;

import android.content.Intent;
import android.os.Bundle;

import com.example.prestamos.Clases.Carrito;
import com.example.prestamos.Clases.Ejemplar;
import com.example.prestamos.Clases.Prestamo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class activity_prestamos extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button registrar = findViewById(R.id.generarPrestamo);
        final EditText dni = findViewById(R.id.dni);
        final EditText nombre = findViewById(R.id.nombrePersona);
        final EditText procedencia = findViewById(R.id.procedencia);
        final EditText correo = findViewById(R.id.correo);
        final EditText telefono = findViewById(R.id.numeroCelular);
        EditText fecha = findViewById(R.id.fechaPrestamo);
        final RadioButton interno = findViewById(R.id.interno);
        final String fechaActual = DateFormat.getDateTimeInstance().format(new Date());

        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        fecha.setText(fechaActual);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar.setEnabled(false);
                Prestamo prestamo = new Prestamo(correo.getText().toString(),dni.getText().toString(),fechaActual,nombre.getText().toString(),procedencia.getText().toString(),telefono.getText().toString(),interno.isActivated());
                db.collection("prestamos")
                        .add(prestamo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Carrito.cambiarEstado();
                                for (Ejemplar ejemplar: Carrito.getLista()) {
                                    db.collection("prestamos")
                                            .document(documentReference.getId()).collection("ejemplares").add(ejemplar)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(activity_prestamos.this, "Prestamo registrado" , Toast.LENGTH_LONG).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(activity_prestamos.this, "Prestamo no registrado" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_prestamos.this, "Prestamo no registrado" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }

}
