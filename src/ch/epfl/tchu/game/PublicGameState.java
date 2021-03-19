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
    private final PlayerId lastPlayer;
    private final Map<PlayerId, PublicPlayerState> playerState;


    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId,
                           Map<PlayerId, PublicPlayerState> playerState , PlayerId lastPlayer) {
        Preconditions.checkArgument(playerState.size() == 2,
                "there are supposed to be 2 playerId - PublicPlayerState given at construction");
        Preconditions.checkArgument(cardState.deckSize() >= 0,
                "faced down cards cant be of negative quantity");
        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.lastPlayer = lastPlayer;
        this.playerState = Objects.requireNonNull(playerState);
    }

    public int ticketsCount(){
        return this.ticketsCount;
    }

    public boolean canDrawTickets(){
        return !this.cardState.isDeckEmpty();
    }

    public PublicCardState cardState(){
        return this.cardState;
    }

    public boolean canDrawCards(){
        return (this.cardState.deckSize() + this.cardState.discardsSize()) >= 5;
    }

    public PublicPlayerState playerState(PlayerId playerId){
        return this.playerState.get(playerId);
    }

    public PublicPlayerState currentPlayerState(){
        return this.playerState.get(this.currentPlayerId);
    }

    public List<Route> claimedRoutes(){
        List<Route> routes = new ArrayList<>(this.playerState.get(this.currentPlayerId).routes());
        if (this.lastPlayer != null)
            routes.addAll(this.playerState.get(this.lastPlayer).routes());
        return List.copyOf(routes);
    }

    public PlayerId lastPlayer(){
        if (this.lastPlayer != null)
            return this.lastPlayer;
        return null;
    }
}
