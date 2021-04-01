package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public interface Player {

    /**
     * different kind of turns
     * the player choses one in the beginning of each turn
     */
    public enum TurnKind{
        DRAW_TICKETS,DRAW_CARDS,CLAIM_ROUTE}

    /**
     * called in the beginning of the game to communicate to
     * the player it's name and those of the other players
     * @param ownId name of the player
     * @param playerNames map with all the names of players
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * called when information has to be communicated to the player
     * it's given as a string and usually uses the class Info to be generated
     * @param info the striing of information
     */
    void receiveInfo(String info);

    /**
     * called every time the game state changes
     * to inform the player of the new public game state
     * and his own new state
     * @param newState the new state of  the game
     * @param ownState the new state of  the player
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * called in the beginning of the game to communicate the tickets that the player received
     * @param tickets the (5) tickets
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * method called  in the beginning of the game
     * to ask the player, which of the (5) tickets
     * distributed  by the method "setInitialTicketChoice" he wants to keep
     * @return
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * called in the beginning of one player's turn
     * to chose the type of turn he wants to play
     * @return DRAW_TICKETS or DRAW_CARDS or CLAIM_ROUTE
     */
    TurnKind nextTurn();

    /**
     * called during the game when a player decides to sort his supplementary cards
     * - communicates possible tickets
     * - gets the  answer: which ones the player keeps
     * @param options possible tickets
     * @return the tickets the player wishes to keep
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * called when the player wants to pick up a card
     * asks the player if a upFace or downFace card is needed
     * @return 0 - 4 (number of the slot) if upFace, otherwise Constants.DECK_SLOT (-1)
     */
    int drawSlot();

    /**
     * called when a player wants to (try to) get a new road in possession
     * to communicate which road interests the player
     * @return the route
     */
    Route claimedRoute();

    /**
     * called when a player wants to (try to) get a new road in possession
     * to communicate which cards the player is willing to use
     * @return the cards
     */
    SortedBag<Card> initialClaimCards();

    /**
     * called when a player tries to get a tunnel in
     * possession and additional cards are necessary
     * @param options the possible card combinations
     * @return if null -> player doesn't want to/ can't add the needed cards
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

}
