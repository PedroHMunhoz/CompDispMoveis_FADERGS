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

        // Set the reference to the inputs and buttons
        txtConsoleName = findViewById(R.id.txtConsoleName);
        btnSaveConsole = findViewById(R.id.btnSaveConsole);

        // Get the Console ID from the Extras, or set 0 if there are none
        consoleID = getIntent().getIntExtra("console_id", 0);

        // If there's any ID informed, load the information on the form
        if (consoleID > 0) {
            LoadForm();
        }

        // Set the Click listener to Save button
        btnSaveConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void Save() {
        // Get the name that the user typed
        String consoleName = txtConsoleName.getText().toString();

        // Validates if the name is empty
        if (consoleName.isEmpty()) {
            Toast.makeText(this, R.string.emptyConsoleNameWarning, Toast.LENGTH_LONG).show();
        } else {
            // If there are no Console ID, then create a new Console object
            if (consoleID == 0) {
                console = new Console();
            }

            // Set the name in the object
            console.setName(consoleName);

            // Call the DAO method that inserts/updates the record on database
            ConsoleDAO.SaveConsoleOnDatabase(this, console);

            // If there is ID on the form, display the Insert success message
            if (consoleID == 0) {
                Toast.makeText(this, R.string.console_created_toast_message, Toast.LENGTH_LONG).show();
            } else {
                // If no ID is informed, than display the Update success message
                Toast.makeText(this, R.string.console_updated_toast_message, Toast.LENGTH_LONG).show();
            }

            // Close the activity
            finish();
        }
    }

    private void LoadForm() {
        // Get the console from database based on the informed ID
        console = ConsoleDAO.getConsoleByID(this, consoleID);

        // Set the name in the textbox
        txtConsoleName.setText(console.getName());
    }
}
