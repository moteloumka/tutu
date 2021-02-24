package ch.epfl.tchu.game;
import ch.epfl.tchu.Preconditions;

import java.util.List;


public final class Ticket implements Comparable<Ticket> {
    private final List<Trip> trips;
    private final String text;

    public Ticket(List<Trip> trips){
        Preconditions.checkArgument(!(trips==null));
        //checking that all trips leave from a station with the same name
        for (Trip t : trips){
            if (!(trips.get(0).from().name().equals(t.from().name()))){
                throw new IllegalArgumentException();
            }
        }
        this.trips = trips;
        this.text = computeText();
    }

    public Ticket(Station from, Station to, int points){
            this(List.of(new Trip (from, to, points)));
    }

    public String text() {
        return this.text;
    }

    private String computeText(){
        String ticketText = "";
        if (this.trips.size() >= 2) {
            ticketText += this.trips.get(0).from().toString() + " - {";
            for (Trip t : trips) {
                int i = 0;
                ++i;
                if (i != trips.size()) {
                    ticketText += t.to().toString() + " (" + t.points() + ")";
                }
                else{
                    ticketText += t.to().toString() + " (" + t.points() + ")";
                }
            }
        }
        else {
            ticketText += this.trips.get(0).from().toString() + " - ";
            ticketText+= this.trips.get(0).to().toString();
            ticketText += " (" + this.trips.get(0).points() + ")";
        }
        return ticketText;
    }

    //this is sort of as far as it got for me,
    //this whole tree thing doesn't seem to really make the algo better
    private static String computeTextt(List<Trip> trips){

        String ticketText = "";
        ticketText += trips.get(0).from().toString() + " - ";

        if (trips.size() >= 2){

        }
        else {
            ticketText = String.format(ticketText+" %s  (%s)",
                    trips.get(0).to().toString(),
                    trips.get(0).points());
        }
        return ticketText;
    }

    @Override
    public String toString(){
        return text();
    }

    //we should come up with something here
    public int points (StationConnectivity connectivity){
        return 0;
    }

    @Override
    public int compareTo(Ticket that){
        return that.toString().compareTo(this.toString());
    }
}