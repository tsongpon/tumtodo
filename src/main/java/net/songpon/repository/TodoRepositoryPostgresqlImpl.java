package net.songpon.repository;

import net.songpon.domain.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 *
 */
@Repository
public class TodoRepositoryPostgresqlImpl implements TodoRepository {

    private JdbcTemplate jdbcTemplate;

    @Inject
    public TodoRepositoryPostgresqlImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo getTodo(Integer id) {
        return null;
    }

    @Override
    public List<Todo> getTodos() {
        return null;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        return null;
    }

    @Override
    public void deleteTodo(Todo todo) {

    }
}
