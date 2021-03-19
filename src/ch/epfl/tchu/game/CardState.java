package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class CardState extends PublicCardState{

    private final Deck<Card> faceDownCards;
    private final SortedBag<Card> outOfGameCards;

    /**
     * private constructor, use static methode of to create an instance
     * @param faceUpCards
     * @param faceDownCards
     * @param outOfGameCards
     */
    private CardState(List<Card> faceUpCards, Deck<Card> faceDownCards, SortedBag<Card> outOfGameCards) {
        super(faceUpCards, faceDownCards.size(), outOfGameCards.size());
        this.faceDownCards = faceDownCards;
        this.outOfGameCards = outOfGameCards;
    }

    /**
     *
     * @param deck a Deck of cards
     * @return The state of this deck
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT,
                "not enough cards in the deck ( < "+Constants.FACE_UP_CARDS_COUNT+" )");

        List<Card> faceUpCards = deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList();
        Deck<Card> faceDownCards = deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT);

        return new CardState(faceUpCards,faceDownCards,SortedBag.of());
    }

    /**
     *
     * @param slot the place of the public card to be removed
     * @return new card state with the pub card of index slot replaced by a card from thee deck (of face down cards)
     */
    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!this.faceDownCards.isEmpty(),
                "can't pick a card from visible cards with an empty deck");

        int index = Objects.checkIndex(slot,Constants.FACE_UP_CARD_SLOTS.size());

        List <Card> newFaceUpCards = new ArrayList<>(this.faceUpCards());
        newFaceUpCards.set(slot,this.faceDownCards.topCard());
        Deck<Card> newFaceDownCards = this.faceDownCards.withoutTopCard();

        return new CardState(newFaceUpCards,newFaceDownCards,this.outOfGameCards);
    }

    /**
     *
     * @return the card on to of faced down cards
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!this.faceDownCards.isEmpty(),
                "can't pick a card from an empty deck");

        return faceDownCards.topCard();
    }

    /**
     *
     * @return the card state but without the card on top of faced down cards
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!this.faceDownCards.isEmpty(),
                "can't recreate a card state with an empty deck");

        return new CardState(this.faceUpCards(),this.faceDownCards.withoutTopCard(),this.outOfGameCards);
    }

    /**
     *
     * @param rng an instance of Random
     * @return card state with reshuffled deck from the discarded cards
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(this.isDeckEmpty(),
                "deck has to be empty to mix");

        return new CardState(this.faceUpCards(),Deck.of(this.outOfGameCards,rng),SortedBag.of());
    }

    /**
     *
     * @param additionalDiscards cards to be added to the discarded cards
     * @return card state but with cadditional cards added to the discarded ones
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState( this.faceUpCards() , this.faceDownCards , this.outOfGameCards.union(additionalDiscards) );
    }
}
