package br.com.pedrohmunhoz.appgameslibrary;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabParent, fabConsoles, fabGames;
    boolean isFabsClosed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabParent = findViewById(R.id.fabParent);
        fabConsoles = findViewById(R.id.fabConsoles);
        fabGames = findViewById(R.id.fabGames);

        fabParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabsClosed) {
                    openFabs();
                } else {
                    closeFabs();
                }
            }
        });

        fabGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabs();
                Intent intent = new Intent(MainActivity.this, GamesListActivity.class);
                intent.putExtra("game_id", 0);
                startActivity(intent);
            }
        });

        fabConsoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabs();
                Intent intent = new Intent(MainActivity.this, ConsolesListActivity.class);
                intent.putExtra("console_id", 0);
                startActivity(intent);
            }
        });
    }

    private void openFabs() {
        fabConsoles.show();
        fabGames.show();
        fabConsoles.animate().translationY(-(fabParent.getCustomSize()));
        fabGames.animate().translationY(-(fabConsoles.getCustomSize() + fabParent.getCustomSize()));
        fabParent.setImageResource(R.drawable.gamepad_up);
        isFabsClosed = false;
    }

    private void closeFabs() {
        fabConsoles.hide();
        fabGames.hide();
        fabGames.animate().translationY(0);
        fabConsoles.animate().translationY(0);
        fabParent.setImageResource(R.drawable.gamepad);
        isFabsClosed = true;
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

        return super.onOptionsItemSelected(item);
    }
}
