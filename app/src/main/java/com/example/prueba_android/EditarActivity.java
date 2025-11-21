package com.example.prueba_android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        cargarDatos();

        btnActualizar.setOnClickListener(v -> {
            // Actualizado a versión 2
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_ANIMALES", null, 2);
            SQLiteDatabase dbUpdate = admin.getWritableDatabase();
            try {
                ContentValues valores = new ContentValues();
                valores.put("NOMBRE", txtNombre.getText().toString());
                valores.put("DESCRIPCION", txtDescripcion.getText().toString());
                valores.put("TIPO", txtTipo.getText().toString());
                valores.put("EDAD", Integer.parseInt(txtEdad.getText().toString()));

                int filas = dbUpdate.update("ANIMALES", valores, "ID=?", new String[]{String.valueOf(idAnimal)});
                
                if (filas > 0) {
                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                dbUpdate.close();
            }
        });
    }

    private void cargarDatos() {
        // Actualizado a versión 2
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_ANIMALES", null, 2);
        SQLiteDatabase db = admin.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM ANIMALES WHERE ID=?", new String[]{String.valueOf(idAnimal)});
            if (cursor.moveToFirst()) {
                int colNombre = cursor.getColumnIndex("NOMBRE");
                int colDesc = cursor.getColumnIndex("DESCRIPCION");
                int colTipo = cursor.getColumnIndex("TIPO");
                int colEdad = cursor.getColumnIndex("EDAD");

                if (colNombre >= 0) txtNombre.setText(cursor.getString(colNombre));
                if (colDesc >= 0) txtDescripcion.setText(cursor.getString(colDesc));
                if (colTipo >= 0) txtTipo.setText(cursor.getString(colTipo));
                if (colEdad >= 0) txtEdad.setText(String.valueOf(cursor.getInt(colEdad)));
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}
