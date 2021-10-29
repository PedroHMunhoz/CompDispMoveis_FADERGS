package br.com.pedrohmunhoz.apppersistfirebase;

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

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnEntrar;
    private Button btnCadastrar;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Pega a instância do Firebase Auth vinculada
        auth = FirebaseAuth.getInstance();

        // Adiciona um listener no estado do auth do Firebase
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        // Adiciona o AuthStateListener no auth usado
        auth.addAuthStateListener(authStateListener);

        // Seta o listener para o botão de Entrar
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });

        // Seta o listener para o botão de Cadastrar
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastro();
            }
        });
    }

    private void entrar() {
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        int tamanhoSenha = senha.length();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "O e-mail e a senha devem ser preenchidos!", Toast.LENGTH_LONG).show();
        } else if (tamanhoSenha < 6) // Valida o tamanho da senha, pois tem que ter no mínimo 6 caracteres
        {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres!", Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Erro ao fazer o login. Verifique o usuário e senha digitados.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void cadastro() {
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        int tamanhoSenha = senha.length();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "O e-mail e a senha devem ser preenchidos!", Toast.LENGTH_LONG).show();
        } else if (tamanhoSenha < 6) // Valida o tamanho da senha, pois tem que ter no mínimo 6 caracteres
        {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres!", Toast.LENGTH_LONG).show();
        } else {
            // Criamos o usuário de forma assíncrona, e criamos um Listener pra quando houver o retorno
            // dessa criação assíncrona, o app ser avisado e manipularmos o retorno
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Se foi concluída com sucesso
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Erro ao cadastrar. Verifique os dados digitados.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
