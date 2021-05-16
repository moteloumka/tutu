package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public final class GraphicalPlayerAdapter implements Player {
    //DELETE THIS
    String debugName;

    private GraphicalPlayer graphicalPlayer;
    private final BlockingQueue<SortedBag<Ticket>> initTickets = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Integer> slots = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> route = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> claimCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> additionalCards = new ArrayBlockingQueue<>(1);

    public GraphicalPlayerAdapter() { }

    /**
     * DO NOT FORGET TO DELETE
     * @param s
     */
    public GraphicalPlayerAdapter(String s){
        this.debugName = s;
    }

    /**
     *  DO NOT FORGET TO FELET THE DEBUG THING
     * @param ownId name of the player
     * @param playerNames map with all the names of players
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
       runLater(() -> this.graphicalPlayer = new GraphicalPlayer(ownId,playerNames,debugName));
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
