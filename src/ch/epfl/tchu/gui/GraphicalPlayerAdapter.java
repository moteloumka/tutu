package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;
/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */

/**
 * an adapter for the class GraphicalPlayer, acting as a Player,
 * the implementations call methods of the instance of GraphicalPlayer
 * that is an attribute of this class. When calling these methods, the instance of
 * GraphicalPlayerAdapter creates the handlers that are passed as params,
 * these handlers usually consist of waiting until the player puts some Object
 * in one of the BlockingQueue attributes of this instance. Returning the content if demanded.
 * This way the method game() gets information on the players choices.
 */

public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;
    private final BlockingQueue<SortedBag<Ticket>> initTickets = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Integer> slots = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> route = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> claimCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> additionalCards = new ArrayBlockingQueue<>(1);

    public GraphicalPlayerAdapter() { }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
       runLater(() -> this.graphicalPlayer = new GraphicalPlayer(ownId,playerNames));
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));

    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(()-> graphicalPlayer.setState(newState, ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(()-> graphicalPlayer.chooseTickets(tickets
                , ticketsBag -> {
                    try {
                        initTickets.put(ticketsBag);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                }));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return initTickets.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }


    @Override
    public TurnKind nextTurn() {
        BlockingQueue<TurnKind> turnKind = new ArrayBlockingQueue<>(1);

        runLater(()->graphicalPlayer.startTurn(
                () -> {
                    try {
                        turnKind.put(TurnKind.DRAW_TICKETS);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                }
                , slot -> {
                    try {
                        this.slots.put(slot);
                        turnKind.put(TurnKind.DRAW_CARDS);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                }
                , (route, cards) -> {
                    try {
                        this.route.put(route);
                        this.claimCards.put(cards);
                        turnKind.put(TurnKind.CLAIM_ROUTE);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                }));
        try {
            return turnKind.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        BlockingQueue<SortedBag<Ticket>> queue = new ArrayBlockingQueue<>(1);
        runLater(()-> graphicalPlayer.chooseTickets(options
                , ticketsBag -> {
                    try {
                        queue.put(ticketsBag);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                }));
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public int drawSlot() {
        try {
            if(!slots.isEmpty()) {
                    return slots.take();}
            else{
                runLater(()->graphicalPlayer.drawCard(slot -> {
                    try {
                        this.slots.put(slot);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }));
            }
            return this.slots.take();
        }catch (InterruptedException e){
            throw new Error();
        }
    }

    @Override
    public Route claimedRoute() {
        try {
            return this.route.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        try {
            return this.claimCards.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(()->graphicalPlayer.chooseAdditionalCards(options, cards -> {
            try {
                additionalCards.put(cards);
            } catch (InterruptedException e) {
                throw new Error();
            }
        }));
        try {
            return additionalCards.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }
}
