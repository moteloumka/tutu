package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

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

    /**
     * what every other player can see
     * @param ticketCount the number of tickets the player has
     * @param cardCount the number of cards the player has
     * @param routes the routes the player has claimed
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount>=0 && cardCount >= 0);
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

    /**
     *
     * @return the number of tickets of the player
     */
    public int ticketCount(){return this.ticketCount;}

    /**
     *
     * @return the number of cards from the player
     */
    public int cardCount(){return this.cardCount;}

    /**
     *
     * @return a list (immutable) of the routes that the player has
     */
    public List<Route> routes() {return List.copyOf(routes);}

    /**
     *
     * @return the numbers of cars left of the player
     */
    public int carCount() {return carCount;}

    /***
     *
     * @return the points the player has gained from its routes
     */
    public int claimPoints() {return claimPoints;}

}
