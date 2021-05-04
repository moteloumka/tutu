package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;
import static ch.epfl.tchu.gui.StringsFr.*;
import java.lang.Math.*;

import java.nio.file.Files;
import java.util.List;
import java.util.Set;


/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Info {
    private final String playerName;
    public static final char COMA_SEPARATOR = ',';

    /**
     * generates texts describing the state of the game by using the player's name
     * @param name (String) of the player
     */
    public Info(String name){
        this.playerName = name;
    }

    /**
     *prints the color of the card in question (with 's' if needed)
     * @param card the type of card
     * @param count how many of these type (used to add an s or not)
     * @return the string of the card's color
     */
    public static String cardName(Card card, int count){
        StringBuilder answer = new StringBuilder();
        switch (card){
            case BLACK:
                answer.append(BLACK_CARD);
                break;
            case WHITE:
                answer.append(WHITE_CARD);
                break;
            case RED:
                answer.append(RED_CARD);
                break;
            case ORANGE:
                answer.append(ORANGE_CARD);
                break;
            case YELLOW:
                answer.append(YELLOW_CARD);
                break;
            case VIOLET:
                answer.append(VIOLET_CARD);
                break;
            case GREEN:
                answer.append(GREEN_CARD);
                break;
            case BLUE:
                answer.append(BLUE_CARD);
                break;
            case LOCOMOTIVE:
                answer.append(LOCOMOTIVE_CARD);
                break;
        }
        return answer.toString() + StringsFr.plural(Math.abs(count));
    }


    /**
     * used to compute attemptsTunnelClaim method
     */
     static String cardNumerator(SortedBag<Card> cards){
        StringBuilder totCards = new StringBuilder();
        Set<Card> setCards = cards.toSet();
        int i = 0;
        for (Card card : setCards) {

            int n = cards.countOf(card);
            totCards.append(n)
                    .append(" ")
                    .append(Info.cardName(card,n));

            if(setCards.size()-i > 2){
                totCards.append(", ");
            } else if (setCards.size()-i  == 2){
                totCards.append(AND_SEPARATOR);
            }
            i++;
        }
        return totCards.toString();
    }


    /**
     * used for getsLongestTrailBonus method
     * @param trail the trail we want to print
     * @return a string stating the first station of the trail to the last one
     */
    private static String trail2Stations(Trail trail){
        return String.format("%s%s%s",trail.station1().toString(),EN_DASH_SEPARATOR, trail.station2().toString());
    }


    /**
     * used at the end of the game if both player end in a draw
     * @param playerNames the name of the players
     * @param points the amount of points all players got in the draw
     * @return a String stating the game ended in a draw with number of points
     */
    public static String draw(List<String> playerNames, int points){
        return String.format(DRAW,String.join(AND_SEPARATOR, playerNames),points);
    }

    /**
     * to be called at the beginning on an instance of Info
     * @return a string stating who will begin the game
     */
    public String willPlayFirst(){return String.format(WILL_PLAY_FIRST,this.playerName);}

    /**
     * @param count the numbers of tickets kept
     * @return a string stating the number of tickets a player kept
     */
    public String keptTickets(int count){
        return String.format(KEPT_N_TICKETS,this.playerName, count , plural(Math.abs(count)));
    }

    /**
     * states whose turn it is to play
     * @return a string stating whose turn it is to play
     */
    public String canPlay(){return String.format(CAN_PLAY,this.playerName);}

    /**
     * states how many tickets a player kept
     * @param count the number of tickets kept
     * @return a string stating how many tickets a player kept
     */
    public String drewTickets(int count){
        return String.format(DREW_TICKETS, this.playerName, count, plural(Math.abs(count)));
    }

    /**
     * states if a player drew a card blindly from the deck
     * @return a string stating if a player drew a card blindly from the deck
     */
    public String drewBlindCard(){
        return String.format(DREW_BLIND_CARD, this.playerName);
    }

    /**
     * states if a player drew a visible card from the 5 card face-up cards
     * @param card the card the player picked
     * @return a string stating if a player drew a visible card from the 5 card face-up cards
     */
    public String drewVisibleCard(Card card){
        return String.format(DREW_VISIBLE_CARD, this.playerName, cardName(card,1));
    }

    /**
     *  states the route a player claimed with which cards
     * @param route the route the player claimed
     * @param cards with which cards the player claimed the route
     * @return a string stating the route a player claimed with which cards
     */
    public String claimedRoute(Route route, SortedBag<Card> cards){
        return String.format( CLAIMED_ROUTE, this.playerName, route, cardNumerator(cards) );
    }

    /**
     *  states the tunnel a player want to claim with which initial cards
     * @param route the tunnel the player claimed
     * @param initialCards with which cards the player claimed the tunnel
     * @return a string stating the tunnel a player claimed with which cards
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards){
        return String.format( ATTEMPTS_TUNNEL_CLAIM, this.playerName, route, cardNumerator(initialCards) );
    }

    /**
     * states that the player drew the 3 additional cards and that they imply the additional cost
     * @param drawnCards the drawn cards
     * @param additionalCost the additional cost due to the 3 drawn cards
     * @return a string stating that the player drew the 3 additional cards and that they imply the additional cost
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(ADDITIONAL_CARDS_ARE,cardNumerator(drawnCards)));

        if(additionalCost == 0){
            builder.append(NO_ADDITIONAL_COST);
            return builder.toString();
        }else{
            builder.append(String.format(SOME_ADDITIONAL_COST,
            additionalCost , plural(Math.abs(additionalCost))));
        }
        return builder.toString();
    }

    /**
     * states if a player did not claim a route
     * @param route the route the player did not claim
     * @return the string of the player not claiming the route
     */
    public String didNotClaimRoute(Route route){
        return String.format(DID_NOT_CLAIM_ROUTE, this.playerName, route);
    }

    /**
     * announces that the last turn begins
     * @param carCount the number of cars of the player (<= 2)
     * @return a string the number of cars of the last player and thus the last turn begins
     */
    public String lastTurnBegins(int carCount){
        return String.format(LAST_TURN_BEGINS, this.playerName,
               carCount, plural(Math.abs(carCount)));
    }

    /**
     * states the player got the longest trail
     * @param longestTrail the longest trail
     * @return the string stating who gest the bonus from longest trail
     */
    public String getsLongestTrailBonus(Trail longestTrail){
        return String.format(GETS_BONUS, this.playerName, trail2Stations(longestTrail));
    }


    /**
     * states who won with how many points against how many points
     * @param points the winner's points
     * @param loserPoints the loser's points
     * @return a string stating who won with how many points against how many points
     */
    public String won(int points, int loserPoints){
        return String.format(WINS, this.playerName,
                points, plural(Math.abs(points)),
                loserPoints, plural(Math.abs(loserPoints)) );
    }
}
