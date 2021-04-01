package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
class GameTest {
    Map<PlayerId, Player> players = new EnumMap<PlayerId, Player>(PlayerId.class);
    Map<PlayerId, String> playerNames = new EnumMap<PlayerId, String>(PlayerId.class);
    SortedBag<Ticket> allTickets = SortedBag.of(ChMap.tickets());
    List<Route> allRoutes = ChMap.routes();
    Random rng = new Random();
    Player player1 = new TestPlayer(1,this.allRoutes,"player1");
    Player player2 = new TestPlayer(1,this.allRoutes,"player2");
    //Player player3 = new TestPlayer(1,this.allRoutes,"player3");

    @Test
    void endGameWhen(){
        players.put(PlayerId.PLAYER_1,player1);
        players.put(PlayerId.PLAYER_2,player2);
        //players.put(PlayerId.PLAYER_3,player3);
        playerNames.put(PlayerId.PLAYER_1,"player1");
        playerNames.put(PlayerId.PLAYER_2,"player2");
        //playerNames.put(PlayerId.PLAYER_3,"player3");
        Game.play(players,playerNames,allTickets,rng);
    }

    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private String name;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;

        private SortedBag<Ticket> ticketsToChoseFrom;
        private SortedBag<Ticket> tickets;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        public TestPlayer(long randomSeed, List<Route> allRoutes, String name) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
            this.name = name;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            System.out.println(ownId);
            System.out.println(playerNames);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println(this.name+" receives info :");
            System.out.println(info);
            System.out.println(turnCounter);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            //idk what to do here
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            this.ticketsToChoseFrom = tickets;
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            this.tickets = SortedBag.of(this.ticketsToChoseFrom.toList().subList(0,3));
            return tickets;
        }

        @Override
        public TurnKind nextTurn() {
            System.out.println(this.ownState.carCount());
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            //finds the routs the player can claim
            List<Route> claimableRoutes = allRoutes.stream()
                    .filter(r -> r.length() > 1)
                    .filter(r -> ownState.canClaimRoute(r))
                    .filter(r -> r.level()== Route.Level.OVERGROUND)
                    .collect(Collectors.toList());

            List<Route> shortRoutes = allRoutes.stream()
                    .filter(r -> ownState.canClaimRoute(r))
                    .filter(r -> r.length()==1)
                    .collect(Collectors.toList());

            if (claimableRoutes.isEmpty() && ownState.carCount() > 5 ) {
                System.out.println(this.name+" drew cards");
                return TurnKind.DRAW_CARDS;

            } else {

                int routeIndex;
                Route route;
                List<SortedBag<Card>> cards;

                if (ownState.carCount() > 5){
                    routeIndex = rng.nextInt(claimableRoutes.size());
                    route = claimableRoutes.get(routeIndex);
                }else {
                    routeIndex = rng.nextInt(shortRoutes.size());
                    route = shortRoutes.get(routeIndex);
                }
                cards = ownState.possibleClaimCards(route);
                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            this.ticketsToChoseFrom = options;
            this.tickets = this.tickets.union(SortedBag.of(options.get(0)));
            return tickets;
        }

        @Override
        public int drawSlot() {
            if(rng.nextBoolean())
                return -1;
            else
                return rng.nextInt(5);
        }

        @Override
        public Route claimedRoute() {
            return this.routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return this.initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            SortedBag<Card> withoutInitial = this.ownState.cards().difference(this.initialClaimCards);
            List<SortedBag<Card>> possibleReturn =
                    options.stream()
                            .filter(withoutInitial::contains)
                            .collect(Collectors.toList());

            return possibleReturn.isEmpty() ? SortedBag.of() : possibleReturn.get(0);
        }
    }

}