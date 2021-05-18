package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */

/**
 * This interface helps encoding Objects and communicating
 * thus making it possible to sent them to different Clients playing the game
 * @param <T> the class of thee objeect to be encoded
 */
public interface Serde <T> {

    /**
     * encoding method
     * @param obj the object to be encoded
     * @return it's string equivalent
     */
    String serialize( T obj);

    /**
     * decoding method
     * @param cipher object encoded as a string
     * @return object as an instance of it's class
     */
    T deserialize(String cipher);

    /**
     *
     * @param ser new instance of Function aka how to serialize
     * @param deSer new instance of Function aka how to deserialize
     * @param <T> the type of object the serde will be created for
     * @return an instance of serde that can theen be easily used
     */
    static <T> Serde<T> of(Function<T, String> ser, Function<String, T> deSer){
        return new Serde<T>() {
            @Override
            public String serialize(T obj) {
                return ser.apply(obj);
            }

            @Override
            public T deserialize(String cipher) {
                return deSer.apply(cipher);
            }
        };
    }

    /**
     * Since our game uses a convention of encoding emuns and some other objects in the same way
     * (giving them a number corresponding to their ordinal/index in list), it is practical to have a methode that will
     * automatically implement this sort of approach
     * @param enumList the lis with all of the instences of this particular enum/
     *                 object thst is somehow linked to an existing/constant list
     * @param <T> the type of object we will be working with
     * @return a serde reeady to encode/decode
     */
    static <T> Serde<T> oneOf(List<T> enumList){
        Function<T,String> ser   = t -> t == null ? "" : String.valueOf(enumList.indexOf(t));
        Function<String,T> deSer = t -> t.equals("") ? null : enumList.get(Integer.parseInt(t));
        return Serde.of(ser,deSer);
    }

    /**
     * this project also uses a specific way to encode elements of a list, this method has thus a similar usage to oneOf
     * @param serde describes how to encode each individual element
     * @param separation the sign the Ciphers have to be separated with
     * @param <T> the type of object contained in the list
     * @return serde ready to serialize/deserialize lists as a whole (and not just their elements)
     */
    static <T> Serde<List<T>> listOf(Serde<T> serde, char separation){
        String str = String.valueOf(separation);
        return new Serde<>() {
            @Override
            public String serialize(List<T> list) {
                List<String> strings = list.stream()
                                .map(serde::serialize)
                                .collect(Collectors.toList());
                return String.join(str,strings);
            }

            @Override
            public List<T> deserialize(String cipher) {
                String[] strings = cipher.split(Pattern.quote(str),-1);
                List<T> list = Arrays.stream(strings)
                        .map(serde::deserialize)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                return list.isEmpty() ? List.of() : list;
            }
        };
    }

    /**
     * same point as listOf but for SortedBag
     * @param serde describes how to encode each individual element
     * @param separation the sign the Ciphers have to be separated with
     * @param <T> the type of object contained in the list
     * @return serde ready to serialize/deserialize SortedBags as a whole (and not just their elements)
     */
    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, char separation){
        String str = String.valueOf(separation);
        return new Serde<>() {
            @Override
            public String serialize(SortedBag<T> bag) {
                List<String> strings = bag.toList()
                        .stream()
                        .map(serde::serialize)
                        .collect(Collectors.toList());
                return String.join(str,strings);
            }

            @Override
            public SortedBag<T> deserialize(String cipher) {
                String[] strings = cipher.split(Pattern.quote(str),-1);
                List<T> list = Arrays.stream(strings)
                        .map(serde::deserialize)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                return list.isEmpty() ? SortedBag.of() : SortedBag.of(list);
            }
        };
    }

}
