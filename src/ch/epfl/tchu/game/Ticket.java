package ch.epfl.tchu.game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class Ticket implements Comparable<Ticket> {
    private List<Trip> trips;
    private String text;

    public Ticket(List<Trip> trips){
        if (trips==null){
            throw new IllegalArgumentException();
        }
        //checking that all trips leave from a station with the same name
        for (Trip t : trips){
            if (!(trips.get(0).from().name().equals(t.from().name()))){
                throw new IllegalArgumentException();
            }
        }
        this.trips = Objects.requireNonNull(trips, "trips must not be null");
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
                if (i != trips.size())
                    ticketText += t.to().toString() + " (" + t.points() + ")";
                else{
                    ticketText += t.to().toString() + " (" + t.points() + ")}";
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



    public String toString(){
        return text();
    }

    public int points (StationConnectivity connectivity){
        List<Integer> pointsList = new ArrayList<>();
        for (Trip t : trips){
            pointsList.add(t.points(connectivity));
        }
        return Collections.max(pointsList);
    }

    @Override
    public int compareTo(Ticket that) {
        return that.text.compareTo(this.text);
    }
}