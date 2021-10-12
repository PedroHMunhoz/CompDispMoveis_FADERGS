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

public class ConsolesListActivity extends AppCompatActivity {

    private ListView lvwConsoles;
    private ArrayAdapter adapter;
    private List<Console> lstConsoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consoles_list);

        // Set the reference to the Consoles Listview
        lvwConsoles = findViewById(R.id.lvwConsoles);

        // Call the method to list the consoles
        LoadConsoles();

        // Set the reference to the New Console Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fabNewConsole);

        // Set the Click event to open the Console Activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsolesListActivity.this, ConsolesActivity.class);
                startActivity(intent);
            }
        });

        // Set the Click listener on the Listview Item to open the Console Activity, in edit mode
        lvwConsoles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int consoleID = lstConsoles.get(position).getId();
                Intent intent = new Intent(ConsolesListActivity.this, ConsolesActivity.class);
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
    }

    private void LoadConsoles() {
        // Fetch all consoles from database
        lstConsoles = ConsoleDAO.getConsoles(this);

        // If there are no records in the database yet, adds a fake one to don't let the screen blank
        // and disable the Listview to avoid users click
        if (lstConsoles.size() == 0) {
            Console fake = new Console(getString(R.string.empty_list_placeholder));
            lstConsoles.add(fake);
            lvwConsoles.setEnabled(false);
        } else {
            // Enable the listview to listen to user clicks
            lvwConsoles.setEnabled(true);
        }

        // Set the adapter with the itens stored in lstConsoles variable
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lstConsoles);

        // Set the adapter to the listview
        lvwConsoles.setAdapter(adapter);
    }

    // This method will be called when returning to the activity, to reload the list if we add a
    // new item on database
    @Override
    protected void onRestart() {
        super.onRestart();
        LoadConsoles();
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
                if (ConsoleDAO.ConsoleHasGames(ConsolesListActivity.this, console.getId())) {
                    // Build and show to the user the warning message that the Console has games linked to it
                    AlertDialog.Builder alertConsoleLink = new AlertDialog.Builder(ConsolesListActivity.this);
                    alertConsoleLink.setTitle(R.string.console_has_links_alert_message_title);
                    alertConsoleLink.setMessage(console.getName() + " " + getString(R.string.console_has_links_alert_message));
                    alertConsoleLink.setIcon(android.R.drawable.ic_dialog_alert);
                    alertConsoleLink.setNeutralButton("Ok", null);
                    alertConsoleLink.show();
                    return;
                }

                // If the console doesn't have any games linked, delete it
                ConsoleDAO.Delete(ConsolesListActivity.this, console.getId());

                // Reload the consoles listview to refresh it
                LoadConsoles();
            }
        });

        // Show the confirmation alert dialog to the user
        alert.show();
    }
}
