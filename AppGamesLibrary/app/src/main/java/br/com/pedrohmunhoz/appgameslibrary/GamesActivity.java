package br.com.pedrohmunhoz.appgameslibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class GamesActivity extends AppCompatActivity {

    private EditText txtGameName;
    private EditText txtGameYear;
    private Spinner cboConsoles;
    private CheckBox chkGameFinished;
    private Button btnSaveGame;
    private int gameID;
    private int selectedConsoleID;
    private Game game;
    private SpinnerAdapter adapter;
    private List<Console> lstConsoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        // Set the reference to the inputs and buttons
        txtGameName = findViewById(R.id.txtGameName);
        txtGameYear = findViewById(R.id.txtGameYear);
        btnSaveGame = findViewById(R.id.btnSaveGame);
        chkGameFinished = findViewById(R.id.chkGameFinished);
        cboConsoles = findViewById(R.id.cboConsoles);

        // Get the Game ID from the Extras, or set 0 if there are none
        gameID = getIntent().getIntExtra("game_id", 0);

        // Retrieve the console lists existing in SQLite
        lstConsoles = ConsoleDAO.getConsoles(GamesActivity.this);

        // Set the custom adapter to populate the Consoles spinner
        adapter = new SpinnerAdapter(GamesActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                lstConsoles);
        cboConsoles = findViewById(R.id.cboConsoles);
        cboConsoles.setAdapter(adapter);

        // If there's any ID informed, load the information on the form
        if (gameID > 0) {
            LoadForm();
        }

        // Get the selected console from list and store it on the local variable
        cboConsoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Console console = adapter.getItem(position);
                selectedConsoleID = console.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        // Set the Click listener to Save button
        btnSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void Save() {
        // Get the name that the user typed
        String gameName = txtGameName.getText().toString();

        // Get the game year that the user typed
        String strGameYear = txtGameYear.getText().toString().equals("") ? "0" : txtGameYear.getText().toString();

        // Cast the type Year to Int
        int gameYear = Integer.parseInt(strGameYear);

        // Get the selected console ID
        int selectedConsoleID = this.selectedConsoleID;

        // Set the gameFinished flag default value
        int gameFinished = 0;

        // If the checkbox is checked, then set Finished to 1
        if (chkGameFinished.isChecked()) {
            gameFinished = 1;
        }

        // Validates if the name is empty
        if (gameName.isEmpty()) {
            Toast.makeText(this, R.string.empty_game_name_warning, Toast.LENGTH_LONG).show();
        }
        else if (selectedConsoleID == 0)  // Validates if the selected console is invalid
        {
            Toast.makeText(this, R.string.invalid_console_selected, Toast.LENGTH_LONG).show();
        }
        else if (gameYear == 0)   // Validates if the game year is invalid
        {
            Toast.makeText(this, R.string.invalid_game_year, Toast.LENGTH_LONG).show();
        } else {
            // If there are no Game ID, then create a new Game object
            if (gameID == 0) {
                game = new Game();
            }

            // Set the name in the object
            game.setName(gameName);

            // Set the game year in the object
            game.setGameYear(gameYear);

            // Set the game finished flag in the object
            game.setGameFinished(gameFinished);

            // Set the console id in the object
            game.setConsole_id(selectedConsoleID);

            // Call the DAO method that inserts/updates the record on database
            GameDAO.SaveGameOnDatabase(this, game);

            // If there is ID on the form, display the Insert success message
            if (gameID == 0) {
                Toast.makeText(this, R.string.game_created_toast_message, Toast.LENGTH_LONG).show();
            } else {
                // If no ID is informed, than display the Update success message
                Toast.makeText(this, R.string.game_updated_toast_message, Toast.LENGTH_LONG).show();
            }

            // Close the activity
            finish();
        }
    }

    private void LoadForm() {
        // Get the console from database based on the informed ID
        game = GameDAO.getGameByID(this, gameID);

        // Get the Game Finished flag int value
        int gameFinished = game.getGameFinished();

        // Set the name in the textbox
        txtGameName.setText(game.getName());

        // Set the game year in the textbox
        txtGameYear.setText(String.valueOf(game.getGameYear()));

        // If Game Finished flag is 1, then check the checkbox
        if (gameFinished == 1) {
            chkGameFinished.setChecked(true);
        }

        // Set the selected console based on the Console ID FK
        for (int i = 0; i < lstConsoles.size(); i++) {
            if (game.getConsole_id() == lstConsoles.get(i).getId()) {
                cboConsoles.setSelection(i);
                break;
            }
        }
    }
}
