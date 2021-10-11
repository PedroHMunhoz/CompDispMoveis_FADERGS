package br.com.pedrohmunhoz.appgameslibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConsoleDAO {

    public static void Insert(Context context, Console console) {
        ContentValues values = new ContentValues();
        values.put("name", console.getName());

        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getWritableDatabase();

        db.insert("consoles", null, values);
    }

    public static List<Console> getConsoles(Context context) {
        List<Console> listConsoles = new ArrayList<>();
        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM consoles ORDER BY name", null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Console console = new Console();
                console.setId(cursor.getInt(0));
                console.setName(cursor.getString(1));

                listConsoles.add(console);
            }
            while (cursor.moveToNext());
        }

        return listConsoles;
    }

    public static void Update(Context context, Console console) {
        ContentValues values = new ContentValues();
        values.put("name", console.getName());
        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.update("consoles", values, "id = " + console.getId(), null);
    }

    public static Console getConsoleByID(Context context, int idConsole) {
        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM consoles WHERE id = " + idConsole, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Console console = new Console();
            console.setId(cursor.getInt(0));
            console.setName(cursor.getString(1));
            return console;
        } else {
            return null;
        }
    }
}
