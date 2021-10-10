package br.com.pedrohmunhoz.appswipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txtDirecao;
    private ConstraintLayout fundoDaTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDirecao = findViewById(R.id.txtDirecao);
        fundoDaTela = findViewById(R.id.fundoDaTela);

        fundoDaTela.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeTop() {
                super.onSwipeTop();

                txtDirecao.setText("Para cima");
                fundoDaTela.setBackgroundColor(Color.BLUE);
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();

                txtDirecao.setText("Para baixo");
                fundoDaTela.setBackgroundColor(Color.RED);
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                txtDirecao.setText("Para esquerda");
                fundoDaTela.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                txtDirecao.setText("Para direita");
                fundoDaTela.setBackgroundColor(Color.YELLOW);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtDirecao.setText(getResources().getString(R.string.txtDirecao));
                fundoDaTela.setBackgroundColor(getResources().getColor(R.color.corDoFundoDaTela));

                return super.onTouch(v, event);
            }
        });
    }
}
