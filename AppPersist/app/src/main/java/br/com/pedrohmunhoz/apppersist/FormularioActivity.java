package br.com.pedrohmunhoz.apppersist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FormularioActivity extends AppCompatActivity {

    private EditText txtNomeProduto;
    private Spinner cboCategorias;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        // Pegamos a referência do elemento da tela para o nosso objeto para manipular
        txtNomeProduto = findViewById(R.id.txtNomeProduto);
        cboCategorias = findViewById(R.id.cboCategorias);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Criamos um clickListener para disparar quando o botão Salvar for clicado
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ao clicar, ele irá chamar o método Salvar que implementa a regra
                salvar();
            }
        });
    }

    private void salvar() {
        // Lemos do campo da tela o valor digitado pelo usuário
        String nomeProduto = txtNomeProduto.getText().toString();

        /** Se o nome estiver vazio ou a opção selecionada for 0, que é o "Selecione a categoria"
         * vamos dar um Toast pro usuário preencher os dados completamente. O último parâmetro
         * do Toast é uma constante de duração, sendo ele LONG ou SHORT.
         */
        if (nomeProduto.isEmpty() || cboCategorias.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Você deve preencher todos os campos", Toast.LENGTH_LONG).show();
        } else {
            // Instanciamos um novo produto para popular as propriedades dele
            Produto produto = new Produto();

            // Seta o nome
            produto.setNome(nomeProduto);

            // Seta a categoria, pegando o texto que está na opção selecionada do Spinner
            produto.setCategoria(cboCategorias.getSelectedItem().toString());

            // Chama a classe de DAO passando o contexto e o produto a ser inserido no banco
            ProdutoDAO.inserir(this, produto);

            // Limpa o campo do nome do produto, para digitar um novo
            txtNomeProduto.setText("");

            // Seta o combo de Categorias para a primeira opção, com animação habilitada
            cboCategorias.setSelection(0, true);

            Toast.makeText(this, "Produto cadastro com sucesso!", Toast.LENGTH_LONG).show();
        }
    }
}
