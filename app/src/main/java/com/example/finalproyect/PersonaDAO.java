package com.example.finalproyect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class PersonaDAO {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public PersonaDAO(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    // Este método se utiliza para abrir una conexión de lectura/escritura a la base de datos.
    public void open() throws SQLException {
        // En este caso, se utiliza el objeto 'dbHelper' (que generalmente es una instancia de una clase que extiende SQLiteOpenHelper)
        // para obtener una referencia a la base de datos en modo escritura (getWritableDatabase()).
        // Esto significa que se permite realizar operaciones de escritura en la base de datos.
        // La conexión a la base de datos se almacena en la variable 'database' para su posterior uso.
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    // Método para insertar nuevos registros en la BD
    public long Insertar(String nomb, String ruc) {
        long estado = 0;  // Inicializamos la variable 'estado' con 0 (que generalmente indica un fallo).

        try {
            ContentValues valores = new ContentValues();  // Creamos un objeto ContentValues para almacenar los valores que queremos insertar en la base de datos.
            valores.put("nombre", nomb);  // Agregamos el valor del nombre a la columna "nombre" en el objeto ContentValues.
            valores.put("ruc", ruc);  // Agregamos el valor del ruc a la columna "ruc" en el objeto ContentValues.

            // Luego, realizamos la inserción en la base de datos utilizando el objeto 'database', que debería ser una instancia de la clase SQLiteDatabase.
            // Utilizamos el método 'insert' proporcionado por la instancia de la base de datos para realizar la inserción.
            // El primer argumento es el nombre de la tabla en la que se va a insertar el registro (MySQLiteHelper.NOMBRETABLA).
            // El segundo argumento es generalmente nulo, ya que se utiliza para insertar filas vacías.
            // El tercer argumento es el objeto ContentValues que contiene los valores que se insertarán.
            estado = database.insert(MySQLiteHelper.NOMBRETABLA, null, valores);
        } catch (Exception e) {
            estado = 0;  // Si ocurre alguna excepción, establecemos el 'estado' nuevamente en 0 para indicar un fallo.
        }

        return estado;  // Devolvemos el valor de 'estado' después de intentar la inserción en la base de datos.
    }

    public long EliminarRegistro(int codigo){
        long estado= 0;
        try {
            estado = database.delete(MySQLiteHelper.NOMBRETABLA,"codigo=?",
            new String[]{String.valueOf(codigo)});
            }catch (Exception e){
            estado=0;
            }
            return estado;
    }

    public long ModificarRegistro(String codigo, String nombre, String ruc){
        long estado=0;
        try {
            ContentValues valores = new ContentValues();
            valores.put("nombre", nombre);
            valores.put("ruc", ruc);
            estado = database.update(MySQLiteHelper.NOMBRETABLA, valores, "codigo="+codigo,null);
        }catch (Exception e){
            estado = 0;
        }
        return estado;
    }

    // Este método recupera todos los registros de la tabla "persona" de la base de datos
    // y los devuelve como una lista de objetos Persona.
    public ArrayList<Persona> ListadoGeneral(){
        Cursor c; // Declara un objeto Cursor para obtener los resultados de la consulta.
        ArrayList<Persona> listado = new ArrayList<Persona>(); // Crea una lista vacía para almacenar los objetos Persona.
        // Ejecuta una consulta SQL para seleccionar todos los registros de la tabla "persona".
        // El resultado se almacena en el objeto Cursor 'c'.
        c = database.rawQuery("SELECT * FROM persona", null);
        // Mientras haya más filas en el cursor, se crea un nuevo objeto Persona para cada fila
        // y se llena con los valores del cursor.
        while (c.moveToNext()){
            Persona objpersona=new Persona();
                    objpersona.setCodigo(c.getInt(0)); // Se obtiene el valor de la columna 0 (generalmente el ID o código) y se establece en el objeto Persona.
                    objpersona.setNombre(c.getString(1)); // Se obtiene el valor de la columna 1 (generalmente el nombre) y se establece en el objeto Persona.
                    objpersona.setRuc(c.getString(2)); // Se obtiene el valor de la columna 2 (generalmente el RUC) y se establece en el objeto Persona.
            listado.add(objpersona); // Se agrega el objeto Persona a la lista.
        }
        c.close(); // Se cierra el cursor cuando ya no se necesita.
        return listado; // Devuelve la lista de objetos Persona que contiene todos los registros de la tabla "persona".
    }
}
