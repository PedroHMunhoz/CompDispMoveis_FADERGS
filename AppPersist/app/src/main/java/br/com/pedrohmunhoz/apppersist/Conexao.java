package br.com.pedrohmunhoz.apppersist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    // Variável constante que contém o nome do banco SQLite que será criado no dispositivo
    private static final String NOME_BANCO = "AppPersist_DB";

    // Versão do banco de dados, para controle de upgrades de versão
    private static final int VERSAO = 1;

    // Construtor recebe o contexto da aplicação por parâmetro, para instanciar o super
    public Conexao(Context context) {

        // Chamamos o métood construtor da classe pai passando contexto, nome e versão do banco
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Na criação do SQLite vamos rodar essa instrução SQL para criar a tabela de produtos
        db.execSQL("CREATE TABLE IF NOT EXISTS produtos (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "categoria TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
