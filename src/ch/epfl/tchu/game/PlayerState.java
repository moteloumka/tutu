package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class
PlayerState extends PublicPlayerState {
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    /**
     * public constructor for the state of a player
     * @param tickets the tickets the player has
     * @param cards the cards the player currently has
     * @param routes the routes owned by the player
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes){
        super(tickets.size(), cards.size(),routes);
        this.tickets = tickets;
        this.cards = cards;
    }

    /**
     * used for the beginning of a game
     * @param initialCards the cards the player starts with
     * @return an instance of playerstate with no ticket, 4 cards and no route owned by the player
     */
    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size()==4,
                "nb initial cards has to be 4");
        SortedBag<Ticket> tickets = SortedBag.of();
        List<Route> routes = List.of();

        return new PlayerState(tickets,initialCards,routes);
    }

    /**
     *
     * @return the tickets of the player
     */
    public SortedBag<Ticket> tickets() {return tickets; }

    /**
     *
     * @return the cards of the player
     */
    public SortedBag<Card> cards() {return cards; }

    /**
     *
     * @param newTickets a sorted bag containing the tickets we want to add to the player
     * @return a new instance of the playerState with the tickets added
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(this.tickets.union(newTickets),this.cards,this.routes());
    }

    /**
     *
     * @param card card we want to add to the player
     * @return a new instance of the playerState with the cards added
     */
    public PlayerState withAddedCard(Card card){
        SortedBag.Builder<Card> newCardsB = new SortedBag.Builder<>();
        newCardsB.add(card);
        SortedBag<Card> newCards = newCardsB.build();
        return new PlayerState(this.tickets,this.cards.union(newCards),this.routes());
    }

    /**
     *
     * @param route the route that we want to claim
     * @return true player can claim the route, false otherwise
     */
    public boolean canClaimRoute(Route route){
        if(this.carCount()<route.length())
            return false;
        for(SortedBag<Card> combination : route.possibleClaimCards()){
            if(this.cards.contains(combination))
                return true;
        }
        return false;
    }

    /**
     *
     * @param route the route we want to claim
     * @return the list of sorted bag of possible cards we can use to claim the route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(route.length()<=this.carCount(),
                "not enough wagons to get the route");
        return route
                .possibleClaimCards()
                .stream()
                .filter(this.cards::contains)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param additionalCardsCount the number of additional cards the player has to use to claim the tunnel
     * @param initialCards cards the player uses to claim the tunnel
     * @return the list of sorted bag of possible cards we can use to claim the tunnel
     */

    public List<SortedBag<Card>> possibleAdditionalCards
            (int additionalCardsCount, SortedBag<Card> initialCards){

        Preconditions.checkArgument(additionalCardsCount<=3 && additionalCardsCount >= 1,
                "the number of additional cards has to be 1, 2 or 3");
        Preconditions.checkArgument(!initialCards.isEmpty() && initialCards.toSet().size() <= 2,
                "initial cards can't have more than 2 diff type of cards");

        List<Card> cardsTab = new ArrayList<>();
        for (Card card : initialCards){
            if (card.color() != null)
                cardsTab.add(card);
        }

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();

        int locoCards = this.cards.countOf(Card.LOCOMOTIVE)- initialCards.countOf(Card.LOCOMOTIVE) ;
        if (locoCards > 0)
            builder.add(locoCards,Card.LOCOMOTIVE);
        if(!cardsTab.isEmpty()){
            int coloredCards = this.cards.countOf(cardsTab.get(0))- cardsTab.size();
            if (coloredCards > 0)
                builder.add(coloredCards,cardsTab.get(0));
        }

        SortedBag<Card> daBag = builder.build();

        if (daBag.size() >= additionalCardsCount){
            Set<SortedBag<Card>> ssb  = daBag.subsetsOfSize(additionalCardsCount);
            List<SortedBag<Card>> options = new ArrayList<>(ssb);
            options.sort(
                    Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
            return options;
            }
        return List.of();
        }


    /**
     *
     * @param route the route the player claims
     * @param claimCards the cards the player uses to claim the route with
     * @return a new state of the player with the route minus the cards he used
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){
        List<Route> newRoutes = new ArrayList<>(this.routes());
        newRoutes.add(route);
        return new PlayerState(this.tickets,this.cards.difference(claimCards),newRoutes);
    }

    /**
     *
     * @return the points the player gains or loses whether or not he connects stations from a ticket
     */
    public int ticketPoints(){
        int ans = 0;
        StationPartition partition = partitionConstructor();
        for (Ticket ticket : this.tickets){
            ans += ticket.points(partition);
        }
        return ans;
    }

    public boolean isFullyDone(Ticket ticket){
        StationPartition partition = partitionConstructor();
        return ticket.isFullyDone(partition);
    }

    public boolean isPartlyDone(Ticket ticket){
        StationPartition partition = partitionConstructor();
        return ticket.isPartlyDone(partition);
    }
    /**
     *
     * @return the points gained from tickets and from the routes the player has claimed
     */
    public int finalPoints(){return ticketPoints()+ super.claimPoints(); }

    /**
     * used only for ticketPoints()
     * @return a StationPartition that tells us the network of stations the player has
     */
    private StationPartition partitionConstructor(){
        int max = -1;
        List<Route> routes = this.routes();
        for (Route route : routes){
            if(route.station1().id() > max)
                max = route.station1().id();
            if(route.station2().id() > max)
                max = route.station2().id();
        }
        StationPartition.Builder builder = new StationPartition.Builder(max+1);

        for (int i = 0; i < routes.size() ; i++) {
            Route route1 = routes.get(i);
            //connecting the two stations in each route
            builder.connect(route1.station1(),route1.station2());
            //going through all the other routes
            for (int j = i+1 ; j < routes.size(); j++) {
                Route route2 = routes.get(j);
                //connecting stations if any routes are connected by a station
                if(route1.station1().equals(route2.station1()))
                    builder.connect(route1.station2(),route2.station2());
                if(route1.station1().equals(route2.station2()))
                    builder.connect(route1.station2(),route2.station1());
                if(route1.station2().equals(route2.station1()))
                    builder.connect(route1.station1(),route2.station2());
                if(route1.station2().equals(route2.station2()))
                    builder.connect(route1.station1(),route2.station1());
            }
        }
        return builder.build();
    }
}
