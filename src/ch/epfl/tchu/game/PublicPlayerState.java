package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;

    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        int w = Constants.INITIAL_CAR_COUNT;
        int pc = 0;
        for(Route route : routes){
            w  -= route.length();
            pc += route.claimPoints();
        }
        this.carCount = w;
        this.claimPoints = pc;
    }

    public int ticketCount(){return this.ticketCount;}
    public int cardCount(){return this.cardCount;}
    public List<Route> routes() {return List.copyOf(routes);}
    public int carCount() {return carCount;}
    public int claimPoints() {return claimPoints;}

}
