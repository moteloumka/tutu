package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip {
    private Station from;
    private Station to;
    private int points;

    public Trip(Station from, Station to, int points){
        if(from==null||to==null){
            throw new NullPointerException();
            }
        Preconditions.checkArgument(points>0);
        this.from = Objects.requireNonNull(from,"from Station must be not null");
        this.to = Objects.requireNonNull(to, "to Station must be not null");
        this.points = points;
        }

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

    public Station from(){
        return this.from;
    }
    public Station to(){
        return this.to;
    }
    public int points(){
        return this.points;
    }
    public int points(StationConnectivity connectivity){
        if (connectivity.connected(this.from, this.to)){
            return this.points;
        }
        else return -(this.points);
    }


    }

