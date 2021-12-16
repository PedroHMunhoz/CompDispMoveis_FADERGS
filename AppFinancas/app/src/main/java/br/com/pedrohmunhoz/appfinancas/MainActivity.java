package br.com.pedrohmunhoz.appfinancas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabParent, fabAddLancamento, fabRelatorio, fabPerfil, fabSair;
    private TextView lblAddLancamento, lblRelatorio, lblPerfil, lblSair, lblNomeUsuarioLogado;
    private Boolean isAllFabsVisible;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView txtValorReceitas, txtValorDespesas, txtValorTotal;
    PieChart pieChart;
    PieData pieData;
    List<PieEntry> pieEntryList = new ArrayList<>();
    private double valorTotalReceitas, valorTotalDespesas, valorTotal;

    @Override
    protected void onRestart() {
        super.onRestart();

        // Valida o usuário logado via FireBase e se o cadastro está completo
        ValidarUsuario();

        PreencherNomeUsuarioLogado();
        CalcularTotaisUsuario();
        RefreshGrafico();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        fabParent = findViewById(R.id.fabParent);
        fabAddLancamento = findViewById(R.id.fabAddLancamento);
        fabRelatorio = findViewById(R.id.fabRelatorio);
        fabPerfil = findViewById(R.id.fabPerfil);
        fabSair = findViewById(R.id.fabSair);
        lblAddLancamento = findViewById(R.id.lblAddLancamento);
        lblRelatorio = findViewById(R.id.lblRelatorio);
        lblPerfil = findViewById(R.id.lblPerfil);
        lblSair = findViewById(R.id.lblSair);
        lblNomeUsuarioLogado = findViewById(R.id.lblNomeUsuarioLogado);
        txtValorReceitas = findViewById(R.id.txtValorReceitas);
        txtValorDespesas = findViewById(R.id.txtValorDespesas);
        txtValorTotal = findViewById(R.id.txtValorTotal);

        fabAddLancamento.setVisibility(View.GONE);
        fabRelatorio.setVisibility(View.GONE);
        fabPerfil.setVisibility(View.GONE);
        fabSair.setVisibility(View.GONE);
        lblAddLancamento.setVisibility(View.GONE);
        lblRelatorio.setVisibility(View.GONE);
        lblPerfil.setVisibility(View.GONE);
        lblSair.setVisibility(View.GONE);
        isAllFabsVisible = false;

        // Configuração do gráfico de pizza
        pieChart = findViewById(R.id.graficoPizzaMain);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(false);
        Description description = pieChart.getDescription();
        description.setEnabled(false);
        description.setText("");

        auth = FirebaseAuth.getInstance();

        // Valida o usuário logado via FireBase e se o cadastro está completo
        ValidarUsuario();
        PreencherNomeUsuarioLogado();
        CalcularTotaisUsuario();
        RefreshGrafico();

        auth.addAuthStateListener(authStateListener);

        fabParent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {
                            AbrirFABs();
                        } else {
                            FecharFABs();
                        }
                    }
                });

        fabAddLancamento.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FecharFABs();
                        Intent intent = new Intent(MainActivity.this, LancamentoActivity.class);
                        startActivity(intent);
                    }
                });

        fabRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FecharFABs();
                Intent intent = new Intent(MainActivity.this, RelatorioActivity.class);
                startActivity(intent);
            }
        });

        fabSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        fabPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FecharFABs();
                AbrirTelaPerfil(true);
            }
        });
    }

    private void AbrirTelaPerfil(boolean openFromMain) {
        Intent intent = new Intent(MainActivity.this, TelaCadastro.class);
        startActivity(intent);

        // Se a activity não foi aberta pelo Main, deve matar ela pra evitar que o usuário volte pra cá de malandro
        if (!openFromMain)
            finish();
    }

    private void RefreshGrafico() {
        pieChart.clear();
        pieEntryList = new ArrayList<>();

        if (valorTotalReceitas > 0 && valorTotalDespesas > 0) {
            pieChart.setVisibility(View.VISIBLE);
            pieEntryList.add(new PieEntry((float) valorTotalReceitas, getString(R.string.titulo_receitas)));
            pieEntryList.add(new PieEntry((float) valorTotalDespesas, getString(R.string.titulo_despesas)));
            PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");

            if (valorTotalReceitas > 0 && valorTotalDespesas > 0) {
                pieDataSet.setColors(new int[]{R.color.colorReceitas, R.color.colorDespesas}, MainActivity.this);
            }

            pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
        } else {
            pieChart.setVisibility(View.GONE);
        }
    }

    private void ValidarUsuario() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Tenta pegar o usuário autenticado
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // Se não tiver ninguém logado, fecha a activity
                if (user == null) {
                    finish();
                } else {
                    // Busca o usuário logado no banco para verificar se ele já cadastrou o nome
                    Usuario userDb = UsuarioDAO.getUsuarioByID(MainActivity.this, user.getUid());

                    // Se não achar o usuário no SQLIte, fecha a activity
                    if (userDb == null) {
                        finish();
                    } else {
                        // Pega o valor da propriedade Nome do usuário cadastrado no SQLite
                        String nomeUsuario = userDb.getNome();

                        // Se estiver NULL ou vazio, mostra mensagem de alerta e direciona pra activity de perfil
                        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle(R.string.cadastro_incompleto_dialog_title);
                            dialog.setIcon(android.R.drawable.ic_dialog_alert);
                            dialog.setMessage(getString(R.string.cadastro_incompleto_dialog_message));

                            // Evita que o alerta possa ser cancelado ou fechado
                            dialog.setCancelable(false);

                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AbrirTelaPerfil(false);
                                }
                            });

                            dialog.show();
                        }
                    }
                }
            }
        };
    }

    private void AbrirFABs() {
        fabAddLancamento.show();
        lblAddLancamento.setVisibility(View.VISIBLE);
        fabRelatorio.show();
        lblRelatorio.setVisibility(View.VISIBLE);
        fabPerfil.show();
        lblPerfil.setVisibility(View.VISIBLE);
        fabSair.show();
        lblSair.setVisibility(View.VISIBLE);
        isAllFabsVisible = true;
    }

    private void FecharFABs() {
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

    private void PreencherNomeUsuarioLogado() {
        FirebaseUser user = auth.getCurrentUser();
        Usuario userDb = UsuarioDAO.getUsuarioByID(MainActivity.this, user.getUid());

        if (userDb != null) {
            String nomeUsuarioLogado = userDb.getNome();

            if (nomeUsuarioLogado != null && !nomeUsuarioLogado.isEmpty()) {
                lblNomeUsuarioLogado.setText(nomeUsuarioLogado + "!");
            }
        }
    }

    private void CalcularTotaisUsuario() {
        FirebaseUser user = auth.getCurrentUser();
        Date dt = Calendar.getInstance().getTime();

        LocalDate dataInicial = LocalDate.now().minusDays(7);
        LocalDate dataFinal = LocalDate.now();

        TotaisLancamentos totais = LancamentoDAO.GetTotaisLancamentosUltimos7DiasByUser(MainActivity.this, user.getUid(), dataInicial, dataFinal);

        Locale current = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getInstance(current);
        formatter.setMaximumFractionDigits(2);

        String receitasFormat = formatter.format(totais.valorReceitas);
        String despesasFormat = formatter.format(totais.valorDespesas);
        String totalFormat = formatter.format(totais.valorTotal);

        if (totais != null) {

            valorTotalReceitas = totais.valorReceitas;
            valorTotalDespesas = totais.valorDespesas;
            valorTotal = totais.valorTotal;

            txtValorReceitas.setText("R$ " + receitasFormat);
            txtValorDespesas.setText("R$ " + despesasFormat);
            txtValorTotal.setText("R$ " + totalFormat);
        }
    }
}
