package net.songpon.service;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.exception.UpdateEntityErrorException;
import net.songpon.query.TodoQuery;
import net.songpon.repository.TodoRepository;
import net.songpon.repository.TodoRepositoryPostgresqlImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoServiceImplTest {

    @Autowired
    private TodoService service;

    @MockBean
    private TodoRepositoryPostgresqlImpl repository;

    @Test
    public void create() throws Exception {
        Todo createdMockTodo = new Todo("new-created", "todo", "have to do");
        when(repository.saveTodo(any(Todo.class))).thenReturn(createdMockTodo);

        Todo toCreateTodo = new Todo(null, "todo", "have to do");
        Todo createdTodo = service.create(toCreateTodo);
        assertNotNull(createdTodo);
        assertNotNull(createdTodo.getId());
        verify(repository, times(1)).saveTodo(any(Todo.class));
    }

    @Test
    public void update() throws Exception {
        Todo toUpdateTodo = new Todo("some-task-id", "todo", "have to do");
        when(repository.saveTodo(toUpdateTodo)).thenReturn(toUpdateTodo);
        Todo updatedTodo = service.update(toUpdateTodo);
        assertNotNull(updatedTodo);
        verify(repository, times(1)).saveTodo(toUpdateTodo);
    }

    @Test
    public void listTodos() throws Exception {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo("11-22", "do it", "todo"));
        todos.add(new Todo("22-33", "do it", "todo"));
        todos.add(new Todo("33-33", "do it", "todo"));
        TodoQuery query = new TodoQuery.QueryBuilder().size(3).build();
        when(repository.getTodos(query)).thenReturn(todos);

        List<Todo> result = service.listTodos(query);
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository, times(1)).getTodos(query);
    }

    @Test
    public void getTodo() throws Exception {
        when(repository.getTodo("112-345-11")).thenReturn(new Todo("112-345-11","todo", "must do"));
        Todo todo = service.getTodo("112-345-11");
        assertNotNull(todo);
        assertEquals("112-345-11", todo.getId());
        verify(repository, times(1)).getTodo("112-345-11");
    }

    @Test
    public void delete() throws Exception {
        doNothing().when(repository).deleteTodo("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
        service.delete("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
        verify(repository, times(1)).deleteTodo("96d4cdf2-0d47-4975-999a-1d82ec138aa8");
    }

    @Test
    public void count() throws Exception {
        TodoQuery query = new TodoQuery.QueryBuilder().build();
        when(repository.count(query)).thenReturn(3);
        int count = service.count(query);
        assertEquals(3, count);
        verify(repository, times(1)).count(query);
    }

    @Test(expected = EntityFoundException.class)
    public void testGetNoneExistTodo() throws Exception {
        when(repository.getTodo("none-exist_id")).thenThrow(EntityFoundException.class);
        service.getTodo("none-exist_id");
    }

    @Test(expected = UpdateEntityErrorException.class)
    public void updateNoneExistTodo() throws Exception {
        Todo toUpdateTodo = new Todo("some-task-id", "todo", "have to do");
        when(repository.getTodo("some-task-id")).thenThrow(EntityFoundException.class);
        Todo updatedTodo = service.update(toUpdateTodo);
        assertNotNull(updatedTodo);
        verify(repository, times(1)).getTodo("some-task-id");
        verify(repository, times(0)).saveTodo(toUpdateTodo);
    }
}