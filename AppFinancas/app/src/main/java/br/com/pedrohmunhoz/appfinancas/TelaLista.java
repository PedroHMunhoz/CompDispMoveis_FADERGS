package br.com.pedrohmunhoz.appfinancas;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import br.com.pedrohmunhoz.appfinancas.databinding.ActivityTelaListaBinding;

public class TelaLista extends AppCompatActivity {

 //   private AppBarConfiguration appBarConfiguration;
//private ActivityTelaListaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     setContentView(R.layout.activity_tela_lista);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}