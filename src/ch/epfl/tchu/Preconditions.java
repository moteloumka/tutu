package ch.epfl.tchu;

public final class Preconditions {
    /**
     * private constructor, because an utility class shouldn't
     * be initialised
     */
    private Preconditions(){}
    /**
     * static method, used to verify the validity of certain arguments
     * used by other classes
     * @param shouldBeTrue boolean expression that has to be true not to provoke an error
     */
    public static void checkArgument(boolean shouldBeTrue){
        if (!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }

}
