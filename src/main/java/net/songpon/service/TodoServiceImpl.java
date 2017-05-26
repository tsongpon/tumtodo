package net.songpon.service;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.exception.UpdateEntityErrorException;
import net.songpon.query.TodoQuery;
import net.songpon.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 *
 */
@Service
public class TodoServiceImpl implements TodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoServiceImpl.class);

    private TodoRepository repository;

    @Inject
    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Todo create(Todo todo) {
        LOGGER.info("Saving todo with title {}", todo.getTitle());
        return repository.saveTodo(todo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Todo update(Todo todo) {
        try {
            repository.getTodo(todo.getId());
        } catch (EntityFoundException e) {
            LOGGER.warn("Cannot update none-existing todo, id {}", todo.getId());
            throw new UpdateEntityErrorException("Cannot update none-existing todo");
        }
        return repository.saveTodo(todo);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Todo> listTodos(TodoQuery query) {
        LOGGER.debug("Listing todo");
        return repository.getTodos(query);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Todo getTodo(String id) {
        LOGGER.debug("Getting todo id {}", id);
        return repository.getTodo(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void delete(String id) {
        LOGGER.info("Deleting todo id {}", id);
        repository.deleteTodo(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public int count(TodoQuery query) {
        LOGGER.debug("Counting todo");
        return repository.count(query);
    }
}
