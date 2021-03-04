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

    private boolean isOppositeSingleTrail (Trail t){
        return this.station1().equals(t.station2()) && this.station2().equals(t.station1())
                && this.getRoutes().equals(t.getRoutes());
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
        List<Trail> currentTrails = new ArrayList<>();

        //creating all possible single trails
        for(Route r:routes){
            currentTrails.add(new Trail(r));
            currentTrails.add(makeTrailOpposite(r));
        }

        List<Trail> csPrime = new ArrayList<>();
        List<Route> toBeMergedlist = new ArrayList<>();
        boolean hasMergeOccured;

        //creating biggest possible trail candidates until there are no possibilities of stretching the trail
        do{
            hasMergeOccured = false;
            for (int i=0; i<currentTrails.size();++i) {

                System.out.println( i +" iteration trail1 : " + currentTrails.get(i).station1());
                toBeMergedlist.clear();

                for (Route route : routes) {
                    //System.out.println( " route : " + route.station1());
                    if (route.station1().equals(currentTrails.get(i).station2) && !currentTrails.get(i).routes.contains(route)){
                        toBeMergedlist.add(route);
                        System.out.println(route+"added");
                    }
                }

                System.out.println("here's the list of stuff to be merged");
                for(Route r: toBeMergedlist){
                    System.out.println("route gonna b merged : "+r);
                }

                if (!toBeMergedlist.isEmpty()){
                    for (Route r : toBeMergedlist) {
                        System.out.println(" r : " + r.station1());
                        csPrime.add(addRoute(currentTrails.get(i),r));
                        hasMergeOccured = true;
                    }
                }
            }
            if (csPrime.size()!=0){
                currentTrails = csPrime;
                System.out.println("we got to the bottom of this");
                System.out.println(toBeMergedlist.size());
                toBeMergedlist.clear();
                //csPrime.clear();
            }
        } while (hasMergeOccured);

        //finding out what the maximum possible length is
        List<Integer> lengthList = new ArrayList<>();
        int maxLength;
        for (Trail c : currentTrails) { lengthList.add(c.length); }
        maxLength = Collections.max(lengthList);

        //making a list of trails which have the biggest length
        List<Trail> maxLengthTrails = new ArrayList<>();
        for (Trail t : currentTrails){
            if (t.length == maxLength) { maxLengthTrails.add(t); }
        }

        //picking the first one of the list, doesn't matter which one
        System.out.println(maxLengthTrails.get(0));
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

    public List<Route> getRoutes () {
        if (this.routes==null){ return null; }
        else { return this.routes; }
    }

    public static Trail addRoute(Trail trail ,Route route){
        Objects.requireNonNull(route,"route has to be non null to be added");
        Preconditions.checkArgument(trail.station2 == route.station1());

        List<Station> newStations = new ArrayList<>(trail.stations);
        newStations.add(route.station2());

        List<Route> newRoutes = new ArrayList<>(trail.routes);
        newRoutes.add(route);

        return new Trail(trail.length + route.length(),trail.station1,
                route.station2(),newStations ,newRoutes);
    }

    /**
     * Static method that merges two existing instances of trails as follows:
     * adds the second trail to the end of the first one
     * - it is important that station 2 of originalTrail and station 1 of addedTrail are the same
     * @param originalTrail first trail (has to finish where the second begins)
     * @param addedTrail second trail that will be merged to original trail HAS TO BE CONSISTING OF ONLY ONE ROUTE
     * @return new merged trail that starts at station1 of originalTrail and ends at station2 of addedTrail
     * @throws IllegalArgumentException if the stations don't match
     * @throws NullPointerException if one of the trails is null
     */
    public static Trail merge(Trail originalTrail, Trail addedTrail){

        Objects.requireNonNull(originalTrail,"first trail to stretch must be not null");
        Objects.requireNonNull(addedTrail,"second trail to stretch must be not null");
        Preconditions.checkArgument(originalTrail.station2.id() == addedTrail.station1.id()
                && addedTrail.routes.size() == 1);

        List<Station> newStations = new ArrayList<>(originalTrail.stations);
        newStations.add(addedTrail.station2);

        List<Route> newRoutes = new ArrayList<>(originalTrail.routes);
        newRoutes.add(addedTrail.routes.get(0));

        return new Trail(originalTrail.length+addedTrail.length,originalTrail.station1,
                addedTrail.station2,newStations ,newRoutes);
    }

    /**
     * computes the string with the information on the trail
     * @return information on the trail
     */
    @Override
    public String toString(){
        String trailText = "";
        trailText += String.join(" - ", this.stations.toString());
        trailText += " (" + this.length + ")";
        return trailText;
    }
}
