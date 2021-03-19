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

    /**
     *
     * @return total size (face up with face down)
     */
    public int totalSize(){
        return faceUpCards.size()+deckSize+discardsSize;
    }

    /**
     *
     * @param slot position of the card on the table
     * @return the face up Card
     */
    public Card faceUpCard(int slot){return faceUpCards.get( Objects.checkIndex(slot,Constants.FACE_UP_CARD_SLOTS.size()) );}

    /**
     *
     * @return all the face up Cards from the table
     */
    public List<Card> faceUpCards() {
        return new ArrayList<>(faceUpCards);
    }

    /**
     *
     * @return the size of "pioche" aka non faced down cards
     */
    public int deckSize() {
        return deckSize;
    }

    /**
     *
     * @return the size of discarded cards
     */
    public int discardsSize() {
        return discardsSize;
    }

    /**
     *
     * @return true if faced down cards are empty
     */
    public boolean isDeckEmpty(){return deckSize == 0;}

}
