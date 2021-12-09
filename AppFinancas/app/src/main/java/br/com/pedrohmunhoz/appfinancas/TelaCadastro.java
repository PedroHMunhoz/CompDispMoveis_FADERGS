package br.com.pedrohmunhoz.appfinancas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class TelaCadastro extends AppCompatActivity {

    Button btnSalvar;
    EditText etEmail;
    EditText etNome;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        auth = FirebaseAuth.getInstance();

        String emailUsuario = auth.getCurrentUser().getEmail();
        String id = auth.getCurrentUser().getUid();

        Usuario userDb = UsuarioDAO.getUsuarioByID(TelaCadastro.this, id);
        String nomeUsuarioDb = userDb.getNome();

        etEmail = findViewById(R.id.etEmail);
        etNome = findViewById(R.id.etNome);
        btnSalvar = findViewById(R.id.btnSalvar);

        etEmail.setText(emailUsuario);

        if(nomeUsuarioDb != null && !nomeUsuarioDb.isEmpty())
        {
            etNome.setText(nomeUsuarioDb);
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeUsuario = etNome.getText().toString();

                if (nomeUsuario.isEmpty()) {
                    Toast.makeText(TelaCadastro.this, R.string.etNome_vazio, Toast.LENGTH_LONG).show();
                } else {
                    Usuario user = new Usuario();
                    user.setId(id);
                    user.setEmail(emailUsuario);
                    user.setNome(nomeUsuario);

                    UsuarioDAO.editar(TelaCadastro.this, user);

                    Toast.makeText(TelaCadastro.this, R.string.usuarioAtualizado, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TelaCadastro.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}