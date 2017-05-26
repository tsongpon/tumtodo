package net.songpon.exception;

/**
 *
 */
public class BadRequestException extends TumtodoException {
    public BadRequestException(Throwable e) {
        super(e);
    }

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable e) {
        super(msg, e);
    }
}
