package br.com.pedrohmunhoz.apppersistfirebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public static void inserir(Context context, Produto produto) {
        /** Aqui usamos a classe ContentValues como um QueryBuilder, para facilitar a manipulação
         * dos dados que vamos receber da interface para inserir no banco de dados */
        ContentValues values = new ContentValues();

        /** Montamos usando os ContentValues as duas informações que vamos receber do usuário,
         * que são Nome e Categoria do produto */
        values.put("nome", produto.getNome());
        values.put("categoria", produto.getCategoria());

        // Instancia um objeto da classe Conexao e passa o Context para ela
        Conexao conn = new Conexao(context);

        // Instancia um objeto da classe SQLiteDatabase em modo escrita, pois vamos gravar dados
        SQLiteDatabase db = conn.getWritableDatabase();

        /** Chamamos o método insert do SQLite, passando o nome da tabela e o nosso ContentValues.
         * Importante lembrar que se os ContentValues estiverem VAZIOS, irá ocorrer um erro neste
         * método e há risco até de corromper o arquivo do banco de dados. Portanto, devemos nos
         * certificar que sempre haverá dados ou então preencher as informações do parâmetro
         * "nullColumnHacks" com os valores padrão caso não sejam enviados.
         * */
        db.insert("produtos", null, values);
    }

    public static void editar(Context context, Produto produto) {
        /** Aqui usamos a classe ContentValues como um QueryBuilder, para facilitar a manipulação
         * dos dados que vamos receber da interface para inserir no banco de dados */
        ContentValues values = new ContentValues();

        /** Montamos usando os ContentValues as duas informações que vamos receber do usuário,
         * que são Nome e Categoria do produto */
        values.put("nome", produto.getNome());
        values.put("categoria", produto.getCategoria());

        // Instancia um objeto da classe Conexao e passa o Context para ela
        Conexao conn = new Conexao(context);

        // Instancia um objeto da classe SQLiteDatabase em modo escrita, pois vamos gravar dados
        SQLiteDatabase db = conn.getWritableDatabase();

        // Chamamos o método update do SQLite, passando a tabela e a cláusula WHERE do update
        db.update("produtos", values, "id = " + produto.getId(), null);
    }

    public static void excluir(Context context, int idProduto) {
        // Instancia um objeto da classe Conexao e passa o Context para ela
        Conexao conn = new Conexao(context);

        // Instancia um objeto da classe SQLiteDatabase em modo escrita, pois vamos gravar dados
        SQLiteDatabase db = conn.getWritableDatabase();

        // Chamamos o método delete do SQLite, passando a tabela e a cláusula WHERE do delete
        db.delete("produtos", "id = " + idProduto, null);
    }

    public static List<Produto> getProdutos(Context context) {
        // Criamos um objeto que vai ser a lista de produtos recebidas a partir do banco de dados
        List<Produto> listaProdutos = new ArrayList<>();

        // Instancia um objeto da classe Conexao e passa o Context para ela
        Conexao conn = new Conexao(context);

        // Instancia um objeto da classe SQLiteDatabase em modo leitura, pois não vamos gravar dados
        SQLiteDatabase db = conn.getReadableDatabase();

        /** Instancia uma variável do tipo Cursor, para receber os dados retornados pela consulta.
         * Usamos aqui o método rawQuery pois ele nos retorna um Cursor para que então tenhamos
         * acesso aos dados da consulta que foi executada.*/
        Cursor cursor = db.rawQuery("SELECT * FROM produtos ORDER BY nome", null);

        // Validamos se algum resultado foi retornado, para iterarmos entre eles e popular nosso ArrayList
        if (cursor.getCount() > 0) {

            // Para garantir que vamos percorrer o cursor desde o início, invocamos o método
            // moveToFirst() que reposiciona o ponteiro no primeiro elemento do Cursor
            cursor.moveToFirst();

            // Fazemos um loop Do...While usando o método moveToNext(), pois quando não houver mais
            // dados no cursor, ele retorna FALSE e sairá do loop
            do {
                // Instancia uma nova variável do tipo Produto
                Produto produto = new Produto();

                /** Seta os dados do produto com o resultado do Cursor. Como fizemos um SELECT geral,
                 * serão retornadas as 3 colunas da tabela produto, que ficarão posicionadas
                 * nos índices 0, 1 e 2 do Cursor, respectivamente. Por isso, ao setar as variáveis
                 * do objeto produtos, passamos o index correspondente para o método get do SQLite.
                 * */
                produto.setId(cursor.getString(0));
                produto.setNome(cursor.getString(1));
                produto.setCategoria(cursor.getString(2));

                // Adicionamos o produto recém populado na lista de produtos que vamos retornar
                listaProdutos.add(produto);
            }
            while (cursor.moveToNext());
        }

        // Retornamos a lista, preenchida (se houver dados) ou em branco se o banco estiver vazio
        return listaProdutos;
    }

    public static Produto getProdutoByID(Context context, int idProduto) {
        // Criamos um objeto que vai ser a lista de produtos recebidas a partir do banco de dados
        List<Produto> listaProdutos = new ArrayList<>();

        // Instancia um objeto da classe Conexao e passa o Context para ela
        Conexao conn = new Conexao(context);

        // Instancia um objeto da classe SQLiteDatabase em modo leitura, pois não vamos gravar dados
        SQLiteDatabase db = conn.getReadableDatabase();

        /** Instancia uma variável do tipo Cursor, para receber os dados retornados pela consulta.
         * Usamos aqui o método rawQuery pois ele nos retorna um Cursor para que então tenhamos
         * acesso aos dados da consulta que foi executada.*/
        Cursor cursor = db.rawQuery("SELECT * FROM produtos WHERE id = " + idProduto, null);

        // Validamos se algum resultado foi retornado, para iterarmos entre eles e popular nosso ArrayList
        if (cursor.getCount() > 0) {

            // Para garantir que vamos percorrer o cursor desde o início, invocamos o método
            // moveToFirst() que reposiciona o ponteiro no primeiro elemento do Cursor
            cursor.moveToFirst();

            // Instancia uma nova variável do tipo Produto
            Produto produto = new Produto();

            /** Seta os dados do produto com o resultado do Cursor. Como fizemos um SELECT geral,
             * serão retornadas as 3 colunas da tabela produto, que ficarão posicionadas
             * nos índices 0, 1 e 2 do Cursor, respectivamente. Por isso, ao setar as variáveis
             * do objeto produtos, passamos o index correspondente para o método get do SQLite.
             * */
            produto.setId(cursor.getString(0));
            produto.setNome(cursor.getString(1));
            produto.setCategoria(cursor.getString(2));

            // Retorna o objeto Produto recém populado
            return produto;
        }
        else
        {
            // Se não for encontrado um produto com o ID informado, retorna NULL
            return  null;
        }
    }
}