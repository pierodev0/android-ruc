package com.example.finalproyect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private PersonaDAO objpersonadao;
    private EditText txtNombre, txtRuc;
    private Button btnGuardar, btnMostrar;

    private RadioButton rbtnPNatural;
    private RadioButton rbtnPJuridica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txtNombre);
        txtRuc = findViewById(R.id.txtRuc);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnMostrar = findViewById(R.id.btnMostrar);
        rbtnPNatural = findViewById(R.id.RbtnP_Naturales);
        rbtnPJuridica = findViewById(R.id.RbtnP_Juridicas);

        objpersonadao = new PersonaDAO(this);

        rbtnPNatural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbtnPJuridica.setChecked(false);
            }
        });

        rbtnPJuridica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbtnPNatural.setChecked(false);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabar();
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatos();
            }
        });

        try {
            objpersonadao.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Este método se utiliza para grabar (insertar) datos en la base de datos.
    private void grabar() {
        // Obtener el valor ingresado en el campo de texto 'txtNombre' y 'txtRuc'.
        String nombre = txtNombre.getText().toString();
        String ruc = txtRuc.getText().toString();

        // Llamar al método 'Insertar' de 'objpersonadao' para insertar los datos en la base de datos.
        // El método 'Insertar' devuelve un valor que indica el resultado de la inserción.
        long fila = objpersonadao.Insertar(nombre, ruc);

        // Verificar que el campo nombre no esté vacío y
        // que el campo RUC tenga exactamente 11 caracteres numéricos.
        if(nombre.length() == 0){
            txtNombre.setError("Campo inválido");
        }else if (ruc.length() != 11 || !ruc.matches("[0-9]+")){
            txtRuc.setError("RUC inválido (debe tener 11 números)");
        }else {
            // Verificar el resultado de la inserción.
            if (fila == 0) {
                Toast.makeText(getApplicationContext(), "REGISTRO NO INSERTADO", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "REGISTRO INSERTADO", Toast.LENGTH_LONG).show();
                // Limpiar los campos de texto 'txtNombre' y 'txtRuc'
                // y establecer el foco en 'txtNombre' para permitir una nueva entrada.
                txtNombre.setText("");
                txtRuc.setText("");
                txtNombre.requestFocus();
            }
        }


    }

    // Este método se utiliza para ver todos los registros de la base de datos.
    private void mostrarDatos() {
        try {
            // Llamar al método 'ListadoGeneral' de 'objpersonadao'
            // para obtener una lista de todas las personas en la base de datos.
            ArrayList<Persona>lista = objpersonadao.ListadoGeneral();

            // Verificar si la lista no está vacía.
            if (!lista.isEmpty()) {
                ArrayList<Persona> listaFiltrada = new ArrayList<>();

                // Comprobar cuál radio button está seleccionado para filtrar la lista
                // en función de la elección del usuario.
                if (rbtnPNatural.isChecked()) {
                    // Filtrar la lista para incluir solo personas cuyo RUC comienza con "10".
                    for (Persona persona : lista) {
                        if (persona.getRuc().startsWith("10")) {
                            listaFiltrada.add(persona);
                        }
                    }
                } else if (rbtnPJuridica.isChecked()) {
                    // Filtrar la lista para incluir solo personas cuyo RUC comienza con "20".
                    for (Persona persona : lista) {
                        if (persona.getRuc().startsWith("20")) {
                            listaFiltrada.add(persona);
                        }
                    }
                }

                // Verificar si la lista filtrada no está vacía.
                if (!listaFiltrada.isEmpty()) {
                    // Crear un StringBuilder para construir una cadena de texto
                    // con los nombres y RUC de las personas filtradas.
                    StringBuilder acum = new StringBuilder();
                    for (Persona obj : listaFiltrada) {
                        acum.append(obj.getNombre()).append(" ").append(obj.getRuc()).append("\n");
                    }

                    // Crear un Intent para abrir la actividad 'DatosActivity'
                    // y pasar la lista filtrada como datos extra.
                    Intent intent = new Intent(this, DatosActivity.class);
                    intent.putExtra("personas", listaFiltrada);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "NO HAY REGISTROS POR EL MOMENTO", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
