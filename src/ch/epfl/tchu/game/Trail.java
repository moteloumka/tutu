package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    /**
     * the constructors for this class are private so that an instance of a trail can only be created through methods
     * that construct it using "elaborated" algorithms such as the method longest
     * @param length length of the trail: sum of the lengths of the routes in the trail
     * @param station1 first station on one side of the trail
     * @param station2 last station on the opposite side of station1
     * @param stations all the stations that are in the trail (sorted from station 1 to station 2)
     * @param routes all routes in the trail (also sorted going form station 1 to station 2)
     */
    public Trail(int length, Station station1, Station station2, List<Station> stations, List<Route> routes) {

        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
        this.stations = stations;
        this.routes = routes;
    }

    /**
     * Constructor for a trail, consisting from only one route
     * @param route the only route in the trail
     */
    public Trail(Route route){
        this(route.length(), route.station1(), route.station2(),
             List.of(route.station1(),route.station2()),List.of(route) );
    }

    /**
     * constructor for an instance of a trail that is null
     */
    public Trail(){
        this(0,null,null,null,null);
    }

    /**
     * Used for trails with single route, inverses the direction of the trail
     * @param route the route of the trail we want to invert
     * @return the opposite trail
     */
    private static Trail makeTrailOpposite (Route route){
        Trail trail = new Trail(route.length(), route.station2(), route.station1(),
                List.of(route.station2(), route.station1()), List.of(route));
        return trail;
    }

    /**
     *
     * @param routes list of routes
     * @return the longest possible trail consisting of routes that were passed as a parameter
     */
    public static Trail longest(List<Route> routes){
       if (routes.isEmpty()){
           return new Trail();
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
               rs.clear();
                for (Trail t : singleTrail) {
                     if (t.station1.equals(c.station2) && !c.routes.contains(t)) {
                          rs.add(t);
                     }
                }
                for (Trail r : rs) { csPrime.add(merge(c, r)); }
           cs = csPrime;
           }
       } while (rs.size()!=0);

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

    /**
     *
     * @return the trail length
     */
    public int length() {
        return length;
    }

    /**
     *
     * @return the first station of the trail
     */
    public Station station1() {
        if (this.length==0){ return null; }
        else { return station1; }
    }

    /**
     *
     * @return the last station of the trail
     */
    public Station station2() {
        if (this.length==0){ return null; }
        else { return station2; }
    }

    /**
     * Static method that merges two existing instances of trails as follows:
     * adds the second trail to the end of the first one
     * - it is important that station 2 of originalTrail and station 1 of addedTrail are the same
     * @param originalTrail first trail (has to finish where the second begins)
     * @param addedTrail second trail that will be merged to original trail
     * @return new merged trail that starts at station1 of originalTrail and ends at station2 of addedTrail
     * @throws IllegalArgumentException if the stations don't match
     * @throws NullPointerException if one of the trails is null
     */
    private static Trail merge(Trail originalTrail, Trail addedTrail){

        Objects.requireNonNull(originalTrail,"first trail to stretch must be not null");
        Objects.requireNonNull(addedTrail,"second trail to stretch must be not null");
        Preconditions.checkArgument(originalTrail.station2.id() == addedTrail.station1.id());

        List<Station> newStations = new ArrayList<>();
        for(Station s : originalTrail.stations){ newStations.add(s); }
        for (Station s : addedTrail.stations){ newStations.add(s); }

        List<Route> newRoutes = new ArrayList<>();
        for (Route r : originalTrail.routes){ newRoutes.add(r); }
        for (Route r : addedTrail.routes){ newRoutes.add(r); }

        Trail trail = new Trail(originalTrail.length+addedTrail.length(),originalTrail.station1,
                addedTrail.station2,newStations ,newRoutes);

        return trail;
    }

    /**
     * computes the string with the information on the trail
     * @return
     */
    @Override
    public String toString(){
       String trailText = "";
       trailText += String.join(" - ", this.stations.toString());
       trailText += " (" + this.length + ")";
       return trailText;
    }
}
