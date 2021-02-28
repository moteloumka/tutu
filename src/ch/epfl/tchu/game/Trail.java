package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Trail {
    private final int length;
    private final Station station1;
    private final Station station2;

    private Trail(int length, Station station1, Station station2) {
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
    }

    public Trail longest(List<Route> routes){
       if (routes.isEmpty()){
           return new Trail(0,null,null);
       }
   }

    public int getLength() {
        return length;
    }

    public Station getStation1() {
        return station1;
    }

    public Station getStation2() {
        return station2;
    }

    @Override
    public String toString(){
        return (this.station1+" - "+this.station2+" ("+this.length+")");
    }
}
