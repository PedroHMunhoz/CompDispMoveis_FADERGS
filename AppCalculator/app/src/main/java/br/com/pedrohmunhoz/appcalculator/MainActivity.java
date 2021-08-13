package br.com.pedrohmunhoz.appcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // Criação das variáveis privadas para ler os elementos da tela que vamos manipular
    private EditText txtValorInformado;
    private Button btnMultiplicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa as variáveis locais com a referência pro elemento da tela, pelo seu ID
        txtValorInformado = findViewById(R.id.txtValorInformado);
        btnMultiplicar = findViewById(R.id.btnMultiplicar);

        /* Adiciona um listener de click no botão de Multiplicar, para que quando o usuário clicar
        ele irá chamar o método onClick. Dentro desse método nós chamamos o nosso método calcular()
        que vai executar as regras conforme implementado nele
        */
        btnMultiplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcular();
            }
        });
    }

    // Método que fará o cálculo do valor e vai exibir pro usuário na tela o resultado
    private void calcular(){
        // Pegamos o texto que foi digitado no campo em formato string
        String textoDigitado = txtValorInformado.getText().toString();

        // Convertemos o valor lido de string para double
        double valorDigitado = Double.valueOf(textoDigitado);

        // Variável para armazenar o resultado, multiplicando o valor digitado por 2
        double resultado = valorDigitado * 2;

        // Instancia um novo objeto da classe AlertDialog pra mostrar um alerta na tela
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        // Seta o título do alerta
        alerta.setTitle("Resultado");

        // Seta a mensagem do corpo do alerta, convertendo já o resultado double para string
        // pois o método setMessage espera uma string e não um double
        alerta.setMessage(String.valueOf(resultado));

        // Configura o botão que irá tratar a ação de positivo (por exemplo, um SIM) do alerta
        // Aqui o parâmetro Listener está NULL pois não vamos atribuir nenhum listener a esse botão
        alerta.setPositiveButton("OK", null);

        // Mostra o alerta na tela
        alerta.show();
    }
}
