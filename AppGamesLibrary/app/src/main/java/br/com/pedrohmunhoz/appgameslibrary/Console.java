package br.com.pedrohmunhoz.appgameslibrary;

public class Console {
    private int id;
    private String name;

    public Console() {

    }

    public Console(String name) {
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

    @Override
    public String toString() {
        return name;
    }
}
