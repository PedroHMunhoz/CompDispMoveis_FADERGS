package br.com.pedrohmunhoz.appgameslibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    public static void Insert(Context context, Game game) {
        ContentValues values = new ContentValues();
        values.put("name", game.getName());
        values.put("gameYear", game.getGameYear());
        values.put("gameFinished", game.getGameFinished());
        values.put("console_id", game.getConsole_id());
        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.insert("games", null, values);
    }

    public static void Update(Context context, Game game) {
        ContentValues values = new ContentValues();
        values.put("name", game.getName());
        values.put("gameYear", game.getGameYear());
        values.put("gameFinished", game.getGameFinished());
        values.put("console_id", game.getConsole_id());
        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();
        db.update("games", values, "id = " + game.getId(), null);
    }

    public static List<Game> getGames(Context context) {
        List<Game> listGames = new ArrayList<>();
        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM games ORDER BY name", null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                Game game = new Game();
                game.setId(cursor.getInt(0));
                game.setName(cursor.getString(1));
                game.setGameYear(cursor.getInt(2));
                game.setGameFinished(cursor.getInt(3));
                game.setConsole_id(cursor.getInt(4));

                listGames.add(game);
            }
            while (cursor.moveToNext());
        }

        return listGames;
    }

    public static Game getGameByID(Context context, int idGame) {
        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM games WHERE id = " + idGame, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Game game = new Game();
            game.setId(cursor.getInt(0));
            game.setName(cursor.getString(1));
            game.setGameYear(cursor.getInt(2));
            game.setGameFinished(cursor.getInt(3));
            game.setConsole_id(cursor.getInt(4));
            return game;
        } else {
            return null;
        }
    }
}
