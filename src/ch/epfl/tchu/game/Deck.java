package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 * Deck represents a  deck of "cards" such as Card or Ticket
 */

public final class Deck<C extends Comparable<C> > {
    //easiest way to stock the cards in one deck
    private final List<C> deck;

    /**
     * private constructor called by the static method of
     * @param deck the deck that will be the deck...
     */
    private Deck (List<C> deck){ this.deck = List.copyOf(deck);}

    /**
     * creates a deck  by shuffling the provided cards
     * @param cards cards to be converted into a deck
     * @param rng and instance of  the class Random
     * @param <C> the type of cards that will be constituting the deck
     * @return new instance of a deck of cards
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> deck = new ArrayList<>(cards.toList());
        Collections.shuffle(deck,rng);
        return new Deck<>(deck);
    }

    /**
     * simple getter for the size of  the deck
     * @return size of the list in which all the cards are stocked
     */
    public int size(){
        return deck.size();
    }

    /**
     * indicator if the deck is empty
     * @return true if there aren't any elements in the list of cards
     */
    public boolean isEmpty(){
        return deck.isEmpty();
    }

    /**
     *
     * @return the top card of the deck
     */
    public C topCard(){
        Preconditions.checkArgument(!this.isEmpty());
        return deck.get(0);
    }

    /**
     * creates a new deck
     * @return new deck without the top one
     */
    public Deck<C> withoutTopCard(){
        Preconditions.checkArgument(!this.isEmpty(),"deck can't be empty");
        return new Deck<>( this.deck.subList(1,this.size()) );
    }

    /**
     * creates a SortedBag of the first few cards from the deck
     * @param count the amount of cards  you want
     * @return SortedBag of the first few cards from the deck
     */
    public SortedBag<C> topCards(int count){
        Preconditions.checkArgument(count  <= this.size() && count  >=  0);
        SortedBag.Builder<C> builder = new SortedBag.Builder<>();
        for (int i= 0; i<count;++i){
            builder.add(this.deck.get(i));
        }
        return builder.build();
    }

    /**
     * @param count amounts of cards to remove from the top
     * @return new deck without the first "count" on top of the old one
     */
    public Deck<C> withoutTopCards(int count){
        Preconditions.checkArgument(this.deck.size()>=count);

        List<C> newCards = new ArrayList<>();
        for (int i= count;i<this.deck.size();++i){
            newCards.add(this.deck.get(i));
        }
        return new Deck<>(newCards);
    }
}