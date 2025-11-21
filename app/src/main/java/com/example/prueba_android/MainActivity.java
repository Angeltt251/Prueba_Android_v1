package com.example.prueba_android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewDatos;
    Button boton_agregar;
    ArrayList<Animal> lista_animales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewDatos = findViewById(R.id.lista_animales);
        boton_agregar = findViewById(R.id.boton_agregar);

        // Botón agregar → abrir AgregarActivity
        boton_agregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarActivity.class);
            startActivity(intent);
        });

        // Click en item → abrir DetalleActivity
        listViewDatos.setOnItemClickListener((parent, view, position, id) -> {
            Animal objeto_actual = lista_animales.get(position);
            Intent intent_detalle = new Intent(MainActivity.this, DetalleActivity.class);
            intent_detalle.putExtra("ID", objeto_actual.ID);
            startActivity(intent_detalle);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarAnimales(); // refresca cada vez que vuelves
    }

    private void cargarAnimales() {
        // Conectarnos a la BD
        SQLiteDatabase db = openOrCreateDatabase("BD_ANIMALES", Context.MODE_PRIVATE, null);

        // Crear tabla si no existe
        db.execSQL("CREATE TABLE IF NOT EXISTS ANIMALES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NOMBRE VARCHAR, " +
                "DESCRIPCION VARCHAR, " +
                "TIPO VARCHAR, " +
                "EDAD INTEGER)");

        // Leer datos
        Cursor cursor_listar = db.rawQuery("SELECT * FROM ANIMALES", null);

        int ID = cursor_listar.getColumnIndex("ID");
        int NOMBRE = cursor_listar.getColumnIndex("NOMBRE");
        int DESCRIPCION = cursor_listar.getColumnIndex("DESCRIPCION");
        int TIPO = cursor_listar.getColumnIndex("TIPO");
        int EDAD = cursor_listar.getColumnIndex("EDAD");

        lista_animales = new ArrayList<>();
        ArrayList<String> arreglo = new ArrayList<>();

        while (cursor_listar.moveToNext()) {
            Animal obj = new Animal();
            obj.ID = cursor_listar.getInt(ID);
            obj.NOMBRE = cursor_listar.getString(NOMBRE);
            obj.DESCRIPCION = cursor_listar.getString(DESCRIPCION);
            obj.TIPO = cursor_listar.getString(TIPO);
            obj.EDAD = cursor_listar.getInt(EDAD);

            lista_animales.add(obj);

            arreglo.add("# " + obj.ID + " " + obj.NOMBRE + " - " + obj.DESCRIPCION +
                    " (" + obj.TIPO + ", Edad: " + obj.EDAD + ")");
        }

        cursor_listar.close();
        db.close();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arreglo);
        listViewDatos.setAdapter(adapter);
    }
}
