package br.com.pedrohmunhoz.appgameslibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    // Constant for database name
    private static final String NOME_BANCO = "DB_AppGamesLibrary";

    // Constant for database version
    private static final int VERSAO = 1;

    public Conexao(Context context) {

        super(context, NOME_BANCO, null, VERSAO);
    }

    // This method will be executed on the first run, to create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS consoles (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS games (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "gameYear INT NOT NULL," +
                "gameFinished INT NOT NULL," +
                "console_id INT NOT NULL," +
                "FOREIGN KEY(console_id) REFERENCES consoles(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}