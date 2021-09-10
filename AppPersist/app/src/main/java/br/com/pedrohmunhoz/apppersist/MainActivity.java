package br.com.pedrohmunhoz.apppersist;

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
            }
        });
    }

    private void carregarProdutos() {
        lstProdutos = new ArrayList<>();

        Produto produto = new Produto("Coca Cola 1.5L", "Bebidas");
        lstProdutos.add(produto);

        produto = new Produto("Pepsi 2L", "Bebidas");
        lstProdutos.add(produto);

        produto = new Produto("Trakinas", "Alimentos");
        lstProdutos.add(produto);

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
