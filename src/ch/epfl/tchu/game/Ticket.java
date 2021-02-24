package ch.epfl.tchu.game;
import java.util.*;


public final class Ticket implements Comparable<Ticket> {
    private List<Trip> trips;
    private String text;

    public Ticket(List<Trip> trips){
       if (trips==null){
            throw new IllegalArgumentException();
        }


        //checking that all trips leave from a station with the same name
        this.trips = Objects.requireNonNull(trips, "trips must not be null");
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
        return this.text.compareTo(that.text);
    }
}