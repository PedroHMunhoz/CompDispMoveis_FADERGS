package br.com.pedrohmunhoz.appfinancas;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabParent, fabAddLancamento, fabRelatorio;
    TextView lblAddLancamento, lblRelatorio;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabParent = findViewById(R.id.fabParent);
        fabAddLancamento = findViewById(R.id.fabAddLancamento);
        fabRelatorio = findViewById(R.id.fabRelatorio);
        lblAddLancamento = findViewById(R.id.lblAddLancamento);
        lblRelatorio = findViewById(R.id.lblRelatorio);

        fabAddLancamento.setVisibility(View.GONE);
        fabRelatorio.setVisibility(View.GONE);
        lblAddLancamento.setVisibility(View.GONE);
        lblRelatorio.setVisibility(View.GONE);
        isAllFabsVisible = false;

        fabParent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            fabAddLancamento.show();
                            lblAddLancamento.setVisibility(View.VISIBLE);
                            fabRelatorio.show();
                            lblRelatorio.setVisibility(View.VISIBLE);
                            isAllFabsVisible = true;
                        } else {
                            fabAddLancamento.hide();
                            lblAddLancamento.setVisibility(View.GONE);
                            fabRelatorio.hide();
                            lblRelatorio.setVisibility(View.GONE);
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
    }
}
