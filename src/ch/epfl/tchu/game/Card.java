package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public enum Card {

    BLACK(Color.BLACK), VIOLET(Color.VIOLET), BLUE(Color.BLUE), GREEN(Color.GREEN), YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE), RED(Color.RED), WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    public final static List<Card> ALL = List.of(Card.values());
    public final static int COUNT = ALL.size();
    public final static List<Card> CARS = List.of(BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE);
    private final Color color;

    private Card(Color color){
         this.color = color;
    }

    public static Card of(Color color){
        //assuring that the colors are written in the same order
        //we can directly access them by their order in the enums
        return CARS.get(color.ordinal());
        /**
        switch (color){
            case BLACK:
                return Card.BLACK;
            case VIOLET:
                return Card.VIOLET;
            case BLUE:
                return Card.BLUE;
            case GREEN:
                return Card.GREEN;
            case YELLOW:
                return Card.YELLOW;
            case ORANGE:
                return Card.ORANGE;
            case RED:
                return Card.RED;
            case WHITE:
                return Card.WHITE;
        }
         return null;
         */
    }
    
    public Color color(){
        if(this!= Card.LOCOMOTIVE){
            //same idea as in the method Card of(Color color)
            return Color.ALL.get(this.ordinal());
            /**
            switch (this){
                case BLACK:
                    return Color.BLACK;
                case VIOLET:
                    return Color.VIOLET;
                case BLUE:
                    return Color.BLUE;
                case GREEN:
                    return Color.GREEN;
                case YELLOW:
                    return Color.YELLOW;
                case ORANGE:
                    return Color.ORANGE;
                case RED:
                    return Color.RED;
                case WHITE:
                    return Color.WHITE;
            }
             */
        }
        return null;

    }
}
