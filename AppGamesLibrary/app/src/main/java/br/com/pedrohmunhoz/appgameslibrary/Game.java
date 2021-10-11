package br.com.pedrohmunhoz.appgameslibrary;

public class Game {

    private int id;
    private String name;
    private int gameYear;
    private int gameFinished;
    private int console_id;

    public Game() {

    }

    public Game(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameYear() {
        return gameYear;
    }

    public void setGameYear(int gameYear) {
        this.gameYear = gameYear;
    }

    public int getGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(int gameFinished) {
        this.gameFinished = gameFinished;
    }

    public int getConsole_id() {
        return console_id;
    }

    public void setConsole_id(int console_id) {
        this.console_id = console_id;
    }

    @Override
    public String toString() {
        return name;
    }
}
