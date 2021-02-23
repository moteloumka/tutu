package ch.epfl.tchu.game;
import java.util.List;

public final class Ticket implements Comparable<Ticket> {

    Ticket(List<Trip> trips){

    }
    Ticket(Station from, Station to, int points){

    }

    @Override
    public int compareTo(Ticket o) {
        return 0;
    }
}