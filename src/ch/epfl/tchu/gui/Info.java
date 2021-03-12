package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;
import ch.epfl.tchu.gui.StringsFr.*;
import java.lang.Math.*;

import java.util.List;
import java.util.Set;


/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Info {
    private final String playerName;

    public Info(String name){
        this.playerName = name;
    }

    public static String cardName(Card card, int count){
        StringBuilder answer = new StringBuilder();
        switch (card){
            case BLACK:
                answer.append(StringsFr.BLACK_CARD);
                break;
            case WHITE:
                answer.append(StringsFr.WHITE_CARD);
                break;
            case RED:
                answer.append(StringsFr.RED_CARD);
                break;
            case ORANGE:
                answer.append(StringsFr.ORANGE_CARD);
                break;
            case YELLOW:
                answer.append(StringsFr.YELLOW_CARD);
                break;
            case VIOLET:
                answer.append(StringsFr.VIOLET_CARD);
                break;
            case GREEN:
                answer.append(StringsFr.GREEN_CARD);
                break;
            case BLUE:
                answer.append(StringsFr.BLUE_CARD);
                break;
            case LOCOMOTIVE:
                answer.append(StringsFr.LOCOMOTIVE_CARD);
                break;
        }
        return answer.toString() + StringsFr.plural(Math.abs(count));
    }

    private static String cardNumerator(SortedBag<Card> cards){
        StringBuilder totCards = new StringBuilder();
        Set<Card> setCards = cards.toSet();
        int i = 0;
        for (Card card : setCards) {

            int n = cards.countOf(card);
            totCards.append(n)
                    .append(" ")
                    .append(StringsFr.CARDS)
                    .append(" ")
                    .append(Info.cardName(card,n));

            if(setCards.size()-i > 2){
                totCards.append(", ");
            } else if (setCards.size()-i  == 2){
                totCards.append(StringsFr.AND_SEPARATOR);
            }
            i++;
        }
        return totCards.toString();
    }

    private static String trail2Stations(Trail trail){
        return String.format("%s %s %s",trail.station1().toString(),StringsFr.EN_DASH_SEPARATOR, trail.station2().toString());
    }



    public static String draw(List<String> playerNames, int points){
        return String.format(StringsFr.DRAW,String.join(", ", playerNames),points);
    }

    public String willPlayFirst(){return String.format(StringsFr.WILL_PLAY_FIRST,this.playerName);}

    public String keptTickets(int count){
        return String.format(StringsFr.KEPT_N_TICKETS,this.playerName, count , StringsFr.plural(Math.abs(count)));
    }

    public String canPlay(){return String.format(StringsFr.CAN_PLAY,this.playerName);}

    public String drewTickets(int count){
        return String.format(StringsFr.DREW_TICKETS, this.playerName, count, Math.abs(count));
    }

    public String drewBlindCard(){
        return String.format(StringsFr.DREW_BLIND_CARD, this.playerName);
    }

    public String drewVisibleCard(Card card){
        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playerName, cardName(card,1));
    }

    public String claimedRoute(Route route, SortedBag<Card> cards){
        return String.format( StringsFr.CLAIMED_ROUTE, this.playerName, route, cardNumerator(cards) );
    }

    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards){
        return String.format( StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playerName, route, cardNumerator(initialCards) );
    }

    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost){
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(StringsFr.ADDITIONAL_CARDS_ARE,cardNumerator(drawnCards)));

        if(drawnCards.isEmpty()){
            builder.append(StringsFr.NO_ADDITIONAL_COST);
        }else{
            builder.append(String.format(StringsFr.SOME_ADDITIONAL_COST,
            additionalCost , StringsFr.plural(Math.abs(additionalCost))));
        }
        return builder.toString();
    }

    public String didNotClaimRoute(Route route){
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this.playerName, route);
    }

    public String lastTurnBegins(int carCount){
        return String.format(StringsFr.LAST_TURN_BEGINS, this.playerName,
               carCount, StringsFr.plural(Math.abs(carCount)));
    }

    public String getsLongestTrailBonus(Trail longestTrail){
        return String.format(StringsFr.GETS_BONUS, this.playerName, trail2Stations(longestTrail));
    }

    public String won(int points, int loserPoints){
        return String.format(StringsFr.WINS, this.playerName,
                points, StringsFr.plural(Math.abs(points)),
                loserPoints, StringsFr.plural(Math.abs(loserPoints)) );
    }
}
