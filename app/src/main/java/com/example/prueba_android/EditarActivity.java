package com.example.prueba_android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditarActivity extends AppCompatActivity {

    EditText txtNombre, txtDescripcion, txtTipo, txtEdad;
    Button btnActualizar;
    int idAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtTipo = findViewById(R.id.txtTipo);
        txtEdad = findViewById(R.id.txtEdad);
        btnActualizar = findViewById(R.id.btnActualizar);

        idAnimal = getIntent().getIntExtra("ID", -1);

        SQLiteDatabase db = openOrCreateDatabase("BD_ANIMALES", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM ANIMALES WHERE ID=?", new String[]{String.valueOf(idAnimal)});
        if (cursor.moveToFirst()) {
            txtNombre.setText(cursor.getString(cursor.getColumnIndex("NOMBRE")));
            txtDescripcion.setText(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
            txtTipo.setText(cursor.getString(cursor.getColumnIndex("TIPO")));
            txtEdad.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("EDAD"))));
        }
        cursor.close();

        btnActualizar.setOnClickListener(v -> {
            db.execSQL("UPDATE ANIMALES SET NOMBRE=?, DESCRIPCION=?, TIPO=?, EDAD=? WHERE ID=?",
                    new Object[]{
                            txtNombre.getText().toString(),
                            txtDescripcion.getText().toString(),
                            txtTipo.getText().toString(),
                            Integer.parseInt(txtEdad.getText().toString()),
                            idAnimal
                    });
            db.close();
            finish();
        });
    }
}
