package br.com.pedrohmunhoz.appfinancas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContaBancariaDAO {

    public static void inserir(Context context, ContaBancaria contaBancaria) {
        ContentValues values = new ContentValues();
        values.put("usuario_id", contaBancaria.getUsuario_id());
        values.put("nome_banco", contaBancaria.getNome_banco());
        values.put("numero_conta", contaBancaria.getNumero_conta());
        values.put("saldo_inicial", contaBancaria.getSaldo_inicial());

        // Ao inserir, gravamos o saldo atual = saldo inicial, pois é uma nova conta
        values.put("saldo_atual", contaBancaria.getSaldo_inicial());

        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();

        db.insert("contabancaria", null, values);
    }

    public static void editar(Context context, ContaBancaria contaBancaria) {
        ContentValues values = new ContentValues();
        values.put("nome_banco", contaBancaria.getNome_banco());
        values.put("numero_conta", contaBancaria.getNumero_conta());

        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();

        db.update("contabancaria", values, "usuario_id = '" + contaBancaria.getUsuario_id() + "' AND id = " + contaBancaria.getId(), null);
    }

    public static List<ContaBancaria> getContasByUser(Context context, String idUsuario) {
        List<ContaBancaria> listaContaBancaria = new ArrayList<>();

        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM contabancaria WHERE usuario_id = '" + idUsuario + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                ContaBancaria conta = new ContaBancaria();
                conta.setId(cursor.getInt(0));
                conta.setUsuario_id(cursor.getString(1));
                conta.setNome_banco(cursor.getString(2));
                conta.setNumero_conta(cursor.getInt(3));
                conta.setSaldo_inicial(cursor.getDouble(4));
                conta.setSaldo_atual(cursor.getDouble(5));
                listaContaBancaria.add(conta);
            } while (cursor.moveToNext());
        }

        return listaContaBancaria;
    }

    public static void atualizaSaldoConta(Context context, String idUsuario, int idConta, int tipoLancamento, double valor) {
        double valorSaldoAtual = 0;
        double valorSaldoNovo = 0;
        ContaBancaria conta = new ContaBancaria();

        List<ContaBancaria> contas = new ArrayList<>();
        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT saldo_atual, id FROM contabancaria WHERE usuario_id = '" + idUsuario + "' AND id = " + idConta, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            conta.setSaldo_atual(cursor.getDouble(0));
            conta.setId(cursor.getInt(1));

            valorSaldoAtual = conta.getSaldo_atual();

            // Receita
            if (tipoLancamento == 1) {
                valorSaldoNovo = valorSaldoAtual + valor;
            } else // Despesa
            {
                valorSaldoNovo = valorSaldoAtual - valor;
            }

            ContentValues values = new ContentValues();
            values.put("saldo_atual", valorSaldoNovo);

            db = conn.getWritableDatabase();

            db.update("contabancaria", values, "usuario_id = '" + idUsuario + "' AND id = " + conta.getId(), null);
        }
    }
}
