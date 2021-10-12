package br.com.pedrohmunhoz.appgameslibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public static void SaveGameOnDatabase(Context context, Game game) {
        // Creates a ContentValues keypair to set the object values
        ContentValues values = new ContentValues();
        values.put("name", game.getName());
        values.put("gameYear", game.getGameYear());
        values.put("gameFinished", game.getGameFinished());
        values.put("console_id", game.getConsole_id());

        // Get a new SQLite connection
        Conexao conn = new Conexao(context);

        // Get an instance of the SQLite database, with write mode
        SQLiteDatabase db = conn.getWritableDatabase();

        // If the ID is greater than zero, means it's an update
        if (game.getId() > 0) {
            db.update("games", values, "id = " + game.getId(), null);
        } else // Else, it's a new object
        {
            db.insert("games", null, values);
        }
    }

    public static List<Game> getGames(Context context) {
        // Creates a new ArrayList to store the Games from database
        List<Game> listGames = new ArrayList<>();

        // Get a new SQLite connection
        Conexao conn = new Conexao(context);

        // Get an instance of the SQLite database, with read only mode
        SQLiteDatabase db = conn.getReadableDatabase();

        // Creates a cursor and store the SELECT results on it
        Cursor cursor = db.rawQuery("SELECT * FROM games ORDER BY name", null);

        // If there are any results from the query
        if (cursor.getCount() > 0) {

            // Move the cursor to the first element
            cursor.moveToFirst();

            // Loop through all the records from database, until the end
            do {
                // Create a new Game object
                Game game = new Game();

                // Set the ID based on the returned record
                game.setId(cursor.getInt(0));

                // Set the Name based on the returned record
                game.setName(cursor.getString(1));

                // Set the Game Year based on the returned record
                game.setGameYear(cursor.getInt(2));

                // Set the Game Finished flag based on the returned record
                game.setGameFinished(cursor.getInt(3));

                // Set the Console ID based on the returned record
                game.setConsole_id(cursor.getInt(4));

                // Add the record to the Games list
                listGames.add(game);
            }
            while (cursor.moveToNext());
        }

        // Return the list of consoles
        return listGames;
    }

    public static Game getGameByID(Context context, int idGame) {
        // Get a new SQLite connection
        Conexao conn = new Conexao(context);

        // Get an instance of the SQLite database, with read only mode
        SQLiteDatabase db = conn.getReadableDatabase();

        // Creates a cursor and store the SELECT results on it
        Cursor cursor = db.rawQuery("SELECT * FROM games WHERE id = " + idGame, null);

        // If there are any results from the query
        if (cursor.getCount() > 0) {

            // Move the cursor to the first element
            cursor.moveToFirst();

            // Create a new Game object
            Game game = new Game();

            // Set the ID based on the returned record
            game.setId(cursor.getInt(0));

            // Set the Name based on the returned record
            game.setName(cursor.getString(1));

            // Set the Game Year based on the returned record
            game.setGameYear(cursor.getInt(2));

            // Set the Game Finished flag based on the returned record
            game.setGameFinished(cursor.getInt(3));

            // Set the Console ID based on the returned record
            game.setConsole_id(cursor.getInt(4));

            // Return the found object
            return game;
        } else // If can't find an object with the ID, returns null
        {
            return null;
        }
    }

    public static void Delete(Context context, int game_id) {
        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getWritableDatabase();

        db.delete("games", "id = " + game_id, null);
    }
}
