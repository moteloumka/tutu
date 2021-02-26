package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * colors are used trough out the project, representing the 8 colors in the game
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public enum Color {
    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;
    //returns the list of all elements present in this enum in their original order
    public final static List<Color> ALL = List.of(Color.values());
    //returns the number of elements in the enum
    public final static int COUNT = ALL.size();

}
