package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Trail {
    private final int length;
    private final Station station1;
    private final Station station2;
    private final List<Station> stations;
    private final List<Route> routes;

    private Trail(int length, Station station1, Station station2, List<Station> stations, List<Route> routes) {
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
        this.stations = stations;
        this.routes = routes;
    }
    private Trail(Route route){
        this(route.length(), route.station1(), route.station2(),
             List.of(route.station1(),route.station2()),List.of(route) );
    }


    public Trail longest(List<Route> routes){
       if (routes.isEmpty()){
           return new Trail(0,null,null,null,null);
       }
       List<Trail> trails = new ArrayList<>();
       for(Route r:routes){
           trails.add(new Trail(r));
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

    private Trail stretch(Route route){
        List<Station> newStations = this.stations.subList(0,this.stations.size()-1);
        newStations.add(route.station2());
        List<Route> newRoutes = this.routes.subList(0,this.routes.size()-1);
        newRoutes.add(route);
        new Trail(this.length+route.length(),this.station1,route.station2(),newStations ,newRoutes);
    }

    @Override
    public String toString(){
        return (this.station1+" - "+this.station2+" ("+this.length+")");
    }
}
