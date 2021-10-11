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
}
