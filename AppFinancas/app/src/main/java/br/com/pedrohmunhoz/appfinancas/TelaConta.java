package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class TelaConta extends AppCompatActivity {

    Button butSalvar;
    EditText etConta;
    EditText etSaldoInicial;
    private EditText edNomeBanco;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_conta);

        butSalvar = findViewById(R.id.butSalvar);
        etConta = findViewById(R.id.etConta);
        etSaldoInicial = findViewById(R.id.etSaldoInicial);
        edNomeBanco = findViewById(R.id.edNomeBanco);

        auth = FirebaseAuth.getInstance();

        butSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarConta();
            }
        });
    }

    private void salvarConta() {
        if (validaCampos()) {
            String userIdFirebase = auth.getCurrentUser().getUid();
            String nomebanco = edNomeBanco.getText().toString();
            int numero_conta = Integer.parseInt(etConta.getText().toString());
            double saldo_inicial = Double.parseDouble(etSaldoInicial.getText().toString());
            double saldo_atual = saldo_inicial;

            ContaBancaria conta = new ContaBancaria();
            conta.setUsuario_id(userIdFirebase);
            conta.setNome_banco(nomebanco);
            conta.setNumero_conta(numero_conta);
            conta.setSaldo_inicial(saldo_inicial);
            conta.setSaldo_atual(saldo_atual);

            ContaBancariaDAO.inserir(TelaConta.this, conta);

            Toast.makeText(TelaConta.this, R.string.conta_bancaria_cadastrada_sucesso, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean validaCampos() {
        String nomebanco = edNomeBanco.getText().toString();
        String numeroContastr = etConta.getText().toString();
        String saldoInicialStr = etSaldoInicial.getText().toString();

        if (nomebanco.isEmpty()) {
            Toast.makeText(TelaConta.this, R.string.erro_nome_banco_vazio, Toast.LENGTH_LONG).show();
            return false;
        }

        if (numeroContastr.isEmpty()) {
            Toast.makeText(TelaConta.this, R.string.erro_numero_conta_vazio, Toast.LENGTH_LONG).show();
            return false;
        }

        if (saldoInicialStr.isEmpty()) {
            Toast.makeText(TelaConta.this, R.string.erro_saldo_inicial_vazio, Toast.LENGTH_LONG).show();
            return false;
        }

        int numeroConta = Integer.parseInt(numeroContastr);
        double saldoInicial = Double.parseDouble(saldoInicialStr);

        if (numeroConta == 0) {
            Toast.makeText(TelaConta.this, R.string.erro_numero_conta_invalido, Toast.LENGTH_LONG).show();
            return false;
        }

        if (saldoInicial == 0) {
            Toast.makeText(TelaConta.this, R.string.erro_saldo_inicial_invalido, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}