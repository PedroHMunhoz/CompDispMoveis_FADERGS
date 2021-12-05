package br.com.pedrohmunhoz.appfinancas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static void inserir(Context context, Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("id", usuario.getId());
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());

        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();

        db.insert("usuario", null, values);
    }

    public static void editar(Context context, Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());

        Conexao conn = new Conexao(context);
        SQLiteDatabase db = conn.getWritableDatabase();

        db.update("usuario", values, "id = " + usuario.getId(), null);
    }

    public static Usuario getUsuarioByID(Context context, String idUsuario) {
        List<Usuario> listaUsuarios = new ArrayList<>();

        Conexao conn = new Conexao(context);

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE id = '" + idUsuario + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Usuario user = new Usuario();
            user.setId(cursor.getString(0));
            user.setNome(cursor.getString(1));
            user.setEmail(cursor.getString(2));

            return user;
        } else {
            return null;
        }
    }
}
