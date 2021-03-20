package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.*;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public class PublicGameState {
    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    //from what i understand, this guy
    //only appears when it's time to end the game
    private final PlayerId lastPlayer;
    private final Map<PlayerId, PublicPlayerState> playerState;
    /**
     * public constructor of a public game state
     * @param ticketsCount amount of tickets in the game
     * @param cardState the cardState of the game (non null)
     * @param currentPlayerId (non null)
     * @param playerState map PlayerId -> PlayerState (non null)
     * @param lastPlayer (can be null)
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId,
                           Map<PlayerId, PublicPlayerState> playerState , PlayerId lastPlayer) {
        Preconditions.checkArgument(playerState.size() == 2,
                "there are supposed to be 2 playerId - PublicPlayerState given at construction");
        Preconditions.checkArgument(cardState.deckSize() >= 0,
                "faced down cards cant be of negative quantity");
        this.ticketsCount    = ticketsCount;
        this.cardState       = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.lastPlayer      = lastPlayer;
        this.playerState     = Map.copyOf(Objects.requireNonNull(playerState));
    }

    /**
     * @return the number of tickets in the ticket deck
     */
    public int ticketsCount(){
        return this.ticketsCount;
    }

    /**
     * @return true if ticket deck isn't empty
     */
    public boolean canDrawTickets(){
        return !this.cardState.isDeckEmpty();
    }

    /**
     * @return the public card state of the game
     */
    public PublicCardState cardState(){
        return this.cardState;
    }

    /**
     * @return true if the deck and the recyclable cards sum is bigger than (5)
     */
    public boolean canDrawCards(){
        return (this.cardState.deckSize()
                + this.cardState.discardsSize())
                >= Constants.FACE_UP_CARDS_COUNT;
    }

    /**
     * @return the instance of enum PlayerId of the player who is currently playing
     */
    public PlayerId currentPlayerId(){return this.currentPlayerId;};

    /**
     * @param playerId the player
     * @return player's public state
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return this.playerState.get(playerId);
    }

    /**
     * @return the public state of the current player
     */
    public PublicPlayerState currentPlayerState(){
        return this.playerState.get(this.currentPlayerId);
    }

    /**
     * @return all the routes owned by both players together
     */
    public List<Route> claimedRoutes(){
        List<Route> routes = new ArrayList<>(this.playerState.get(this.currentPlayerId).routes());
        if (this.lastPlayer != null)
            routes.addAll(this.playerState.get(this.lastPlayer).routes());
        return List.copyOf(routes);
    }

    /**
     * @return the  last player
     */
    public PlayerId lastPlayer(){
        //return this.lastPlayer == null? null : this.lastPlayer;
        return lastPlayer;
    }
}
