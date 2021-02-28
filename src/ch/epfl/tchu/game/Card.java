package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 *a card represents a wagon or a locomotive that will later take part in the game
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public enum Card {

    BLACK(Color.BLACK), VIOLET(Color.VIOLET), BLUE(Color.BLUE), GREEN(Color.GREEN), YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE), RED(Color.RED), WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    //returns the list of all elements present in this enum in their original order
    public final static List<Card> ALL = List.of(Card.values());
    //returns the number of elements in the enum
    public final static int COUNT = ALL.size();
    //returns the list of CARS in the order they appear in the enum
    public final static List<Card> CARS = List.of(BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);
    //a color is assigned to an instance of thr enum on creation
    private final Color color;

    /**
     * private enum constructor that assigns the needed color to the card
     * null to LOCOMOTIVE
     * @param color the color to be assigned
     */
    private Card(Color color){
         this.color = color;
    }

    /**
     * @param color the color in question
     * @return wagon of the same color
     */
    public static Card of(Color color){
        //assuring that the colors are written in the same order
        //we can directly access them by their order in the enums
        return CARS.get(color.ordinal());
    }

    /**
     * @return the Color of the instance of the Card
     */
    public Color color(){
       return this.color;
    }
}
