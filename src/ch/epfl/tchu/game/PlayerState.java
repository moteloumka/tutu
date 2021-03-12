package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class PlayerState extends PublicPlayerState {
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes){
        super(tickets.size(), cards.size(),routes);
        this.tickets = tickets;
        this.cards = cards;
    }

    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size()==4,
                "nb initial cards has to be 4");
        return new PlayerState(null,initialCards,null);
    }

    public SortedBag<Ticket> tickets() {return tickets; }

    public SortedBag<Card> cards() {return cards; }

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(this.tickets.union(newTickets),this.cards,this.routes());
    }

    public PlayerState withAddedCard(Card card){
        SortedBag.Builder<Card> newCardsB = new SortedBag.Builder<>();
        newCardsB.add(card);
        SortedBag<Card> newCards = newCardsB.build();
        return new PlayerState(this.tickets,this.cards.union(newCards),this.routes());
    }

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(this.tickets,this.cards.union(additionalCards),this.routes());
    }

    public boolean canClaimRoute(Route route){
        if(this.carCount()<route.length())
            return false;
        for(SortedBag<Card> combination : route.possibleClaimCards()){
            if(this.cards.contains(combination))
                return true;
        }
        return false;
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(route.length()<=this.carCount(),
                "not enough wagons to get the route");
        return route.possibleClaimCards();
    }

    /**
     * ONE LAST METHOD LEFT
     * @param additionalCardsCount
     * @param initialCards
     * @param drawnCards
     * @return
     */
    public List<SortedBag<Card>> possibleAdditionalCards
            (int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){
        Preconditions.checkArgument(additionalCardsCount<=3 && additionalCardsCount >= 1,
                "the number of additional cards has to be 1, 2 or 3");
        Preconditions.checkArgument(!initialCards.isEmpty() && initialCards.toSet().size() > 3,
                "initial cards can't have more than 2 diff type of cards");
        Preconditions.checkArgument(drawnCards.size()==3,
                "there  has to be exactly 3 drawn cards");

        List<Card> cardsTab = new ArrayList<>();
        for (Card card : initialCards){
            if (card.color() != null)
                cardsTab.add(card);
        }

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();

        this.cards.countOf(Card.LOCOMOTIVE);
        builder.add(this.cards.countOf(Card.LOCOMOTIVE),Card.LOCOMOTIVE);
        if(!cardsTab.isEmpty()){
            int colVo = this.cards.countOf(cardsTab.get(0)) - cardsTab.size();
            if (colVo > 0)
                builder.add(colVo,cardsTab.get(0));
        }

        SortedBag<Card> daBag = builder.build();

        Set<SortedBag<Card>> ssb  = daBag.subsetsOfSize(additionalCardsCount);
        List<SortedBag<Card>> options = new ArrayList<>(ssb);
        options.sort(
                Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));

        return options;
    }

    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){
        List<Route> newRoutes = this.routes();
        newRoutes.add(route);
        return new PlayerState(this.tickets,this.cards.difference(claimCards),newRoutes);
    }

    //do not forget  to activate
    public int ticketPoints(){
        int ans = 0;
        //StationPartition partition = partitionConstructor();
        for (Ticket ticket : this.tickets){
            //ans += ticket.points(partition);
        }
        return ans;
    }

    public int finalPoints(){return ticketPoints()+ super.claimPoints(); }

    /**
     * HAVE TO FINISH THIS
    private StationPartition partitionConstructor(){
        int max = -1;
        List<Route> routes = List.copyOf(this.routes());
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
            for (int j = i+1 ; j < routes.size()-i-1; j++) {

                Route route2 = routes.get(j);

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
     */

}
