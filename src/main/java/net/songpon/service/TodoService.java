package net.songpon.service;

import net.songpon.domain.Todo;
import net.songpon.query.TodoQuery;

import java.util.List;

/**
 *
 */
public interface TodoService {

    Todo create(Todo todo);

    Todo update(Todo todo);

    List<Todo> listTodos(TodoQuery query);

    Todo getTodo(String id);

    void delete(String id);

    int count(TodoQuery query);
}
