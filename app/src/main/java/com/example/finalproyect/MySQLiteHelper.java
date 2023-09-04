package com.example.finalproyect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String NOMBRETABLA = "persona";
    private  static final String NOMBREBASEDATOS = "BDEMPRESAS.db";
    private static final int VERSION = 1;

    private static final String SQLBD =" create table "+NOMBRETABLA+ "(codigo integer primary key autoincrement ," + "nombre text not null, ruc text not null unique);";

    public MySQLiteHelper(Context context){
        super(context, NOMBREBASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQLBD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int antiguaversion, int nuevaVersion) {
        Log.w(MySQLiteHelper.class.getName(),"ACTUALIZANDO LA VERSION DE BASE DE DATOS: "+antiguaversion+ "a" +nuevaVersion);
        db.execSQL("DROP TABLE IF EXISTS "+NOMBRETABLA);
        onCreate(db);
    }
}
