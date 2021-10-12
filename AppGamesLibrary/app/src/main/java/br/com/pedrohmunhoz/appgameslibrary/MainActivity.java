package br.com.pedrohmunhoz.appgameslibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
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

        // Set the reference to elements of the activity
        fabParent = findViewById(R.id.fabParent);
        fabConsoles = findViewById(R.id.fabConsoles);
        fabGames = findViewById(R.id.fabGames);
        lvwConsoles = findViewById(R.id.lvwConsoles);
        lvwGames = findViewById(R.id.lvwGames);

        // Set the click listener to the parent FAB
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

        // Set the click listener to the Games list FAB
        fabGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabs();
                Intent intent = new Intent(MainActivity.this, GamesListActivity.class);
                intent.putExtra("game_id", 0);
                startActivity(intent);
            }
        });

        // Set the click listener to the Consoles list FAB
        fabConsoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabs();
                Intent intent = new Intent(MainActivity.this, ConsolesListActivity.class);
                intent.putExtra("console_id", 0);
                startActivity(intent);
            }
        });

        // Load all the consoles from database
        LoadConsoles();

        // Load all the games from database
        LoadGames();

        // Set the click listener of the Consoles listview, to open the activity in Edit mode
        lvwConsoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int consoleID = lstConsoles.get(position).getId();
                Intent intent = new Intent(MainActivity.this, ConsolesActivity.class);
                intent.putExtra("console_id", consoleID);
                startActivity(intent);
            }
        });

        // Set the long click listener, used to delete consoles
        lvwConsoles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteConsole(position);
                return true;
            }
        });

        // Set the click listener of the Games listview, to open the activity in Edit mode
        lvwGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int gameID = lstGames.get(position).getId();
                Intent intent = new Intent(MainActivity.this, GamesActivity.class);
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

    // Open all the children FABs when clicking in the parent FAB, with animation
    private void openFabs() {
        fabConsoles.show();
        fabGames.show();
        fabConsoles.animate().translationY(-(fabParent.getCustomSize()));
        fabGames.animate().translationY(-(fabConsoles.getCustomSize() + fabParent.getCustomSize()));
        fabParent.setImageResource(R.drawable.gamepad_up);
        isFabsClosed = false;
    }

    // Close all the children FABs when clicking in the parent FAB, with animation
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

    // Fetch all the consoles from database and populate the list of consoles
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

    // Fetch all the games from database and populate the list of consoles
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

    // This method will be called when returning to the activity, to reload the list if we add
    // new items on database
    @Override
    protected void onRestart() {
        super.onRestart();
        LoadConsoles();
        LoadGames();
    }

    private void DeleteConsole(int position) {
        // Get the console from the Consoles list based on the clicked item position
        final Console console = lstConsoles.get(position);

        // Build the Alert dialog object
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        // Set alert dialog title
        alert.setTitle(R.string.delete_console_alert_title);

        // Set alert dialog icon
        alert.setIcon(android.R.drawable.ic_delete);

        // Set alert dialog message
        alert.setMessage(getString(R.string.console_delete_confirmation_message) + " " + console.getName() + "?");

        // Set alert dialog neutral/cancel button
        alert.setNeutralButton(R.string.delete_console_alert_cancel_button_text, null);

        // Set alert dialog confirmation button message and action
        alert.setPositiveButton(R.string.delete_console_alert_confirm_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Check if console has any games linked, because if it has, we can't delete it
                if (ConsoleDAO.ConsoleHasGames(MainActivity.this, console.getId())) {
                    // Build and show to the user the warning message that the Console has games linked to it
                    AlertDialog.Builder alertConsoleLink = new AlertDialog.Builder(MainActivity.this);
                    alertConsoleLink.setTitle(R.string.console_has_links_alert_message_title);
                    alertConsoleLink.setMessage(console.getName() + " " + getString(R.string.console_has_links_alert_message));
                    alertConsoleLink.setIcon(android.R.drawable.ic_dialog_alert);
                    alertConsoleLink.setNeutralButton("Ok", null);
                    alertConsoleLink.show();
                    return;
                }

                // If the console doesn't have any games linked, delete it
                ConsoleDAO.Delete(MainActivity.this, console.getId());

                // Reload the consoles listview to refresh it
                LoadConsoles();
            }
        });

        // Show the confirmation alert dialog to the user
        alert.show();
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
                GameDAO.Delete(MainActivity.this, game.getId());

                // Reload the games listview to refresh it
                LoadGames();
            }
        });

        // Show the confirmation alert dialog to the user
        alert.show();
    }
}
