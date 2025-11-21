package com.example.prueba_android;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

            SQLiteDatabase db = openOrCreateDatabase("BD_ANIMALES", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS ANIMALES (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRE VARCHAR, " +
                    "DESCRIPCION VARCHAR, " +
                    "TIPO VARCHAR, " +
                    "EDAD INTEGER)");

            String sql = "INSERT INTO ANIMALES (NOMBRE, DESCRIPCION, TIPO, EDAD) VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, nombre);
            statement.bindString(2, descripcion);
            statement.bindString(3, tipo);
            statement.bindLong(4, Integer.parseInt(edadStr));
            statement.execute();
            db.close();

            Toast.makeText(this, "Animal agregado correctamente", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AgregarActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
