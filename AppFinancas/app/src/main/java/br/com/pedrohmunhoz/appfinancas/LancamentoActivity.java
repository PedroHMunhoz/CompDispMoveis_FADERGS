package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

public class LancamentoActivity extends AppCompatActivity {
    private EditText txtNomeLancamento;
    private RadioButton rdbReceita, rdbDespesa;
    private EditText txtValor;
    private EditText txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);
    }
}