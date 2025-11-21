package com.example.prueba_android;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgregarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button boton_guardar = findViewById(R.id.btnGuardar);

        boton_guardar.setOnClickListener(v -> {
            EditText input_nombre = findViewById(R.id.txtNombre);
            EditText input_descripcion = findViewById(R.id.txtDescripcion);
            EditText input_tipo = findViewById(R.id.txtTipo);
            EditText input_edad = findViewById(R.id.txtEdad);

            String nombre = input_nombre.getText().toString().trim();
            String descripcion = input_descripcion.getText().toString().trim();
            String tipo = input_tipo.getText().toString().trim();
            String edadStr = input_edad.getText().toString().trim();

            if (nombre.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || edadStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizado a versión 2 para forzar la creación correcta de la tabla
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BD_ANIMALES", null, 2);
            SQLiteDatabase db = admin.getWritableDatabase();

            try {
                ContentValues registro = new ContentValues();
                registro.put("NOMBRE", nombre);
                registro.put("DESCRIPCION", descripcion);
                registro.put("TIPO", tipo);
                registro.put("EDAD", Integer.parseInt(edadStr));

                long result = db.insert("ANIMALES", null, registro);

                if (result == -1) {
                    Toast.makeText(this, "Error al guardar el registro (Tabla no encontrada o error de datos)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Animal agregado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        });
    }
}
