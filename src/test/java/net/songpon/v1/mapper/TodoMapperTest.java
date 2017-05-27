package net.songpon.v1.mapper;

import net.songpon.domain.Todo;
import net.songpon.v1.transport.TodoTransport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TodoMapperTest {
    @Test
    public void mapToTransport() throws Exception {
        Todo todo = new Todo("id", "title", "desc");
        TodoTransport transport = TodoMapper.map(todo);
        assertNotNull(transport);
        assertEquals(todo.getTitle(), transport.getTitle());
        assertEquals(todo.getDescription(), transport.getDescription());
    }

    @Test
    public void mapToDomain() throws Exception {
        TodoTransport transport = new TodoTransport();
        transport.setId("id-of-transport");
        transport.setTitle("just do it");
        transport.setDescription("do it now");
        Todo todo = TodoMapper.map(transport);
        assertEquals(transport.getId(), todo.getId());
        assertEquals(transport.getTitle(), todo.getTitle());
        assertEquals(transport.getDescription(), todo.getDescription());
    }

    @Test
    public void mapList() throws Exception {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo("id", "title", "desc"));
        todos.add(new Todo("id-2", "another title", "another desc"));
        List<TodoTransport> transports = TodoMapper.map(todos);
        assertEquals(todos.size(), transports.size());
    }

}