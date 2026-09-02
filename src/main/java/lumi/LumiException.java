package lumi;

/**
 * Signals that the user's input could not be understood by Lumi.
 */
public class LumiException extends Exception {
    public LumiException(String message) {
        super(message);
    }
}
