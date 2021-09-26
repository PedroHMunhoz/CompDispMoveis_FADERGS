package br.com.pedrohmunhoz.apppersist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvwProdutos;
    private ArrayAdapter adapter;
    private List<Produto> lstProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Busca a referência da listview da tela e inicializa ele
        lvwProdutos = findViewById(R.id.lvwProdutos);

        // Chama o método para popular a lista e o adapter que será usado
        carregarProdutos();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instancia uma nova intent, para navegar pra tela de cadastro
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);

                // Inicia a activity configurada
                startActivity(intent);
            }
        });
    }

    // Aqui fazemos override do método onRestart para que quando o usuário ir para a tela de
    // cadastro e voltar, a lista seja atualizada novamente, e não somente ao abrir o programa
    @Override
    protected void onRestart() {
        super.onRestart();

        // Chama o método para popular a lista e o adapter que será usado
        carregarProdutos();
    }

    private void carregarProdutos() {
        // Usando a classe ProdutoDAO, chamamos o método que vai buscar todos os produtos no banco
        lstProdutos = ProdutoDAO.getProdutos(this);

        // Se a lista não tiver nada, vamos criar um produto "fake" para mostrar na tela pro usuário
        if (lstProdutos.size() == 0) {
            // Produto placeholder, apenas para mostrar caso a lista venha vazia
            Produto fake = new Produto("Lista vazia...", "");
            lstProdutos.add(fake);

            // Desabilitamos as interações com a ListView, para que ela fique similar a uma label apenas
            lvwProdutos.setEnabled(false);
        } else {
            // Habilitamos a interação com a ListView, caso a lista tenha produtos vindos do banco
            lvwProdutos.setEnabled(true);
        }

        /* O adapter recebe 3 parâmetros, sendo eles:
         * Parâmetro 1: o contexto onde o mesmo será usado, nesse caso o "this" referencia a activity atual
         * Parâmetro 2: o layout dos itens da lista
         * Parâmetro 3: a lista de itens a serem exibidos
         * * */
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstProdutos);

        // Seta o adapter na listview, para renderizar
        lvwProdutos.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
