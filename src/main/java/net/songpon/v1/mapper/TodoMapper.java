package net.songpon.v1.mapper;

import net.songpon.domain.Todo;
import net.songpon.v1.transport.TodoTransport;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class TodoMapper {

    public static Todo map(TodoTransport transport) {
        return new Todo(transport.getId(), transport.getTitle(), transport.getDescription());
    }

    public static TodoTransport map(Todo todo) {
        TodoTransport transport = new TodoTransport();
        transport.setId(todo.getId());
        transport.setTitle(todo.getTitle());
        transport.setDescription(todo.getDescription());
        return transport;
    }

    public static List<TodoTransport> map(List<Todo> todos) {
        return todos.stream().map(TodoMapper::map).collect(Collectors.toList());
    }
}
