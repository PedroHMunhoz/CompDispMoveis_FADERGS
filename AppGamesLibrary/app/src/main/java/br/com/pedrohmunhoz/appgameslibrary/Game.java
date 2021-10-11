package br.com.pedrohmunhoz.appgameslibrary;

// Class for the Game entity
public class Game {

    // Game ID, primary key
    private int id;

    // Game name
    private String name;

    // Game year
    private int gameYear;

    // Flag if the game has been finished or not
    private int gameFinished;

    // ID to reference the Console which the game belongs
    private int console_id;

    // Empty constructor for Game
    public Game() {

    }

    // Constructor that creates an object and set the name
    public Game(String name) {
        this.name = name;
    }

    // GET method to get the Game ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // GET method to get the Game Name
    public String getName() {
        return name;
    }

    // SET method to set the Game Name
    public void setName(String name) {
        this.name = name;
    }

    // GET method to get the Game Year
    public int getGameYear() {
        return gameYear;
    }

    // SET method to set the Game Year
    public void setGameYear(int gameYear) {
        this.gameYear = gameYear;
    }

    // GET method to get the Game Finished flag
    public int getGameFinished() {
        return gameFinished;
    }

    // SET method to set the Game Finished flag
    public void setGameFinished(int gameFinished) {
        this.gameFinished = gameFinished;
    }

    // GET method to get the Game Console ID
    public int getConsole_id() {
        return console_id;
    }

    // SET method to set the Game Console's ID
    public void setConsole_id(int console_id) {
        this.console_id = console_id;
    }

    // Override the toString() method in the class, to return the name
    @Override
    public String toString() {
        return name;
    }
}
