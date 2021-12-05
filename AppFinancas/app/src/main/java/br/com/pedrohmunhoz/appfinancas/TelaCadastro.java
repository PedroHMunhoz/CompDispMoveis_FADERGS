package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class TelaCadastro extends AppCompatActivity {

    Button btnSalvar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

      btnSalvar = findViewById(R.id.btnSalvar);
      btnSalvar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             Toast.makeText(getBaseContext(),"Salvar", Toast.LENGTH_LONG).show();
             finish();
          }
      });

    }
}