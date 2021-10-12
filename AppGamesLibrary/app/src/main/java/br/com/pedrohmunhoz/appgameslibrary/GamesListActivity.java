package br.com.pedrohmunhoz.appgameslibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

        // Set the reference to the Games Listview
        lvwGames = findViewById(R.id.lvwGames);

        // Call the method to list the games
        LoadGames();

        // Set the reference to the New Game Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fabNewGame);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GamesListActivity.this, GamesActivity.class);
                startActivity(intent);
            }
        });

        // Set the Click event to open the Game Activity
        lvwGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gameID = lstGames.get(position).getId();
                Intent intent = new Intent(GamesListActivity.this, GamesActivity.class);
                intent.putExtra("game_id", gameID);
                startActivity(intent);
            }
        });

        // Set the long click listener, used to delete games
        lvwGames.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteGame(position);
                return true;
            }
        });
    }

    private void LoadGames() {
        // Fetch all games from database
        lstGames = GameDAO.getGames(this);

        // If there are no records in the database yet, adds a fake one to don't let the screen blank
        // and disable the Listview to avoid users click
        if (lstGames.size() == 0) {
            Game fake = new Game(getString(R.string.empty_list_placeholder));
            lstGames.add(fake);
            lvwGames.setEnabled(false);
        } else {
            // Enable the listview to listen to user clicks
            lvwGames.setEnabled(true);
        }

        // Set the adapter with the itens stored in lstConsoles variable
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstGames);

        // Set the adapter to the listview
        lvwGames.setAdapter(adapter);
    }

    // This method will be called when returning to the activity, to reload the list if we add a
    // new item on database
    @Override
    protected void onRestart() {
        super.onRestart();
        LoadGames();
    }

    private void DeleteGame(int position) {
        // Get the Game from the Games list based on the clicked item position
        final Game game = lstGames.get(position);

        // Build the Alert dialog object
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        // Set alert dialog title
        alert.setTitle(R.string.delete_game_alert_title);

        // Set alert dialog icon
        alert.setIcon(android.R.drawable.ic_delete);

        // Set alert dialog message
        alert.setMessage(getString(R.string.game_delete_confirmation_message) + " " + game.getName() + "?");

        // Set alert dialog neutral/cancel button
        alert.setNeutralButton(R.string.delete_game_alert_cancel_button_text, null);

        // Set alert dialog confirmation button message and action
        alert.setPositiveButton(R.string.delete_game_alert_confirm_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Delete the game from database
                GameDAO.Delete(GamesListActivity.this, game.getId());

                // Reload the games listview to refresh it
                LoadGames();
            }
        });

        // Show the confirmation alert dialog to the user
        alert.show();
    }
}
