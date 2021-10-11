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
        lvwConsoles = findViewById(R.id.lvwConsoles);
        LoadConsoles();

        FloatingActionButton fab = findViewById(R.id.fabNewConsole);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsolesListActivity.this, ConsolesActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    protected void onRestart() {
        super.onRestart();
        LoadConsoles();
    }
}
