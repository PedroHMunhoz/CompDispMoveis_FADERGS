package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class RelatorioActivity extends AppCompatActivity {

    private Button btnGerarRelatorio;
    private EditText txtDataInicial, txtDataFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        txtDataInicial = findViewById(R.id.txtDataInicial);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        btnGerarRelatorio = findViewById(R.id.btnGerarRelatorio);

        btnGerarRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidarDatas())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(RelatorioActivity.this);
                    alert.setTitle(R.string.datas_invalidas_titulo);
                    alert.setCancelable(false);
                    alert.setMessage(R.string.datas_invalidas_relatorios);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.setNeutralButton("OK", null);
                    alert.show();
                }
                else
                {
                   //ToDo: Implementar as regras para gerar o relatório e msotrar no listview
                }
            }
        });
    }

    private Boolean ValidarDatas(){
        String dtInicial = txtDataInicial.getText().toString();
        String dtFinal = txtDataFinal.getText().toString();

        String dateFormat = "dd/MM/uuuu";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalDate date = LocalDate.parse(dtInicial, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        try {
            LocalDate date = LocalDate.parse(dtFinal, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }
}
