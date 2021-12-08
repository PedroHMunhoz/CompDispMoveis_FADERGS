package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {

    private Button btnGerarRelatorio;
    private EditText txtDataInicial, txtDataFinal;
    private ListView lvwLancamentos;
    private ArrayAdapter adapter;
    private List<Lancamento> lstLancamentos;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        txtDataInicial = findViewById(R.id.txtDataInicial);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        btnGerarRelatorio = findViewById(R.id.btnGerarRelatorio);
        lvwLancamentos = findViewById(R.id.lvwLancamentos);

        auth = FirebaseAuth.getInstance();

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
                    CarregarLancamentos();
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

    private void CarregarLancamentos(){
        String userIdFirebase = auth.getCurrentUser().getUid();
        String dateFormat = "dd/MM/uuuu";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);

        String dtInicial = txtDataInicial.getText().toString();
        String dtFinal = txtDataFinal.getText().toString();
        LocalDate dateInicial = LocalDate.parse(dtInicial, dateTimeFormatter);
        LocalDate dateFinal = LocalDate.parse(dtFinal, dateTimeFormatter);

        lstLancamentos = LancamentoDAO.getLancamentosByUser(this, userIdFirebase, dateInicial,dateFinal);

        if (lstLancamentos.size() == 0) {
            Lancamento fake = new Lancamento( "Não existem lançamentos ainda!", this);
            lstLancamentos.add(fake);

            lvwLancamentos.setEnabled(false);
        } else {
            lvwLancamentos.setEnabled(true);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstLancamentos);

        lvwLancamentos.setAdapter(adapter);
    }
}
