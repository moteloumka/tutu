package ch.epfl.tchu.game;
import ch.epfl.tchu.Preconditions;

import java.util.*;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Ticket implements Comparable<Ticket> {
    private final List<Trip> trips;
    private final String text;

    /**
     * @param trips list of trips the ticket contains. Must be at least one.
     * @throws NullPointerException if the List provided is null
     */
    public Ticket(List<Trip> trips){
        Preconditions.checkArgument(!(trips==null || trips.size()==0));
        //checking that all trips leave from a station with the same name
        Objects.requireNonNull(trips, "trips must not be null");

        for (Trip t : trips) {
            if (!(trips.get(0).from().name().equals(t.from().name()))) {
                throw new IllegalArgumentException();
            }
        }
    this.trips = List.copyOf(trips);
    this.text = computeText();

    }

    /**
     * constructor for ticket containing only one trip
     * @param from the departure station of the ticket (must not be null)
     * @param to the arrival station of the ticket (must not be null)
     * @param points the nbr of points that can be gained by connecting the two stations (must not be null)
     */
    public Ticket(Station from, Station to, int points){
            this(List.of(new Trip (from, to, points)));
    }

    /**
     * @return the text of the trip (departure station to arrival stations and points)
     */
    public String text() {
        return this.text;
    }

    /**
     * does the computation for the text method
     * @return the text of ticket
     */
    private  String computeText(){
        String ticketText = "";
        ticketText += this.trips.get(0).from().toString() + " - ";
        TreeSet <String> toList = new TreeSet<>();
        if (this.trips.size() >= 2) {
            ticketText += "{";
            for (Trip t : trips) {
                toList.add(t.to().name() + " (" + t.points() + ")");
            }
            ticketText += String.join(", ", toList);
            ticketText += "}";
        }
        else {
            ticketText+= this.trips.get(0).to().toString();
            ticketText += " (" + this.trips.get(0).points() + ")";
        }
        return ticketText;
    }

    /**
     * @return the same as the text() method
     */
    @Override
    public String toString(){
        return this.text;
    }

    /**
     * @param connectivity tells us if the stations of the trip have been connected
     * @return the nbr of points from the trip if it only contains one, the nbr with the most points from all the trips if
     * more than one trip in the ticket
     */
    public int points (StationConnectivity connectivity){
        List<Integer> pointsList = new ArrayList<>();
        for (Trip t : trips){
            pointsList.add(t.points(connectivity));
        }
        return Collections.max(pointsList);
    }

    /**
     * tells if the tickets trips have all been connected
     * @return true if they are connected
     */
    public boolean isFullyDone(StationConnectivity stationConnectivity ){
        return trips.stream().allMatch(trip -> trip.points(stationConnectivity) > 0);
    }

    /**
     * tells if some of this tickets trips have been connected
     * @return true if some are connected, yet some still aren't
     */
    public boolean isPartlyDone(StationConnectivity stationConnectivity){
        return (trips.stream().anyMatch(trip -> trip.points(stationConnectivity) > 0)
                && trips.stream().anyMatch(trip -> trip.points(stationConnectivity) < 0));
    }

    /**
     * compares both tickets by alphabetical order.
     * @param that the ticket with which we're comparing this ticket
     * @return negative int if this comes before that, 0 if same ticket, positive int if this comes after that
     */
    @Override
    public int compareTo(Ticket that) {
        return this.text.compareTo(that.text);
    }
}