package net.songpon.v1.controller;

import net.songpon.v1.transport.TodoTransport;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

/**
 *
 */

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTodo(@PathParam("id") Integer id) {
        TodoTransport dto = new TodoTransport();
        dto.setId(1);
        dto.setTitle("please do");
        dto.setDescription("do something");
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(dto);
    }
}
