package br.com.pedrohmunhoz.appfinancas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;

public class TelaInicial extends AppCompatActivity {
    public Button btEntrar;
    public EditText etEmail;
    public EditText etSenha;
    public Button btCadastrar;
    private Usuario user;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btEntrar = findViewById(R.id.btEntrar);
        btCadastrar = findViewById(R.id.btCadastrar);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(TelaInicial.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        auth.addAuthStateListener(authStateListener);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
    }

    private void entrar() {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        int tamanhoSenha = senha.length();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, R.string.email_password_empty_warning, Toast.LENGTH_LONG).show();
        } else if (tamanhoSenha < 6) {
            Toast.makeText(this, R.string.password_min_length_warning, Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(TelaInicial.this, R.string.login_error_message, Toast.LENGTH_LONG).show();
                    } else {
                        limparCampos();
                    }
                }
            });
        }
    }

    private void cadastrar() {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        int tamanhoSenha = senha.length();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, R.string.email_password_empty_warning, Toast.LENGTH_LONG).show();
        } else if (tamanhoSenha < 6) {
            Toast.makeText(this, R.string.password_min_length_warning, Toast.LENGTH_LONG).show();
        } else {
            user = new Usuario();

            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(TelaInicial.this, R.string.signup_error_message, Toast.LENGTH_LONG).show();
                    } else {
                        String firebaseId = task.getResult().getUser().getUid();

                        user.setEmail(email);
                        user.setId(firebaseId);

                        UsuarioDAO.inserir(TelaInicial.this, user);
                        limparCampos();
                    }
                }
            });
        }
    }

    private void limparCampos() {
        etEmail.setText("");
        etSenha.setText("");
    }
}