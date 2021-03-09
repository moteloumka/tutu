package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public enum PlayerId {

    PLAYER_1,PLAYER_2;

    public List<PlayerId> ALL =List.of(PlayerId.values());
    public int COUNT = ALL.size();

    public PlayerId next(){
        if(this == PLAYER_1)
            return PLAYER_2;
        return PLAYER_1;
    }
}
