package net.songpon.exception;


/**
 *
 */

public class EntityFoundException extends TumtodoException {

    public EntityFoundException(Throwable e) {
        super(e);
    }

    public EntityFoundException(String msg) {
        super(msg);
    }

    public EntityFoundException(String msg, Throwable e) {
        super(msg, e);
    }
}
