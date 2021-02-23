package ch.epfl.tchu.game;
import java.util.List;


public final class Ticket implements Comparable<Ticket> {
    private List<Trip> trips;

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
        this.trips = trips;
    }

    public Ticket(Station from, Station to, int points){
            this(List.of(new Trip (from, to, points)));
    }


    public String text() {
        return computeText();
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
        return 0;
    }

    @Override
    public int compareTo(Ticket o) {
        return 0;
    }
}