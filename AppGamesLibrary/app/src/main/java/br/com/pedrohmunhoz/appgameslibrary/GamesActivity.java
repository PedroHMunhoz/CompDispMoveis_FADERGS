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

        txtGameName = findViewById(R.id.txtGameName);
        txtGameYear = findViewById(R.id.txtGameYear);
        btnSaveGame = findViewById(R.id.btnSaveGame);
        chkGameFinished = findViewById(R.id.chkGameFinished);
        cboConsoles = findViewById(R.id.cboConsoles);

        gameID = getIntent().getIntExtra("game_id", 0);

        // Retrieve the console lists existing in SQLite
       lstConsoles = ConsoleDAO.getConsoles(GamesActivity.this);

        // Set the custom adapter to populate the Consoles spinner
        adapter = new SpinnerAdapter(GamesActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                lstConsoles);
        cboConsoles = findViewById(R.id.cboConsoles);
        cboConsoles.setAdapter(adapter);

        if (gameID > 0) {
            LoadForm();
        }

        // Get the selected console from list
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

        btnSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void Save() {
        String gameName = txtGameName.getText().toString();
        String strGameYear = txtGameYear.getText().toString().equals("") ? "0" : txtGameYear.getText().toString();
        int gameYear = Integer.parseInt(strGameYear);
        int selectedConsoleID = this.selectedConsoleID;
        int gameFinished = 0;

        // If the checkbox is checked, then set Finished to 1
        if (chkGameFinished.isChecked()) {
            gameFinished = 1;
        }

        if (gameName.isEmpty()) {
            Toast.makeText(this, R.string.empty_game_name_warning, Toast.LENGTH_LONG).show();
        } else if (selectedConsoleID == 0) {
            Toast.makeText(this, R.string.invalid_console_selected, Toast.LENGTH_LONG).show();
        } else if (gameYear == 0) {
            Toast.makeText(this, R.string.invalid_game_year, Toast.LENGTH_LONG).show();
        } else {
            if (gameID == 0) {
                game = new Game();
            }

            game.setName(gameName);
            game.setGameYear(gameYear);
            game.setGameFinished(gameFinished);
            game.setConsole_id(selectedConsoleID);

            //console_created_toast_message
            if (gameID == 0) {
                GameDAO.Insert(this, game);
                Toast.makeText(this, R.string.game_created_toast_message, Toast.LENGTH_LONG).show();
            } else {
                GameDAO.Update(this, game);
                Toast.makeText(this, R.string.game_updated_toast_message, Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    private void LoadForm() {
        game = GameDAO.getGameByID(this, gameID);
        int gameFinished = game.getGameFinished();
        txtGameName.setText(game.getName());
        txtGameYear.setText(String.valueOf(game.getGameYear()));

        if(gameFinished == 1){
            chkGameFinished.setChecked(true);
        }

        for (int i = 0; i < lstConsoles.size(); i++) {
            if (game.getConsole_id() == lstConsoles.get(i).getId()) {
                cboConsoles.setSelection(i);
                break;
            }
        }
    }
}
