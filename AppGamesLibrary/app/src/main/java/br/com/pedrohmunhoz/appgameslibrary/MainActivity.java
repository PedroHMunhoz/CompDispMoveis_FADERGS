package br.com.pedrohmunhoz.appgameslibrary;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabParent, fabConsoles, fabGames;
    boolean isFabsClosed = true;

    private ListView lvwConsoles;
    private ArrayAdapter adapter;
    private List<Console> lstConsoles;

    private ListView lvwGames;
    private ArrayAdapter adapterGames;
    private List<Game> lstGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabParent = findViewById(R.id.fabParent);
        fabConsoles = findViewById(R.id.fabConsoles);
        fabGames = findViewById(R.id.fabGames);
        lvwConsoles = findViewById(R.id.lvwConsoles);
        lvwGames = findViewById(R.id.lvwGames);

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

        LoadConsoles();
        LoadGames();

        lvwConsoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int consoleID = lstConsoles.get(position).getId();
                Intent intent = new Intent(MainActivity.this, ConsolesActivity.class);
                intent.putExtra("console_id", consoleID);
                startActivity(intent);
            }
        });

        lvwGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gameID = lstGames.get(position).getId();
                Intent intent = new Intent(MainActivity.this, GamesActivity.class);
                intent.putExtra("game_id", gameID);
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

    private void LoadConsoles() {
        lstConsoles = ConsoleDAO.getConsoles(this);

        if (lstConsoles.size() == 0) {
            Console fake = new Console(getString(R.string.empty_list_placeholder));
            lstConsoles.add(fake);
            lvwConsoles.setEnabled(false);
        } else {
            lvwConsoles.setEnabled(true);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstConsoles);

        lvwConsoles.setAdapter(adapter);
    }

    private void LoadGames() {
        lstGames = GameDAO.getGames(this);

        if (lstGames.size() == 0) {
            Game fake = new Game(getString(R.string.empty_list_placeholder));
            lstGames.add(fake);
            lvwGames.setEnabled(false);
        } else {
            lvwGames.setEnabled(true);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstGames);

        lvwGames.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoadConsoles();
        LoadGames();
    }
}
