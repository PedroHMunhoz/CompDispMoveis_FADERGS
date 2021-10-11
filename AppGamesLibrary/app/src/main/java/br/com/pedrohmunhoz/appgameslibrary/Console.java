package br.com.pedrohmunhoz.appgameslibrary;

// Class for the Console entity
public class Console {

    // Console ID, primary key
    private int id;

    // Console name
    private String name;

    // Empty constructor for Console
    public Console() {

    }

    // Constructor that creates an object and set the name
    public Console(String name) {
        this.name = name;
    }

    // GET method to get the Console ID
    public int getId() {
        return id;
    }

    // SET method to set the Console ID
    public void setId(int id) {
        this.id = id;
    }

    // GET method to get the Console Name
    public String getName() {
        return name;
    }

    // SET method to set the Console name
    public void setName(String name) {
        this.name = name;
    }

    // Override the toString() method in the class, to return the name
    @Override
    public String toString() {
        return name;
    }
}
