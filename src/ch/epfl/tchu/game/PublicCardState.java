package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 * Class PublicCardState contains the informaton on cards visible to all players
 */
public class PublicCardState {

    private final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;

    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize){

        Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT,
                "there isn't precisely "+Constants.FACE_UP_CARDS_COUNT+" face up cards");
        Preconditions.checkArgument(deckSize >= 0,
                "deck size can't be < 0");
        Preconditions.checkArgument(discardsSize >= 0,
                "discard card size can't be < 0");

        //this secures the "immuabilité" (is this necessary?)
        this.faceUpCards = List.copyOf(faceUpCards);
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;
    }

    public int totalSize(){
        return faceUpCards.size()+deckSize+discardsSize;
    }

    public Card faceUpCard(int slot){return faceUpCards.get( Objects.checkIndex(slot,Constants.FACE_UP_CARD_SLOTS.size()) );}

    public List<Card> faceUpCards() {
        return new ArrayList<>(faceUpCards);
    }

    public int deckSize() {
        return deckSize;
    }

    public int discardsSize() {
        return discardsSize;
    }

    public boolean isDeckEmpty(){return discardsSize == 0;}

}
