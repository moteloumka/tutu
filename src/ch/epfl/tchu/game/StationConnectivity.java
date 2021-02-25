package ch.epfl.tchu.game;

public interface StationConnectivity {
    /**
     * indicates if the two stations are connected by the players railroad
     * @param s1 first station
     * @param s2 second station
     * @return true if connected, false otherwise
     */
    boolean connected(Station s1, Station s2);
}
