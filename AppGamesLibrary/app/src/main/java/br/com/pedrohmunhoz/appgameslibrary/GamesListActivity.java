package br.com.pedrohmunhoz.appgameslibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GamesListActivity extends AppCompatActivity {

    private ListView lvwGames;
    private ArrayAdapter adapter;
    private List<Game> lstGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        lvwGames = findViewById(R.id.lvwGames);

        LoadGames();

        FloatingActionButton fab = findViewById(R.id.fabNewGame);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamesListActivity.this, GamesActivity.class);
                startActivity(intent);
            }
        });

        lvwGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gameID = lstGames.get(position).getId();
                Intent intent = new Intent(GamesListActivity.this, GamesActivity.class);
                intent.putExtra("game_id", gameID);
                startActivity(intent);
            }
        });


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
        LoadGames();
    }
}
