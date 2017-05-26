package net.songpon.repository;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.query.TodoQuery;

import java.util.List;

/**
 *
 */
public interface TodoRepository {

    Todo getTodo(String id) throws EntityFoundException;

    List<Todo> getTodos(TodoQuery query);

    Todo saveTodo(Todo todo);

    void deleteTodo(String id);

    int count(TodoQuery query);
}
