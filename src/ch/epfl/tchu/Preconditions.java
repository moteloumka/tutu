package ch.epfl.tchu;

/**
 *  @author Nikolay (314355)
 *  @author Gullien (316143)
 */
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
    public static void checkArgument(boolean shouldBeTrue, String errorMessage){
        if(!shouldBeTrue){
            throw new IllegalArgumentException(errorMessage);
        }

    }
    //wise men on Stack Overflow said this is better than trowing errors
    //who are we to disagree?
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

}
