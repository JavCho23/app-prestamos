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
import java.util.AbstractCollection;
import java.util.Date;

public class activity_prestamos extends AppCompatActivity {
    EditText dni, nombre, procedencia, correo, telefono;
    FirebaseFirestore db;
    String fechaActual;
    Button registrar;
    RadioButton interno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

<<<<<<< HEAD
        registrar = findViewById(R.id.generarPrestamo);
        dni = findViewById(R.id.dni);
        nombre = findViewById(R.id.nombrePersona);
        procedencia = findViewById(R.id.procedencia);
        correo = findViewById(R.id.correo);
        telefono = findViewById(R.id.correo);
=======
        final Button registrar = findViewById(R.id.generarPrestamo);
        final EditText dni = findViewById(R.id.dni);
        final EditText nombre = findViewById(R.id.nombrePersona);
        final EditText procedencia = findViewById(R.id.procedencia);
        final EditText correo = findViewById(R.id.correo);
        final EditText telefono = findViewById(R.id.numeroCelular);
>>>>>>> afa990b9aa37b8e60874023e141a2795ca67bdb1
        EditText fecha = findViewById(R.id.fechaPrestamo);
        interno = findViewById(R.id.interno);
        interno.setSelected(true);
        fechaActual = DateFormat.getDateTimeInstance().format(new Date());

        db = FirebaseFirestore.getInstance();

        fecha.setText(fechaActual);


            registrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        generarPrestamo(v);

                }

            });

    }
    public void generarPrestamo (View v){
        if (validar()){

            registrar.setEnabled(false);
            Prestamo prestamo = new Prestamo(correo.getText().toString(), dni.getText().toString(), fechaActual, nombre.getText().toString(), procedencia.getText().toString(), telefono.getText().toString(), interno.isActivated());

            db.collection("prestamos")
                    .add(prestamo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Carrito.cambiarEstado();
                            for (Ejemplar ejemplar : Carrito.getLista()) {
                                db.collection("prestamos")
                                        .document(documentReference.getId()).collection("ejemplares").add(ejemplar)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(activity_prestamos.this, "Préstamo registrado", Toast.LENGTH_LONG).show();
                                                onBackPressed();

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
                            Toast.makeText(activity_prestamos.this, "Préstamo no registrado" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    public boolean validar(){
        boolean retorno=true;

        if(dni.getText().length() == 0){
            dni.setError("Ingrese DNI");
            retorno=false;
        }
        if (nombre.getText().length()==0){
            nombre.setError("Ingrese nombre");
            retorno=false;
        }
        if(procedencia.getText().length()==0){
            procedencia.setError("Ingrese procedencia");
            retorno=false;
        }
        if (correo.getText().length()==0){
            correo.setError("Ingrese correo");
            retorno=false;
        }
        if (telefono.getText().length()==0){
            telefono.setError("Ingrese teléfono");
            retorno=false;
        }

        return  retorno;
    }

}
