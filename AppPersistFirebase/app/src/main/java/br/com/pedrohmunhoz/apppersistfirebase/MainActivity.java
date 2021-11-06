package br.com.pedrohmunhoz.apppersistfirebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Busca a referência da listview da tela e inicializa ele
        lvwProdutos = findViewById(R.id.lvwProdutos);

        lstProdutos = new ArrayList<>();

        /* O adapter recebe 3 parâmetros, sendo eles:
         * Parâmetro 1: o contexto onde o mesmo será usado, nesse caso o "this" referencia a activity atual
         * Parâmetro 2: o layout dos itens da lista
         * Parâmetro 3: a lista de itens a serem exibidos
         * * */
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstProdutos);

        // Seta o adapter na listview, para renderizar
        lvwProdutos.setAdapter(adapter);

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

                // Pegamos o produto selecionado completo da lista
                Produto prodSelecionado = lstProdutos.get(position);

                // Instancia uma nova intent, para navegar pra tela de cadastro
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);

                // Passamos um parâmetro para a Intent, de forma a tratar na activity de destino
                intent.putExtra("acao", "editar");

                // Passamos o id do produto clicado para editar na tela de formulário
                intent.putExtra("idProduto", prodSelecionado.getId());

                // Passamos o nome do produto clicado para editar na tela de formulário
                intent.putExtra("nome", prodSelecionado.getNome());

                // Passamos a categoria do produto clicado para editar na tela de formulário
                intent.putExtra("categoria", prodSelecionado.getCategoria());

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
                // Procuramos o produto a ser excluído pela chave e removemos ele do Firebase
                reference.child("produtos").child(produto.getId()).removeValue();
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
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Chama o método para popular a lista e o adapter que será usado
        carregarProdutos();
    }

    private void carregarProdutos() {

        // Limpamos a listagem antes de carregar os produtos que vierem do Firebase
        lstProdutos.clear();

        // Pegamos uma instância do Database do Firebase
        database = FirebaseDatabase.getInstance();

        // Passamos a referência do banco completo para o objeto reference
        reference = database.getReference();

        // Montas o objeto da query dos dados que queremos buscar, no caso "produtos" e ordenamos pelo nome
        query = reference.child("produtos").orderByChild("nome");

        // Instanciamos um ChildEventListener, com seus métodos para monitorar as mudanças na coleção
        childEventListener = new ChildEventListener() {
            // Acionado sempre que um novo item é adicionado
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Produto prod = new Produto();

                // Seta a chave hash gerada para o produto no Firebase na prop ID
                prod.setId(snapshot.getKey());

                // Pega o nome do produto e seta na prop Nome, convertendo pra string
                prod.setNome(snapshot.child("nome").getValue(String.class));

                // Pega a categoria do produto e seta na prop Categoria, convertendo pra string
                prod.setCategoria(snapshot.child("categoria").getValue(String.class));

                // Adiciona o produto na lista
                lstProdutos.add(prod);

                // Notifica o adapter que mudou os dados do do objeto que está vinculado a ele, atualizando
                adapter.notifyDataSetChanged();
            }

            // Acionado sempre que um item é alterado
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Pegamos a hash/chave do produto removido do Firebase
                String idProduto = snapshot.getKey();

                // Fazemos um loop na lista para procurar o produto pelo ID
                for (Produto prod:lstProdutos) {
                    // Ao encontrar o produto na lista
                    if(prod.getId().equals(idProduto)) {
                        // Setamos as props do objeto conforme o produto encontrado
                        prod.setNome(snapshot.child("nome").getValue(String.class));
                        prod.setCategoria(snapshot.child("categoria").getValue(String.class));

                        // Notificamos a alteração ao adapter
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            // Acionado sempre que um item é removido
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Pegamos a hash/chave do produto removido do Firebase
                String idProduto = snapshot.getKey();

                // Fazemos um loop na lista para procurar o produto pelo ID
                for (Produto prod:lstProdutos) {
                    // Ao encontrar o produto na lista
                    if(prod.getId().equals(idProduto)) {
                        // Removemos o produto da lista
                        lstProdutos.remove(prod);

                        // Notificamos a alteração ao adapter
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            // Acionado sempre que um item é movido
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            // Acionado sempre que um item é cancelado
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // Adicionamos o childEventListener ao nosso objeto query
        query.addChildEventListener(childEventListener);

//        // Se a lista não tiver nada, vamos criar um produto "fake" para mostrar na tela pro usuário
//        if (lstProdutos.size() == 0) {
//            // Produto placeholder, apenas para mostrar caso a lista venha vazia
//            Produto fake = new Produto("Lista vazia...", "");
//            lstProdutos.add(fake);
//
//            // Desabilitamos as interações com a ListView, para que ela fique similar a uma label apenas
//            lvwProdutos.setEnabled(false);
//        } else {
//            // Habilitamos a interação com a ListView, caso a lista tenha produtos vindos do banco
//            lvwProdutos.setEnabled(true);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Removemos o event Listener para caso não estivermos na tela ele não ficar ouvindo as alterações
        query.removeEventListener(childEventListener);
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
