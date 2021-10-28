package br.com.pedrohmunhoz.apppersistfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormularioActivity extends AppCompatActivity {

    private EditText txtNomeProduto;
    private Spinner cboCategorias;
    private Button btnSalvar;
    private String acao;
    private Produto produto;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        // Pegamos uma instância do Database do Firebase
        database = FirebaseDatabase.getInstance();

        // Passamos a referência do banco completo para o objeto reference
        reference = database.getReference();

        // Pegamos a referência do elemento da tela para o nosso objeto para manipular
        txtNomeProduto = findViewById(R.id.txtNomeProduto);
        cboCategorias = findViewById(R.id.cboCategorias);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Pegamos a ação selecionada dos "extras" da intent que está chamando a tela
        acao = getIntent().getStringExtra("acao");

        // Se a ação for de editar, vai carregar o formulário e o produto com o ID passado como parâmetro
        if (acao.equals("editar")) {
            carregarFormulario();
        }

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
            /** Instanciamos um novo produto para popular as propriedades dele, se for ação de inserir.
             * Se for editar, o método carregarFormulario já populou nosso objeto local "produto"
             * com todos os dados, inclusive o ID que precisamos para o update, por isso não podemos
             * instanciar um novo objeto.
             * */
            if (acao.equals("inserir")) {
                produto = new Produto();
            }

            // Seta o nome
            produto.setNome(nomeProduto);

            // Seta a categoria, pegando o texto que está na opção selecionada do Spinner
            produto.setCategoria(cboCategorias.getSelectedItem().toString());

            if (acao.equals("inserir")) {
                // Enviamos pro Firebase, no nó "produtos" o nosso objeto
                reference.child("produtos").push().setValue(produto);

                // Limpa o campo do nome do produto, para digitar um novo
                txtNomeProduto.setText("");

                // Seta o combo de Categorias para a primeira opção, com animação habilitada
                cboCategorias.setSelection(0, true);

                // Mostra mensagem de sucesso
                Toast.makeText(this, "Produto cadastro com sucesso!", Toast.LENGTH_LONG).show();
            } else {
                // Pegamos a referência do produto no firebase, pela hash gerada no ID e atualizamos os dados
                reference.child("produtos").child(produto.getId()).setValue(produto);

                // Mostra mensagem de sucesso
                Toast.makeText(this, "Produto atualizado com sucesso!", Toast.LENGTH_LONG).show();

                // Fecha a activity atual e volta para a anteriro
                finish();
            }
        }
    }

    private void carregarFormulario() {
        // Pegamos dos extras da Intent o idProduto que foi passado
        String id = getIntent().getStringExtra("idProduto");

        // Buscamos no banco de dados o produto, pelo ID passado
        //produto = ProdutoDAO.getProdutoByID(this, id);

        // Seta no textbox o nome do produto que veio do banco
        txtNomeProduto.setText(produto.getNome());

        // Buscamos o array de categorias definido no arquivo de strings
        String[] categorias = getResources().getStringArray(R.array.categorias);

        // Fazemos um loop no array, para procurar a categoria que está vinculada ao nosso produto
        // Iniciamos do indice 1 pois o 0 é apenas um placeholder, não pode ser salvo no banco
        for (int i = 1; i < categorias.length; i++) {
            // Se o nome da categoria for encontrado no array, setamos aquela posição como
            // selecionada no spinner
            if (produto.getCategoria().equals(categorias[i])) {
                // Seta o índice selecionado no Spinner
                cboCategorias.setSelection(i);

                // Sai da execução do laço pra evitar processamento desnecessário
                break;
            }
        }
    }
}
