package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;

import java.util.*;
import java.util.stream.Collectors;

public final class ObservableGameState {
    private PublicGameState pubGameState;
    private PlayerState playerState;
    private final PlayerId playerId;

    //PublicGameState proprieties
    private final IntegerProperty cardDeckCapacity = new SimpleIntegerProperty(0);
    private final IntegerProperty ticketDeckCapacity = new SimpleIntegerProperty(0);
    private final List<ObjectProperty<Card>> visibleCards;
    private final List<ObjectProperty<PlayerId>> routesOwners;

    //PublicPlayerState proprieties (of each player)
    private final Map<PlayerId,IntegerProperty> ticketsInHandCount = new EnumMap<>(PlayerId.class);
    private final Map<PlayerId,IntegerProperty> cardsInHandCount = new EnumMap<>(PlayerId.class);
    private final Map<PlayerId,IntegerProperty> wagonCount = new EnumMap<>(PlayerId.class);
    private final Map<PlayerId,IntegerProperty> constructionPoints = new EnumMap<>(PlayerId.class);

    //PlayerState properties
    private final List<ObjectProperty<Ticket>> ticketsInHand =
            new ArrayList<>(List.of(new SimpleObjectProperty<>(null)));
    private final Map<Card,IntegerProperty> numberOfCardInHand = new EnumMap<>(Card.class);
    private final List<BooleanProperty> canGetRoadList =
            new ArrayList<>(Collections.nCopies(ChMap.routes().size(),new SimpleBooleanProperty(false)));




    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
        //not sure if the next 3 lines are really needed
        PublicCardState newPubCardState = new PublicCardState(List.of(),0,0);
        this.pubGameState = new PublicGameState(0,newPubCardState,null, Map.of(),null);
        this.playerState = new PlayerState(SortedBag.of(),SortedBag.of(),List.of());

        this.visibleCards =
                new ArrayList<>
                        (Collections.nCopies
                                (Constants.FACE_UP_CARDS_COUNT,new SimpleObjectProperty<>(null))) ;
        this.routesOwners =
                new ArrayList<>
                        (Collections.nCopies
                                (ChMap.routes().size(), new SimpleObjectProperty<>(null)));


        for (PlayerId pId : PlayerId.ALL){
            ticketsInHandCount.put(pId, new SimpleIntegerProperty(0));
            cardsInHandCount.put(pId, new SimpleIntegerProperty(0));
            wagonCount.put(pId, new SimpleIntegerProperty(0));
            constructionPoints.put(pId, new SimpleIntegerProperty(0));

        }

        for (Card card: Card.ALL)
            numberOfCardInHand.put(card,new SimpleIntegerProperty(0));
    }

    /**
     * update the information after a change in the game
     * @param pubGS the mew public game state
     * @param playerState the new player state
     */
    public void setState(PublicGameState pubGS, PlayerState playerState){
        List<Route> routes = ChMap.routes();
        this.pubGameState = pubGS;
        this.playerState = playerState;

        cardDeckCapacity.set( 100 * pubGameState.cardState().deckSize()/Constants.TOTAL_CARDS_COUNT);
        ticketDeckCapacity.set( 100 * pubGameState.ticketsCount()/ChMap.tickets().size());

        for(int slot : Constants.FACE_UP_CARD_SLOTS){
            Card cardInSlot = pubGS.cardState().faceUpCards().get(slot);
            visibleCards.get(slot).set(cardInSlot);
        }

        for (PlayerId pID : PlayerId.ALL) {
            PublicPlayerState pubPS = pubGS.playerState(pID);

            ticketsInHandCount.get(pID).set(pubPS.ticketCount());
            cardsInHandCount.get(pID).set(pubPS.cardCount());
            wagonCount.get(pID).set(pubPS.carCount());
            constructionPoints.get(pID).set(pubPS.claimPoints());
            for (Route route : routes) {
                if (pubPS.routes().contains(route))
                    routesOwners.get(routes.indexOf(route)).set(pID);
            }
            //we dont throw away tickets until the end of the game -> we can only add them (?)
            for (Ticket ticket : playerState.tickets()) {
                if (ticketsInHand.stream().map(ObservableObjectValue::get).noneMatch(t -> t == ticket))
                    ticketsInHand.add(new SimpleObjectProperty<>(ticket));
            }

            for (Card card : Card.ALL)
                numberOfCardInHand.get(card).set(playerState.cards().countOf(card));
            /**
             * there's a problem here
             */
            //one thing to add -> route double -> voisine doesn't belong to anyone/.todo
            //whatever this means
            for (Route route: routes){
                canGetRoadList.get(routes.indexOf(route))
                        .set(pubGS.currentPlayerId()==this.playerId
                                && routesOwners.get(routes.indexOf(route))==null
                                && playerState.canClaimRoute(route));
            }
        }
    }

    /**
     *
     * @return percentage (integer) of the card deck fullness
     */
    public ReadOnlyIntegerProperty cardDeckCapacityProperty() {
        return cardDeckCapacity;
    }

    /**
     *
     * @return percentage (integer) of the ticket deck fullness
     */
    public ReadOnlyIntegerProperty ticketDeckCapacityProperty() {
        return ticketDeckCapacity;
    }

    /**
     *
     * @return list of cards on the visible slots : number of the slot -> index in the list
     */
    public List<ReadOnlyObjectProperty<Card>> getVisibleCards() {
        return visibleCards.stream().map(o->(ReadOnlyObjectProperty<Card>)o).collect(Collectors.toList());
    }

    /**
     * index of value is linked to the index of the route in the list of all routes
     * null if the route isn't owned
     * @return list of values corresponding to the owner of routes
     */
    public List<ReadOnlyObjectProperty<PlayerId>> getRoutesOwners() {
        return routesOwners.stream().map(o->(ReadOnlyObjectProperty<PlayerId>)o).collect(Collectors.toList());
    }

    /**
     *
     * @return map of player IDs linked -> number of tickets in their hand
     */
    public Map<PlayerId, ReadOnlyIntegerProperty> getTicketsInHandCount() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        ticketsInHandCount.forEach(answer::put);
        return answer;
    }

    public Map<PlayerId, ReadOnlyIntegerProperty> getCardsInHandCount() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        cardsInHandCount.forEach(answer::put);
        return answer;
    }

    public Map<PlayerId, ReadOnlyIntegerProperty> getWagonCount() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        wagonCount.forEach(answer::put);
        return answer;
    }

    public Map<PlayerId, ReadOnlyIntegerProperty> getConstructionPoints() {
        Map<PlayerId,ReadOnlyIntegerProperty> answer = new EnumMap<>(PlayerId.class);
        constructionPoints.forEach(answer::put);
        return answer;
    }

    public List<ReadOnlyObjectProperty<Ticket>> getTicketsInHand() {
        return ticketsInHand.stream().map(o->(ReadOnlyObjectProperty<Ticket>)o).collect(Collectors.toList());
    }

    public Map<Card, ReadOnlyIntegerProperty> getNumberOfCardInHand() {
        Map<Card,ReadOnlyIntegerProperty> answer = new EnumMap<>(Card.class);
        numberOfCardInHand.forEach(answer::put);
        return answer;

    }

    public List<ReadOnlyBooleanProperty> getCanGetRoadList() {
        return canGetRoadList.stream().map(o->(ReadOnlyBooleanProperty)o).collect(Collectors.toList());
    }

    //same methods, new wrapping

    public boolean canDrawCards(){
        return pubGameState.canDrawCards();
    }
    public boolean canDrawTickets(){
        return pubGameState.canDrawTickets();
    }
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        return playerState.possibleClaimCards(route);
    }

}
