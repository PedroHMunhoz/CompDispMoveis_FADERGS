package br.com.pedrohmunhoz.appfinancas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        Cursor cursor = db.rawQuery("SELECT * FROM lancamento WHERE usuario_id = '" + idUsuario + "' AND data BETWEEN '" + dataInicio.toString() + "' AND '" + dataFim.toString() + "' ORDER BY data", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Locale current = Locale.getDefault();
            NumberFormat formatter = NumberFormat.getInstance(current);
            formatter.setMaximumFractionDigits(2);

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

                double valor = cursor.getDouble(4);
                String databla = cursor.getString(5);

                // Formatação para string de exibição
                String valorFormat = formatter.format(valor);
                LocalDate date = LocalDate.parse(cursor.getString(5), dateTimeFormatter);
                lanc.setDataFormatada(date);
                lanc.setValorFormatado(valorFormat);

                listaLancamentos.add(lanc);
            } while (cursor.moveToNext());
        }

        return listaLancamentos;
    }

    public static TotaisLancamentos GetTotaisLancamentosUltimos7DiasByUser(Context context, String idUsuario,LocalDate dataInicio, LocalDate dataFim) {
        double totalReceitas = 0;
        double totalDespesas = 0;
        double total = 0;

        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM lancamento WHERE usuario_id = '" + idUsuario + "' AND data BETWEEN '" + dataInicio.toString() + "' AND '" + dataFim.toString() + "' ORDER BY data DESC", null);

        TotaisLancamentos totais = new TotaisLancamentos();

        if (cursor.getCount() > 0) {


            cursor.moveToFirst();
            do {
                int tipoLancamento = cursor.getInt(3);
                double valorLancamentoAtual = cursor.getDouble(4);

                // Receitas
                if (tipoLancamento == 1) {
                    totalReceitas += valorLancamentoAtual;
                } else {
                    totalDespesas += valorLancamentoAtual;
                }

            } while (cursor.moveToNext());

            total = totalReceitas - totalDespesas;

            totais.setValorReceitas(totalReceitas);
            totais.setValorDespesas(totalDespesas);
            totais.setValorTotal(total);
        } else {
            totais.setValorReceitas(0);
            totais.setValorDespesas(0);
            totais.setValorTotal(0);
        }

        return totais;
    }

    public static void excluir(Context context, int idLancamento) {
        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getWritableDatabase();

        db.delete("lancamento", "id = " + idLancamento, null);
    }
}
