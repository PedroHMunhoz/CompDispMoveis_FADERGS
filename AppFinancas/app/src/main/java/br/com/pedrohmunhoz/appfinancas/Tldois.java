package br.com.pedrohmunhoz.appfinancas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Tldois extends AppCompatActivity {

    private Button btEntrap
    private Button btSaida;
    private EditText etValor;
    private EditText etData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tldois);

       btEntrap = findViewById(R.id.btEtradap);
       btSaida = findViewById(R.id.btSaida);
       etValor = findViewById(R.id.etValor);
       etData = findViewById(R.id.etData);


        }

           public void  Entradap{


    }
    public void Saida {

    }
    public  void   Valor{

    }
    public  void  Data{

    }
}