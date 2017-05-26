package net.songpon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="cannot update none-exist entity")
public class UpdateEntityErrorException extends TumtodoException {
    public UpdateEntityErrorException(Throwable e) {
        super(e);
    }

    public UpdateEntityErrorException(String msg) {
        super(msg);
    }

    public UpdateEntityErrorException(String msg, Throwable e) {
        super(msg, e);
    }
}
