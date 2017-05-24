package net.songpon.repository;

import net.songpon.domain.Todo;

import java.util.List;

/**
 *
 */
public interface TodoRepository {

    Todo getTodo(Integer id);

    List<Todo> getTodos();

    Todo saveTodo(Todo todo);

    void deleteTodo(Todo todo);
}
