package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {

    private Button btnGerarRelatorio;
    private EditText txtDataInicial, txtDataFinal;
    private ListView lvwLancamentos;
    private ArrayAdapter adapter;
    private List<Lancamento> lstLancamentos;
    private FirebaseAuth auth;
    private DatePicker dtpDataInicial, dtpDataFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        txtDataInicial = findViewById(R.id.txtDataInicial);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        btnGerarRelatorio = findViewById(R.id.btnGerarRelatorio);
        lvwLancamentos = findViewById(R.id.lvwLancamentos);
        dtpDataInicial = findViewById(R.id.dtpDataInicial);
        dtpDataFinal = findViewById(R.id.dtpDataFinal);

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

        lvwLancamentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Excluir(position);
                return true;
            }
        });

        txtDataInicial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtDataInicial.getWindowToken(), 0);
                dtpDataInicial.setVisibility(View.VISIBLE);
                dtpDataFinal.setVisibility(View.GONE);
                return true;
            }
        });

        txtDataFinal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtDataFinal.getWindowToken(), 0);
                dtpDataFinal.setVisibility(View.VISIBLE);
                dtpDataInicial.setVisibility(View.GONE);
                return true;
            }
        });

        dtpDataInicial.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dataLancamento = sdf.format(calendar.getTime());
                txtDataInicial.setText(dataLancamento);
                dtpDataInicial.setVisibility(View.GONE);
            }
        });

        dtpDataFinal.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dataLancamento = sdf.format(calendar.getTime());
                txtDataFinal.setText(dataLancamento);
                dtpDataFinal.setVisibility(View.GONE);
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

    private void Excluir(int posicao){
        final Lancamento lanc = lstLancamentos.get(posicao);

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(R.string.alerta_excluir_titulo);
        alerta.setIcon(android.R.drawable.ic_delete);
        alerta.setMessage(getString(R.string.alerta_excluir_mensagem) + " " + lanc.getDescricao() + "?");
        alerta.setNeutralButton(R.string.alerta_excluir_botao_cancelar, null);
        alerta.setPositiveButton(R.string.alerta_excluir_botao_sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LancamentoDAO.excluir(RelatorioActivity.this, lanc.getId());
                CarregarLancamentos();
            }
        });

        alerta.show();
    }
}
