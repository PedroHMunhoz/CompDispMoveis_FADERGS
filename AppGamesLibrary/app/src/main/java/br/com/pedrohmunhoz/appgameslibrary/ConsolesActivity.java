package br.com.pedrohmunhoz.appgameslibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConsolesActivity extends AppCompatActivity {

    private EditText txtConsoleName;
    private Button btnSaveConsole;
    private int consoleID;
    private Console console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consoles);

        txtConsoleName = findViewById(R.id.txtConsoleName);
        btnSaveConsole = findViewById(R.id.btnSaveConsole);

        consoleID = getIntent().getIntExtra("console_id", 0);

        if (consoleID > 0) {
            LoadForm();
        }

        btnSaveConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void Save() {
        String consoleName = txtConsoleName.getText().toString();

        if (consoleName.isEmpty()) {
            Toast.makeText(this, R.string.emptyConsoleNameWarning, Toast.LENGTH_LONG).show();
        } else {
            if (consoleID == 0) {
                console = new Console();
            }

            console.setName(consoleName);

            if (consoleID == 0) {
                ConsoleDAO.Insert(this, console);
                Toast.makeText(this, R.string.console_created_toast_message, Toast.LENGTH_LONG).show();
            } else {
                ConsoleDAO.Update(this, console);
                Toast.makeText(this, R.string.console_updated_toast_message, Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    private void LoadForm() {
        console = ConsoleDAO.getConsoleByID(this, consoleID);
        txtConsoleName.setText(console.getName());
    }
}
