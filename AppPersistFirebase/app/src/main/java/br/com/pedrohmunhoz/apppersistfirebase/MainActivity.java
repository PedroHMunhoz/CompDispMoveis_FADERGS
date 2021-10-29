package br.com.pedrohmunhoz.apppersistfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvwProdutos;
    private ArrayAdapter adapter;
    private List<Produto> lstProdutos;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Busca a referência da listview da tela e inicializa ele
        lvwProdutos = findViewById(R.id.lvwProdutos);

        // Chama o método para popular a lista e o adapter que será usado
        carregarProdutos();

        // Pega a instância do Firebase Auth vinculada
        auth = FirebaseAuth.getInstance();

        // Adiciona um listener no estado do auth do Firebase
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Tenta pegar o usuário autenticado
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // Se não tiver ninguém logado, fecha a activity
                if (user == null) {
                    finish();
                }
            }
        };

        // Adiciona o AuthStateListener no auth usado
        auth.addAuthStateListener(authStateListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Instancia uma nova intent, para navegar pra tela de cadastro
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);

                // Passamos um parâmetro para a Intent, de forma a tratar na activity de destino
                intent.putExtra("acao", "inserir");

                // Inicia a activity configurada
                startActivity(intent);
            }
        });

        // Criamos o listener para o clique de um determinado item dentro da listview
        lvwProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pegamos o ID do produto selecionado pelo usuário na lista, passando o position
                // que foi clicado e buscando dentro da lista o ID daquele produto
                String idProduto = lstProdutos.get(position).getId();

                // Instancia uma nova intent, para navegar pra tela de cadastro
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);

                // Passamos um parâmetro para a Intent, de forma a tratar na activity de destino
                intent.putExtra("acao", "editar");

                // Passamos o id do produto clicado para editar na tela de formulário
                intent.putExtra("idProduto", idProduto);

                // Inicia a activity configurada
                startActivity(intent);
            }
        });

        // Criamos o listener para o clique longo (clicar e segurar) de um determinado item dentro da listview
        lvwProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Chamamos o método excluir passando a posição do item selecionado na lista
                excluir(position);

                return true;
            }
        });
    }

    private void excluir(int posicao) {
        // Pega o produto na List<Produto> populada na tela
        final Produto produto = lstProdutos.get(posicao);

        // Instancia um alerta para pedir confirmação do usuário
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        // Seta o título do modal de alerta
        alerta.setTitle("Excluir");

        // Seta o ícone do modal de alerta
        alerta.setIcon(android.R.drawable.ic_delete);

        // Seta a mensagem de confirmação
        alerta.setMessage("Confirma exclusão do produto " + produto.getNome() + "?");

        // Seta o botão neutro, sem ação, pra não executar nada caso seja clicado
        alerta.setNeutralButton("Cancelar", null);

        // Seta o positive button e cria o listener caso ele seja clicado
        alerta.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ProdutoDAO.excluir(MainActivity.this, produto.getId());

                // Chamamos o método carregarProdutos novamente para atualizar a listagem em tela
                carregarProdutos();
            }
        });

        // Mostra o alert na tela pro usuário
        alerta.show();
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
        //lstProdutos = ProdutoDAO.getProdutos(this);

        lstProdutos = new ArrayList<>();

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
        if (id == R.id.mnuAdicionarProduto) {
            Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
            intent.putExtra("acao", "inserir");
            startActivity(intent);
        }

        if (id == R.id.mnuSair) {
            auth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }
}
