package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip {
    private final Station from;
    private final Station to;
    private final int points;

    /**
     * class constructor
     * @param from station of depart
     * @param to station of destination
     * @param points points earned for the trip
     */
    public Trip(Station from, Station to, int points){
        //checks if the information given to the constructor is valid
        Preconditions.checkArgument(points>0);
        this.from = Objects.requireNonNull(from,"from Station must be not null");
        this.to = Objects.requireNonNull(to, "to Station must be not null");
        this.points = points;
        }
    /**
     *
      * @param from list of starting stations
     * @param to list of destination stations
     * @param points
     * @return list of all different trips possible from one of the destinations in the first list
     * to the second, each of them being equal to the same amount of points
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points){
        Preconditions.checkArgument(!(from.isEmpty()||to.isEmpty()||points<=0));
        List<Trip> trips = new ArrayList<>();

        for(Station f : from){
            for(Station t : to){
                trips.add(new Trip(f, t, points));
            }
        }
        return trips;
    }

    /**
     * @return the trip's starting station
     */
    public Station from(){
        return this.from;
    }

    /**
     * @return the trip's destination station
     */
    public Station to(){
        return this.to;
    }

    /**
     * @return points the trip is worth
     */
    public int points(){
        return this.points;
    }

    /**
     * checks if the the two stations of the trip are connected with the connectivity
     * @param connectivity
     * @return + points the trip is worth if the stations are connected
     * - points --//-- aren't connected
     */

    public int points(StationConnectivity connectivity){
        if (connectivity.connected(this.from, this.to)){
            return this.points;
        }
        else return -(this.points);
    }

    }

