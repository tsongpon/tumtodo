package net.songpon.repository;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.query.TodoQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.inject.Inject;
import java.sql.*;
import java.util.List;

/**
 *
 */
@Repository
public class TodoRepositoryPostgresqlImpl implements TodoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoRepositoryPostgresqlImpl.class);

    private JdbcTemplate jdbcTemplate;

    @Inject
    public TodoRepositoryPostgresqlImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo getTodo(String id) {
        LOGGER.debug("Getting todo id {} from database", id);
        String sql = "SELECT id, title, description FROM todo WHERE id = ?";
        RowMapper<Todo> mapper = new TodoRowMapper();
        Todo todo;
        try {
            todo = jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Todo not found for id ", id);
            throw new EntityFoundException("Todo not exist for id "+id, e);
        }

        return todo;
    }

    @Override
    public List<Todo> getTodos(TodoQuery query) {
        LOGGER.debug("Listing todos from database");
        return jdbcTemplate.query("SELECT id, title, description FROM todo LIMIT ? OFFSET ?"
                ,new TodoRowMapper(), query.getSize(), query.getStart());
    }

    @Override
    public Todo saveTodo(Todo todo) {
        if ( todo.getId() == null ) {
            return create(todo);
        } else {
            return updateTodo(todo);
        }
    }

    @Override
    public void deleteTodo(String id) {
        LOGGER.info("Deleting todo id {} from database", id);
        String sql = "DELETE FROM todo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int count(TodoQuery query) {
        LOGGER.debug("Counting todo in database");
        return jdbcTemplate.queryForObject("SELECT count(id) FROM todo ", Integer.class);
    }

    private Todo create(Todo todo) {
        LOGGER.debug("Inserting new todo in database");
        todo.setId(KeyGenerator.generateKey());
        jdbcTemplate.update(new TodoStatementCreator(todo));
        LOGGER.info("New todo is created with id {}", todo.getId());
        return todo;
    }

    private Todo updateTodo(Todo todo) {
        LOGGER.info("Updating todo id {}", todo.getId());
        jdbcTemplate.update("UPDATE todo SET title = ?," +
                        " description = ?" +
                        " WHERE id = ?",
                todo.getTitle(),
                todo.getDescription(),
                todo.getId());
        return todo;
    }

    private class TodoRowMapper implements RowMapper<Todo> {

        @Override
        public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Todo todo = new Todo();
            todo.setId(rs.getString("id"));
            todo.setTitle(rs.getString("title"));
            todo.setDescription(rs.getString("description"));

            return todo;
        }
    }

    private static class TodoStatementCreator implements PreparedStatementCreator {
        private final Todo model;

        TodoStatementCreator(Todo model) {
            this.model = model;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException {

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO todo (id, title, description)" +
                            " VALUES (?, ?, ?)");
            preparedStatement.setString(1, model.getId());
            preparedStatement.setString(2, model.getTitle());
            preparedStatement.setString(3, model.getDescription());

            return preparedStatement;
        }
    }
}
