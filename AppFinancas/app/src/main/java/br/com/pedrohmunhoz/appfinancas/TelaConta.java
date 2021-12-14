package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class TelaConta extends AppCompatActivity {

   Button butSalvar;
   EditText etConta;
   EditText etSaldoInicial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_conta);

        butSalvar = findViewById(R.id.butSalvar);
        etConta = findViewById(R.id.etConta);
        etSaldoInicial = findViewById(R.id.etSaldoInicial);

    }
}