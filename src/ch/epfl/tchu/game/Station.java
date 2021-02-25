package ch.epfl.tchu.game;
import ch.epfl.tchu.Preconditions;

public final class Station {
    //the id number of the station > 0
    private final int id;
    //the name of a station
    private final String name;

    /**
     * Class constructor
     * @param id
     * @param name
     */
    public Station(int id, String name){
        Preconditions.checkArgument(!(id<0));
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id number of the instance of a station
     */
    public int id(){return this.id;}

    /**
     * @return the name of the instance of a station
     */
    public String name(){return this.name;}

    /**
     * overrides toString()
     * @return same as name()
     */
    @Override
    public String toString(){return this.name;}
}
