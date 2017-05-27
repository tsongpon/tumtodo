package net.songpon.controller;

import net.songpon.exception.BadRequestException;
import net.songpon.exception.EntityFoundException;
import net.songpon.transport.ErrorTransport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 *
 */
@ControllerAdvice
public class TumtodoErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { EntityFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
        ErrorTransport transport = new ErrorTransport();
        transport.setTime(new Date());
        transport.setStatusCode(HttpStatus.NOT_FOUND.value());
        transport.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, transport,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestBody(RuntimeException ex, WebRequest request) {
        ErrorTransport transport = new ErrorTransport();
        transport.setTime(new Date());
        transport.setStatusCode(HttpStatus.BAD_REQUEST.value());
        transport.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, transport,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(value = { Exception.class})
    protected ResponseEntity<Object> handleExcpetion(Exception ex, WebRequest request) {
        ErrorTransport transport = new ErrorTransport();
        transport.setTime(new Date());
        transport.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        transport.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, transport,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
