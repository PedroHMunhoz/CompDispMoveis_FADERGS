package br.com.pedrohmunhoz.appfinancas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

public class LancamentoDAO {

    public static void inserir(Context context, Lancamento lancamento) {
        ContentValues values = new ContentValues();
        values.put("usuario_id", lancamento.getUsuario_id());
        values.put("descricao", lancamento.getDescricao());
        values.put("tipoLancamento", lancamento.getTipoLancamento());
        values.put("valor", lancamento.getValor());
        values.put("data", lancamento.getData().toString());

        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();

        db.insert("lancamento", null, values);
    }

    public static List<Lancamento> getLancamentosByUser(Context context, String idUsuario, LocalDate dataInicio, LocalDate dataFim) {
        List<Lancamento> listaLancamentos = new ArrayList<>();

        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM lancamento WHERE usuario_id = '" + idUsuario + "' AND data BETWEEN '" + dataInicio.toString() + "' AND '" + dataFim.toString() + "' ORDER BY data DESC", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            String dateFormat = "uuuu-MM-dd";

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofPattern(dateFormat)
                    .withResolverStyle(ResolverStyle.STRICT);
            do {

                Lancamento lanc = new Lancamento(context);
                lanc.setId(cursor.getInt(0));

                lanc.setUsuario_id(cursor.getString(1));
                lanc.setDescricao(cursor.getString(2));
                lanc.setTipoLancamento(cursor.getInt(3));
                lanc.setValor(cursor.getDouble(4));

                String databla = cursor.getString(5);
                LocalDate date = LocalDate.parse(cursor.getString(5), dateTimeFormatter);
                lanc.setDataFormatada(date);

                listaLancamentos.add(lanc);
            } while (cursor.moveToNext());
        }

        return listaLancamentos;
    }
}
