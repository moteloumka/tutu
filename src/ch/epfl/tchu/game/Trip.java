package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

public final class Trip {
    private Station from;
    private Station to;
    private int points;
    public Trip(Station from, Station to, int points){
        if(from==null||to==null){
            throw new NullPointerException();
            }
        Preconditions.checkArgument(points>0);
        }
    }

