package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.Map;
import java.util.Random;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class GameState extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final PlayerState p1PlayerState;
    private final PlayerState p2PlayerState;

    private GameState( CardState cardState, PlayerId currentPlayerId,
                      Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer,
                      SortedBag<Ticket> tickets, Random rng,
                       PlayerState p1PlayerState, PlayerState p2PlayerState) {
        super(tickets.size(), (PublicCardState) cardState, currentPlayerId, playerState, lastPlayer);
        this.ticketDeck = Deck.of(tickets, rng);
        this.p1PlayerState =  p1PlayerState;
        this.p2PlayerState =  p2PlayerState;
    }

    static GameState initial(SortedBag<Ticket> tickets, Random rng){

        int ticketsCount = tickets.size();
        SortedBag<Card> cards = Constants.ALL_CARDS;
        SortedBag.Builder<Card> builderP1 = new SortedBag.Builder<>();
        SortedBag.Builder<Card> builderP2 = new SortedBag.Builder<>();

        for(int i = 0; i < Constants.INITIAL_CARDS_COUNT; ++i){
            builderP1.add(cards.get(i));
        }
        for(int i = Constants.INITIAL_CARDS_COUNT; i < Constants.INITIAL_CARDS_COUNT * 2; ++i){
            builderP2.add(cards.get(i));
        }

        SortedBag<Card> p1Cards = builderP1.build();
        SortedBag<Card> p2Cards = builderP1.build();

        PlayerState p1State = new PlayerState(null,p1Cards,null);
        PlayerState p2State = new PlayerState(null, p2Cards,null);

        Deck<Card> deck = Deck.of(cards.difference(p1Cards.union(p2Cards)), rng);
        CardState cardState = CardState.of(deck);
        return null;
    }
}
