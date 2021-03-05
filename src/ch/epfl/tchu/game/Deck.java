package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Random;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public final class Deck<C extends Comparable<C>> {
    private final SortedBag<C> deck;

    private Deck (SortedBag<C> deck){
        this.deck = deck;
    }

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
      return null;
  }

    int size(){
        return deck.size();
    }

    boolean isEmpty(){
        return deck.isEmpty();
    }

    C topCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.get(0);
    }

    Deck<C> withoutTopCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        SortedBag.Builder<C> builder = new SortedBag.Builder<>();
        for (int i= 1; i<this.size();++i){
            builder.add(this.deck.get(i));
        }
        SortedBag<C> newDeck = builder.build();
        return new Deck<C>(newDeck);
    }
    //qui retourne un multiensemble contenant les count cartes se trouvant au sommet du tas ;
    //lève IllegalArgumentException si count n'est pas compris entre 0 (inclus) et la taille du tas (incluse),
    SortedBag<C> topCards(int count){
        return null;
    }
    Deck<C> withoutTopCards(int count){
        return null;
    }


}