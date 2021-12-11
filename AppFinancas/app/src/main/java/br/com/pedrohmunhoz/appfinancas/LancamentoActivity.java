package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;

public class LancamentoActivity extends AppCompatActivity {
    private EditText txtDescricaoLancamento;
    private RadioButton rdbReceita, rdbDespesa;
    private EditText txtValor, txtData;
    private DatePicker dtpData;
    private Button btnLancar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);

        txtDescricaoLancamento = findViewById(R.id.txtDescricaoLancamento);
        rdbReceita = findViewById(R.id.rdbReceita);
        rdbDespesa = findViewById(R.id.rdbDespesa);
        txtValor = findViewById(R.id.txtValor);
        dtpData = findViewById(R.id.dtpData);
        txtData = findViewById(R.id.txtData);
        btnLancar = findViewById(R.id.btnLancar);

        auth = FirebaseAuth.getInstance();

        btnLancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidarDados()) {
                    gravarLancamento();
                }
            }
        });

        txtData.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtData.getWindowToken(), 0);
                dtpData.setVisibility(View.VISIBLE);
                return true;
            }
        });

        dtpData.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dataLancamento = sdf.format(calendar.getTime());
                txtData.setText(dataLancamento);
                dtpData.setVisibility(View.GONE);
            }
        });
    }

    private boolean ValidarDados() {
        String descricao = txtDescricaoLancamento.getText().toString();
        int tipoLancamento = (rdbReceita.isChecked() ? 1 : 2);
        String valorDigitado = txtValor.getText().toString();
        String dataLancamento = txtData.getText().toString();

        String dateFormat = "dd/MM/uuuu";

        if (descricao.equals("")) {
            Toast.makeText(LancamentoActivity.this, R.string.erro_descricao_lancamento, Toast.LENGTH_LONG).show();
            return false;
        }

        if (tipoLancamento != 1 && tipoLancamento != 2) {
            Toast.makeText(LancamentoActivity.this, R.string.erro_tipo_lancamento, Toast.LENGTH_LONG).show();
            return false;
        }

        if (valorDigitado.equals("")) {
            Toast.makeText(LancamentoActivity.this, R.string.erro_valor_lancamento_vazio, Toast.LENGTH_LONG).show();
            return false;
        }

        double valor = Double.parseDouble(valorDigitado);

        if (valor <= 0) {
            Toast.makeText(LancamentoActivity.this, R.string.erro_valor_negativo_ou_zero, Toast.LENGTH_LONG).show();
            return false;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalDate date = LocalDate.parse(dataLancamento, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            Toast.makeText(LancamentoActivity.this, R.string.erro_data_invalida_lancamento, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void gravarLancamento() {

        try {
            String descricao = txtDescricaoLancamento.getText().toString();
            int tipoLancamento = (rdbReceita.isChecked() ? 1 : 2);
            double valor = Double.parseDouble(txtValor.getText().toString());
            String dataDigitada = txtData.getText().toString();
            String dateFormat = "dd/MM/uuuu";
            String userIdFirebase = auth.getCurrentUser().getUid();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofPattern(dateFormat)
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate date = LocalDate.parse(dataDigitada, dateTimeFormatter);

            Lancamento lanc = new Lancamento(this);
            lanc.setUsuario_id(userIdFirebase);
            lanc.setDescricao(descricao);
            lanc.setTipoLancamento(tipoLancamento);
            lanc.setValor(valor);
            lanc.setData(date);

            LancamentoDAO.inserir(LancamentoActivity.this, lanc);

            Toast.makeText(LancamentoActivity.this, R.string.lancamento_efetuado_sucesso, Toast.LENGTH_LONG).show();
            finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(LancamentoActivity.this, getString(R.string.lancamento_erro) + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}