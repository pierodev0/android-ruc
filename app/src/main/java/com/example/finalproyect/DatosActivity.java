package com.example.finalproyect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DatosActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        listView = findViewById(R.id.listView);

        // Obtener la lista de personas pasada como dato extra desde el actividad Principal.
        ArrayList<Persona> personas = (ArrayList<Persona>) getIntent().getSerializableExtra("personas");

        // Crear una lista de cadenas de texto ('dataArray')
        // para almacenar la representación de cada persona.
        ArrayList<String> dataArray = new ArrayList<>();
        for (Persona persona : personas) {
            // Para cada persona en la lista, agregar una cadena que combine el nombre y el RUC en 'dataArray'.
            dataArray.add(persona.getNombre() + "\n" + persona.getRuc());
        }

        // Crear un adaptador ('adapter') para vincular 'dataArray' al ListView.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
        // Establecer el adaptador en el ListView para mostrar los datos en la pantalla.
        listView.setAdapter(adapter);
    }
}
