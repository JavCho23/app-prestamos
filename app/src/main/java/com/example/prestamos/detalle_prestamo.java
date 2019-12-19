package com.example.prestamos;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.prestamos.Clases.Carrito;
import com.example.prestamos.Clases.EjemplarAdaptador;
import com.example.prestamos.Clases.ListaPrestamo;
import com.example.prestamos.Clases.ListaPrestamoInterno;
import com.example.prestamos.Clases.Prestamo;
import com.example.prestamos.Clases.PrestamoAdaptador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class detalle_prestamo extends AppCompatActivity {
    private EjemplarAdaptador adaptador;
    private Button devolver;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String pres;
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

            devolver = findViewById(R.id.btnDevolver);

        final Prestamo prestamo;
        if(getIntent().getBooleanExtra("tipo",true)){
          prestamo=  ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0));
        }else prestamo = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0));
            dni.setText(prestamo.getDni());
            nombre.setText(prestamo.getNombre());
            procedencia.setText(prestamo.getProcedencia());
            telefono.setText(prestamo.getTelefono());
            correo.setText(prestamo.getCorreo());
        final ListView ejemplares = findViewById(R.id.listaDetallePrestamo);

        adaptador = new EjemplarAdaptador(this,prestamo.getEjemplares());
        ejemplares.setAdapter(adaptador);

        ejemplares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               if (prestamo.getEjemplares().isEmpty()){
                   new AlertDialog.Builder(detalle_prestamo.this)
                           .setTitle( "Confirmar")
                           .setMessage( "¿Esta seguro de devolver este ejemplar?")
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   if(getIntent().getBooleanExtra("tipo",true)){
                                       ListaPrestamoInterno.cambiarEstado(getIntent().getIntExtra("pos", 0), position);
                                       pres = ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0)).getId();
                                       ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0)).getEjemplares().remove(position);
                                   }else {
                                       ListaPrestamo.cambiarEstado(getIntent().getIntExtra("pos", 0), position);
                                       pres = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0)).getId();
                                       ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0)).getEjemplares().remove(position);
                                   }
                                   adaptador.notifyDataSetChanged();
                                   String idPrestamo = prestamo.getEjemplares().get(position).getIdPrestamo();
                                   db.collection("prestamos").document(pres)
                                           .collection("ejemplares")
                                           .document( idPrestamo)
                                           .delete()
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   prestamo.getEjemplares().remove(position);

                                                   Toast.makeText(detalle_prestamo.this, "Ejemplar devuelto", Toast.LENGTH_SHORT).show();
                                                   closeContextMenu();
                                               }
                                           });
                               }
                           })
                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   closeContextMenu();
                               }
                           }).show();
               }

            }
        });

        devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(detalle_prestamo.this)
                        .setTitle( "Confirmar")
                        .setMessage( "¿Esta seguro de devolver todos los ejemplares?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String prestamo;
                                if(getIntent().getBooleanExtra("tipo",true)){
                                   ListaPrestamoInterno.cambiarEstado(getIntent().getIntExtra("pos", 0));
                                   prestamo = ListaPrestamoInterno.getPrestamo(getIntent().getIntExtra("pos", 0)).getId();
                                   ListaPrestamoInterno.getLista().remove(getIntent().getIntExtra("pos", 0));
                                }else {
                                    ListaPrestamo.cambiarEstado(getIntent().getIntExtra("pos", 0));
                                    prestamo = ListaPrestamo.getPrestamo(getIntent().getIntExtra("pos", 0)).getId();
                                    ListaPrestamo.getLista().remove(getIntent().getIntExtra("pos", 0));
                                }

                                db.collection("prestamos").document(prestamo).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(detalle_prestamo.this, "Prestamo devuelto", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeContextMenu();
                            }
                        }).show();
            }
        });

    }
    @Override
    public void onResume()
    {
        super.onResume();
        adaptador.notifyDataSetChanged();

    }

}
