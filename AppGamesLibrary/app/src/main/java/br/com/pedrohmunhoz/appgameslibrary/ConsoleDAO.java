package br.com.pedrohmunhoz.appgameslibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConsoleDAO {

    public static void SaveConsoleOnDatabase(Context context, Console console) {
        // Creates a ContentValues keypair to set the object values
        ContentValues values = new ContentValues();
        values.put("name", console.getName());

        // Get a new SQLite connection
        Conexao conn = new Conexao(context);

        // Get an instance of the SQLite database, with write mode
        SQLiteDatabase db = conn.getWritableDatabase();

        // If the ID is greater than zero, means it's an update
        if (console.getId() > 0) {
            db.update("consoles", values, "id = " + console.getId(), null);
        } else // Else, it's a new object
        {
            db.insert("consoles", null, values);
        }
    }

    public static List<Console> getConsoles(Context context) {
        // Creates a new ArrayList to store the Consoles from database
        List<Console> listConsoles = new ArrayList<>();

        // Get a new SQLite connection
        Conexao conn = new Conexao(context);

        // Get an instance of the SQLite database, with read only mode
        SQLiteDatabase db = conn.getReadableDatabase();

        // Creates a cursor and store the SELECT results on it
        Cursor cursor = db.rawQuery("SELECT * FROM consoles ORDER BY id", null);

        // If there are any results from the query
        if (cursor.getCount() > 0) {

            // Move the cursor to the first element
            cursor.moveToFirst();

            // Loop through all the records from database, until the end
            do {
                // Create a new Console object
                Console console = new Console();

                // Set the ID based on the returned record
                console.setId(cursor.getInt(0));

                // Set the Name based on the returned record
                console.setName(cursor.getString(1));

                // Add the record to the Consoles list
                listConsoles.add(console);
            }
            while (cursor.moveToNext());
        }

        // Return the list of consoles
        return listConsoles;
    }

    public static Console getConsoleByID(Context context, int idConsole) {
        // Get a new SQLite connection
        Conexao conn = new Conexao(context);

        // Get an instance of the SQLite database, with read only mode
        SQLiteDatabase db = conn.getReadableDatabase();

        // Creates a cursor and store the SELECT results on it
        Cursor cursor = db.rawQuery("SELECT * FROM consoles WHERE id = " + idConsole, null);

        // If there are any results from the query
        if (cursor.getCount() > 0) {

            // Move the cursor to the first element
            cursor.moveToFirst();

            // Create a new Console object
            Console console = new Console();

            // Set the ID based on the returned record
            console.setId(cursor.getInt(0));

            // Set the Name based on the returned record
            console.setName(cursor.getString(1));

            // Return the found object
            return console;
        } else // If can't find an object with the ID, returns null
        {
            return null;
        }
    }
}
