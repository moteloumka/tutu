package ch.epfl.test;

import ch.epfl.tchu.game.PlayerId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerIdTest {
    @Test
    void playerIdAllIsDefinedCorrectly() {
        Assertions.assertEquals(List.of(PlayerId.PLAYER_1, PlayerId.PLAYER_2), PlayerId.ALL);
    }

    @Test
    void playerIdNextWorks() {
        assertEquals(PlayerId.PLAYER_2, PlayerId.PLAYER_1.next());
        assertEquals(PlayerId.PLAYER_1, PlayerId.PLAYER_2.next());
    }
}