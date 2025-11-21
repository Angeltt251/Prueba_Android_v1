package com.example.prueba_android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {

    TextView lblNombre, lblDescripcion, lblTipo, lblEdad;
    Button btnEditar, btnEliminar;
    int idAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        lblNombre = findViewById(R.id.lblNombre);
        lblDescripcion = findViewById(R.id.lblDescripcion);
        lblTipo = findViewById(R.id.lblTipo);
        lblEdad = findViewById(R.id.lblEdad);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        idAnimal = getIntent().getIntExtra("ID", -1);

        SQLiteDatabase db = openOrCreateDatabase("BD_ANIMALES", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM ANIMALES WHERE ID=?", new String[]{String.valueOf(idAnimal)});
        if (cursor.moveToFirst()) {
            lblNombre.setText(cursor.getString(cursor.getColumnIndex("NOMBRE")));
            lblDescripcion.setText(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
            lblTipo.setText(cursor.getString(cursor.getColumnIndex("TIPO")));
            lblEdad.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("EDAD"))));
        }
        cursor.close();

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleActivity.this, EditarActivity.class);
            intent.putExtra("ID", idAnimal);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            db.execSQL("DELETE FROM ANIMALES WHERE ID=?", new Object[]{idAnimal});
            db.close();
            finish();
        });
    }
}
