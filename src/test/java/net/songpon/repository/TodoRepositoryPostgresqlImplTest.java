package net.songpon.repository;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.query.TodoQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

/**
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(executionPhase= Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts="classpath:/test-sql/clear-testdb.sql")
public class TodoRepositoryPostgresqlImplTest {

    @Autowired
    private TodoRepository repository;

    @Test
    public void getTodo() throws Exception {
        Todo savedTodo = repository.saveTodo(mockTodo(null));
        assertNotNull(savedTodo);
        assertNotNull(savedTodo.getId());

        Todo todoFromDb = repository.getTodo(savedTodo.getId());
        assertNotNull(todoFromDb);
    }

    @Test
    public void getTodos() throws Exception {
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));

        List<Todo> todos = repository.getTodos(new TodoQuery.QueryBuilder().size(5).build());
        assertEquals(5, todos.size());
    }

    @Test
    public void testGetTodosWithTitle() throws Exception {
        repository.saveTodo(new Todo(null, "java", "learn java"));
        repository.saveTodo(new Todo(null, "scala", "learn scala"));
        repository.saveTodo(new Todo(null, "python", "learn python"));
        List<Todo> todos = repository.getTodos(new TodoQuery.QueryBuilder().title("java").size(5).build());
        assertEquals(1, todos.size());
    }

    @Test
    public void testGetTodosWithNoneExistTitle() throws Exception {
        repository.saveTodo(new Todo(null, "java", "learn java"));
        repository.saveTodo(new Todo(null, "scala", "learn scala"));
        repository.saveTodo(new Todo(null, "python", "learn python"));
        List<Todo> todos = repository.getTodos(new TodoQuery.QueryBuilder().title("c#").size(5).build());
        assertNotNull(todos);
        assertEquals(0, todos.size());
    }

    @Test
    public void saveTodo() throws Exception {
        Todo mock = new Todo(null, "todo1", "desc1");
        Todo savedTodo = repository.saveTodo(mock);
        assertNotNull(savedTodo);
        assertNotNull(savedTodo.getId());
        Todo fromDb = repository.getTodo(savedTodo.getId());
        assertNotNull(fromDb);
        assertEquals("todo1", fromDb.getTitle());
    }

    @Test
    public void updateTodo() throws Exception {
        Todo mock = new Todo(null, "todo1", "desc1");
        Todo savedTodo = repository.saveTodo(mock);
        assertNotNull(savedTodo);
        assertNotNull(savedTodo.getId());
        Todo fromDb = repository.getTodo(savedTodo.getId());
        assertNotNull(fromDb);
        assertEquals("todo1", fromDb.getTitle());
        fromDb.setTitle("new title");
        repository.saveTodo(fromDb);
        Todo updatedTodo = repository.getTodo(savedTodo.getId());
        assertEquals("new title", updatedTodo.getTitle());
    }

    @Test(expected = EntityFoundException.class)
    public void deleteTodo() throws Exception {
        Todo mock = new Todo(null, "todo1", "desc1");
        Todo savedTodo = repository.saveTodo(mock);
        Todo fromDb = repository.getTodo(savedTodo.getId());
        assertNotNull(fromDb);
        repository.deleteTodo(fromDb.getId());
        repository.getTodo(fromDb.getId());
    }

    @Test
    public void count() throws Exception {
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));
        repository.saveTodo(mockTodo(null));
        int count = repository.count(new TodoQuery.QueryBuilder().build());
        assertEquals(3, count);
    }

    private Todo mockTodo(String id) {
        return new Todo(id, "mock todo", "Mocking todo and test with mockito");
    }

}