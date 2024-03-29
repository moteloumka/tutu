package ch.epfl.tchu.net;

/**
 * Enum used to communicate which methods to use when multiplayer mode on diff computers
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public enum MessageId {
    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS
}
