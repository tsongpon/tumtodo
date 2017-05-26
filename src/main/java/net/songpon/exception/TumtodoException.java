package net.songpon.exception;

/**
 *
 */
public class TumtodoException extends RuntimeException {
    public TumtodoException(Throwable e) {
        super(e);
    }

    public TumtodoException(String msg) {
        super(msg);
    }

    public TumtodoException(String msg, Throwable e) {
        super(msg, e);
    }
}
