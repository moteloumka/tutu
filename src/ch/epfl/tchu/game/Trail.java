package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import org.junit.jupiter.api.Test;

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
    private Trail(int length, Station station1, Station station2, List<Station> stations, List<Route> routes) {

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
    private Trail(Route route){
        this(route.length(), route.station1(), route.station2(),
                List.of(route.station1(),route.station2()),List.of(route) );
    }

    /**
     * constructor for an instance of a trail that is null
     */
    private Trail(){
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
            for (Trail currentTrail : currentTrails) {
                toBeMergedlist.clear();
                for (Route route : routes) {
                    if (route.station1().equals(currentTrail.station2) && !currentTrail.routes.contains(route)) {
                        toBeMergedlist.add(route);
                    }
                }
                if (!toBeMergedlist.isEmpty()) {
                    for (Route r : toBeMergedlist) {
                        csPrime.add(addRoute(currentTrail, r));
                        hasMergeOccured = true;
                    }
                }
            }
            if (csPrime.size()!=0){
                currentTrails.clear();
                currentTrails.addAll(csPrime);
                toBeMergedlist.clear();
                csPrime.clear();
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

    /**
     * adds a route to an existing trail
     * @param trail a trail
     * @param route a route (that is connected to the trail and isn't contained in the trail's routes)
     * @return new trail with route added
     * @throws NullPointerException if route or trail are null
     * @throws IllegalArgumentException if the trail isn't connected to the route
     * or if  the route is contained in the trail already
     */
    private static Trail addRoute(Trail trail ,Route route){
        Objects.requireNonNull(trail,"trail has to be non null to be added");
        Objects.requireNonNull(route,"route has to be non null to be added");
        Preconditions.checkArgument(trail.station2 == route.station1(),
                "route cannot be added to this trail");
        Preconditions.checkArgument(!trail.routes.contains(route),
                "trail already contains this route");

        List<Station> newStations = new ArrayList<>(trail.stations);
        newStations.add(route.station2());

        List<Route> newRoutes = new ArrayList<>(trail.routes);
        newRoutes.add(route);

        return new Trail(trail.length + route.length(),trail.station1,
                route.station2(),newStations ,newRoutes);
    }

    /**
     * computes the string with the information on the trail
     * @return information on the trail
     */
    @Override
    public String toString(){
        String trailText = "";
        List<String> trailList = new ArrayList<>();
        for (Station s : stations){
            trailList.add(s.toString());
        }
        trailText += String.join(" - ", trailList);
        trailText += " (" + this.length + ")";
        return trailText;
    }


    /**
     * overrides the equal method
     * @param obj with which we want to compare this trail to
     * @return true if they have the same ordered list of routes which basically means they're the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trail trail = (Trail) obj;
        //in case both trail have no routes
        if (this.length==0 && ((Trail) obj).length==0) return true;
        return this.getRoutes().toString().equals(((Trail) obj).getRoutes().toString());
    }
}
