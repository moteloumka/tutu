package ch.epfl.tchu.game;
import ch.epfl.tchu.Preconditions;

public final class Station {
    private int id;
    private String name;

    public Station(int id, String name){
        Preconditions.checkArgument(!(id<0));
        this.id = id;
        this.name = name;
    }
    public int id(){return this.id;}
    public String name(){return this.name;}
    public String toString(){return name();}
}
