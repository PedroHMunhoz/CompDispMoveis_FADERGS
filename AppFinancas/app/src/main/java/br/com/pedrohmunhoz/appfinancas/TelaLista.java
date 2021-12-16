package br.com.pedrohmunhoz.appfinancas;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TelaLista extends AppCompatActivity {

    private ListView lvwContas;
    private FirebaseAuth auth;
    private List<ContaBancaria> lstContas;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_lista);
        Toolbar toolbar = findViewById(R.id.toolbarTelaLista);
        toolbar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        lvwContas = findViewById(R.id.lvwContas);

        carregarContas();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaLista.this, TelaConta.class);
                intent.putExtra("acao", "inserir");
                startActivity(intent);
            }
        });

        lvwContas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Excluir(position);
                return true;
            }
        });

        lvwContas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idConta = lstContas.get(position).getId();

                Intent intent = new Intent(TelaLista.this, TelaConta.class);
                intent.putExtra("acao", "editar");
                intent.putExtra("conta_id", idConta);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        carregarContas();
    }

    private void carregarContas() {
        String userIdFirebase = auth.getCurrentUser().getUid();
        lstContas = ContaBancariaDAO.getContasByUser(TelaLista.this, userIdFirebase);

        if (lstContas.size() == 0) {
            ContaBancaria fake = new ContaBancaria("Seu usuário não possui contas cadastradas ainda.");
            lstContas.add(fake);

            lvwContas.setEnabled(false);
        } else {
            lvwContas.setEnabled(true);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstContas);

        lvwContas.setAdapter(adapter);
    }


}