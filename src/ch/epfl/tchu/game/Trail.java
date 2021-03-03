package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Used for trails with single route, inverses the direction of the trail
     * @param route the route of the trail we want to invert
     * @return the opposite trail
     */
    private Trail makeTrailOpposite (Route route){
        Trail trail = new Trail(route.length(), route.station2(), route.station1(),
                List.of(route.station2(), route.station1()), List.of(route));
        return trail;
    }


    public Trail longest(List<Route> routes){
       if (routes.isEmpty()){
           return new Trail(0,null,null,null,null);
       }
       List<Trail> cs = new ArrayList<>();
       List<Trail> singleTrail = new ArrayList<>();

       //creating all possible single trails
       for(Route r:routes){
           cs.add(new Trail(r));
           singleTrail.add(new Trail(r));
           cs.add(makeTrailOpposite(r));
           singleTrail.add(makeTrailOpposite(r));
       }

       List<Trail> csPrime = new ArrayList<>();
        List<Trail> rs = new ArrayList<>();

        //creating biggest possible trail candidates until there are no possibilities of stretching the trail
       do{
           for (Trail c : cs) {
                for (Trail t : singleTrail) {
                     if (t.station1.equals(c.station2) && !c.routes.contains(t)) {
                          rs.add(t);
                     }
                }
                for (Trail r : rs) { csPrime.add(stretch(c, r)); }
           cs = csPrime;
           }
       } while (rs!=null);

       //finding out what the maximum possible length is
       List<Integer> lengthList = new ArrayList<>();
       int maxLength;
       for (Trail c : cs) { lengthList.add(c.length); }
       maxLength = Collections.max(lengthList);

       //making a list of trails which have the biggest length
       List<Trail> maxLengthTrails = new ArrayList<>();
       for (Trail t : cs){
           if (t.length == maxLength) { maxLengthTrails.add(t); }
       }

       //picking the first one of the list, doesn't matter which one
       return maxLengthTrails.get(0);
   }



    public int length() {
        return length;
    }

    public Station station1() {
        if (this.length==0){ return null; }
        else { return station1; }
    }

    public Station station2() {
        if (this.length==0){ return null; }
        else { return station2; }
    }

    private Trail stretch(Trail originalTrail, Trail addedTrail){
        List<Station> newStations = originalTrail.stations;
        newStations.add(addedTrail.station2());
        List<Route> newRoutes = originalTrail.routes;
        newRoutes.add(addedTrail.routes.get(0));
        Trail trail = new Trail(originalTrail.length+addedTrail.length(),this.station1,
                addedTrail.station2(),newStations ,newRoutes);

        return trail;
    }

    @Override
    public String toString(){
       String trailText = "";
       trailText += String.join(" - ", this.stations.toString());
       trailText += " (" + this.length + ")";
       return trailText;
    }
}
