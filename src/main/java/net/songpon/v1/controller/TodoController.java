package net.songpon.v1.controller;

import net.songpon.domain.Todo;
import net.songpon.exception.BadRequestException;
import net.songpon.query.TodoQuery;
import net.songpon.service.TodoService;
import net.songpon.v1.mapper.TodoMapper;
import net.songpon.v1.transport.TodoResponseTransport;
import net.songpon.v1.transport.TodoTransport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main controller for todos api
 *
 * cache strategy is no-cache for every get methods,
 * since we haven't had infrastructure to support HTTP caching (and invalidate cache mechanism) yet.
 */

@RestController
@RequestMapping(value = "/api/v1/todos")
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private TodoService service;

    @Inject
    public TodoController(TodoService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTodo(@PathVariable("id") String id) {
        LOGGER.debug("Getting todo by id {}", id);
        Todo todo = service.getTodo(id);
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(TodoMapper.map(todo));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listTodos(@RequestParam(value = "size", defaultValue = "5")Integer size,
                                       @RequestParam(value = "start", defaultValue = "0")Integer start,
                                       @RequestParam(value = "title", required = false)String title) {
        LOGGER.debug("Listing todos, size {}", size);
        TodoQuery query = new TodoQuery.QueryBuilder().size(size).start(start).title(title).build();
        List<TodoTransport> todoTransports = TodoMapper.map(this.service.listTodos(query));
        TodoResponseTransport todoResponseTransport = new TodoResponseTransport();
        todoResponseTransport.setSize(todoTransports.size());
        todoResponseTransport.setData(todoTransports);
        todoResponseTransport.setTotal(service.count(query));
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(todoResponseTransport);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTodo(@RequestBody @Valid TodoTransport transport, Errors errors) {
        LOGGER.info("Creating new todo with title {}", transport.getTitle());
        if (errors.hasErrors()) {
            String message = errors.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.joining(","));
            LOGGER.warn("Got bad request body {}", message);
            throw new BadRequestException(message);
        }
        Todo createdTodo = this.service.create(TodoMapper.map(transport));
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdTodo.getId()).toUri()).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTodo(@PathVariable("id") String id,
                                        @RequestBody @Valid  TodoTransport transport,
                                        Errors errors) {
        LOGGER.info("Updating todo by id {}", id);
        if (StringUtils.isBlank(transport.getId())) {
            LOGGER.warn("Got update todo with blank id");
            throw new BadRequestException("Id is required to update todo");
        }
        if (!id.equals(transport.getId())) {
            LOGGER.warn("Got update todo with id change");
            throw new BadRequestException("Id can not be changed");
        }
        if (errors.hasErrors()) {
            String message = errors.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.joining(","));
            LOGGER.warn("Got bad request body {}", message);
            throw new BadRequestException(message);
        }
        service.update(TodoMapper.map(transport));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        LOGGER.info("Deleting todo by id {}", id);
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
