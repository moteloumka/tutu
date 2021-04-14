package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Serde <T> {

    abstract String serialize( T obj);
    abstract T deserialize(String cipher);

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

    static <T> Serde<T> oneOf(List<T> enumList){
        Function<T,String> ser   = t -> String.valueOf(enumList.indexOf(t));
        Function<String,T> deSer = t -> enumList.get(Integer.parseInt(t));
        return Serde.of(ser,deSer);
    }

    static <T> Serde<List<T>> listOf(Serde<T> serde, char separation){
        String str = String.valueOf(separation);
        return new Serde<List<T>>() {
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
                return Arrays.stream(strings)
                        .map(serde::deserialize)
                        .collect(Collectors.toList());
            }
        };
    }

    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, char separation){
        String str = String.valueOf(separation);
        return new Serde<SortedBag<T>>() {
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
                        .collect(Collectors.toList());
                return SortedBag.of(list);
            }
        };
    }

}
