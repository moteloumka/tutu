package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public enum PlayerId {

    PLAYER_1,PLAYER_2;

    public final static List<PlayerId> ALL = List.of(PlayerId.values());
    public final static int COUNT = ALL.size();

    /**
     *
     * @return the PlayerId of the player who wasn't just playing
     */
    public PlayerId next(){
        //a bit more complicated than
       //return this == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        //but this way we can play with more than 2 players
        if(this.ordinal() == COUNT - 1)
            return ALL.get(0);
        return ALL.get(this.ordinal()+1);
    }
}
