package com.example.prueba_android;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        cargarDatos();

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleActivity.this, EditarActivity.class);
            intent.putExtra("ID", idAnimal);
            startActivity(intent);
            finish(); 
        });

        btnEliminar.setOnClickListener(v -> {
            // Actualizado a versión 2
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_ANIMALES", null, 2);
            SQLiteDatabase dbEliminar = admin.getWritableDatabase();
            try {
                int cantidad = dbEliminar.delete("ANIMALES", "ID=?", new String[]{String.valueOf(idAnimal)});
                if (cantidad > 0) {
                    Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                dbEliminar.close();
            }
            finish();
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

                if (colNombre >= 0) lblNombre.setText(cursor.getString(colNombre));
                if (colDesc >= 0) lblDescripcion.setText(cursor.getString(colDesc));
                if (colTipo >= 0) lblTipo.setText(cursor.getString(colTipo));
                if (colEdad >= 0) lblEdad.setText(String.valueOf(cursor.getInt(colEdad)));
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}
