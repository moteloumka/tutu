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

    private CardState(List<Card> faceUpCards, Deck<Card> faceDownCards, SortedBag<Card> outOfGameCards) {
        super(faceUpCards, faceUpCards.size(), outOfGameCards.size());
        this.faceDownCards = faceDownCards;
        this.outOfGameCards = outOfGameCards;
    }

    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size()<Constants.FACE_UP_CARDS_COUNT,
                "not enough cards in the deck ( < "+Constants.FACE_UP_CARDS_COUNT+" )");

        List<Card> faceUpCards = deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList();
        Deck<Card> faceDownCards = deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT);

        return new CardState(faceUpCards,faceDownCards,SortedBag.of());
    }

    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!this.faceDownCards.isEmpty(),
                "can't pick a card from visible cards with an empty deck");

        int index = Objects.checkIndex(slot,Constants.FACE_UP_CARD_SLOTS.size());

        List <Card> newFaceUpCards = new ArrayList<>(this.faceUpCards());
        newFaceUpCards.set(slot,this.faceDownCards.topCard());
        Deck<Card> newFaceDownCards = this.faceDownCards.withoutTopCard();

        return new CardState(newFaceUpCards,newFaceDownCards,this.outOfGameCards);
    }

    public Card topDeckCard(){
        Preconditions.checkArgument(!this.faceDownCards.isEmpty(),
                "can't pick a card from an empty deck");

        return faceDownCards.topCard();
    }

    public CardState withoutTopCard(){
        Preconditions.checkArgument(!this.faceDownCards.isEmpty(),
                "can't recreate a card state with an empty deck");

        return new CardState(this.faceUpCards(),this.faceDownCards.withoutTopCard(),this.outOfGameCards);
    }

    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(this.isDeckEmpty(),
                "deck has to be empty to mix");

        return new CardState(this.faceUpCards(),Deck.of(outOfGameCards,rng),SortedBag.of());
    }

    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState( this.faceUpCards() , this.faceDownCards , this.outOfGameCards.union(additionalDiscards) ) ;
    }
}
