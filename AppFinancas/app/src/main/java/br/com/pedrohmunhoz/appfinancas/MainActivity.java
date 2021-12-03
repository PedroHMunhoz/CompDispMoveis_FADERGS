package br.com.pedrohmunhoz.appfinancas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabParent, fabAddLancamento, fabRelatorio, fabPerfil, fabSair;
    private TextView lblAddLancamento, lblRelatorio, lblPerfil, lblSair;
    private Boolean isAllFabsVisible;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabParent = findViewById(R.id.fabParent);
        fabAddLancamento = findViewById(R.id.fabAddLancamento);
        fabRelatorio = findViewById(R.id.fabRelatorio);
        fabPerfil = findViewById(R.id.fabPerfil);
        fabSair = findViewById(R.id.fabSair);
        lblAddLancamento = findViewById(R.id.lblAddLancamento);
        lblRelatorio = findViewById(R.id.lblRelatorio);
        lblPerfil = findViewById(R.id.lblPerfil);
        lblSair = findViewById(R.id.lblSair);

        fabAddLancamento.setVisibility(View.GONE);
        fabRelatorio.setVisibility(View.GONE);
        fabPerfil.setVisibility(View.GONE);
        fabSair.setVisibility(View.GONE);
        lblAddLancamento.setVisibility(View.GONE);
        lblRelatorio.setVisibility(View.GONE);
        lblPerfil.setVisibility(View.GONE);
        lblSair.setVisibility(View.GONE);
        isAllFabsVisible = false;

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Tenta pegar o usuário autenticado
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // Se não tiver ninguém logado, fecha a activity
                if (user == null) {
                    finish();
                }
            }
        };

        auth.addAuthStateListener(authStateListener);

        fabParent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            fabAddLancamento.show();
                            lblAddLancamento.setVisibility(View.VISIBLE);
                            fabRelatorio.show();
                            lblRelatorio.setVisibility(View.VISIBLE);
                            fabPerfil.show();
                            lblPerfil.setVisibility(View.VISIBLE);
                            fabSair.show();
                            lblSair.setVisibility(View.VISIBLE);
                            isAllFabsVisible = true;
                        } else {
                            fabAddLancamento.hide();
                            lblAddLancamento.setVisibility(View.GONE);
                            fabRelatorio.hide();
                            lblRelatorio.setVisibility(View.GONE);
                            fabPerfil.hide();
                            lblPerfil.setVisibility(View.GONE);
                            fabSair.hide();
                            lblSair.setVisibility(View.GONE);
                            isAllFabsVisible = false;
                        }
                    }
                });

        fabAddLancamento.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Adicionar Lançamento", Toast.LENGTH_SHORT).show();
                    }
                });

        fabRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Relatório", Toast.LENGTH_SHORT).show();
            }
        });

        fabSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });
    }
}
