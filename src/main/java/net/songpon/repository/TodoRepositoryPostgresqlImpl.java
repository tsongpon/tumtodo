package net.songpon.repository;

import net.songpon.domain.Todo;
import net.songpon.exception.EntityFoundException;
import net.songpon.query.TodoQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Inject
    public TodoRepositoryPostgresqlImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo getTodo(String id) {
        LOGGER.debug("Getting todo id {} from database", id);
        String sql = "SELECT id, title, description FROM todo WHERE id = :id";
        RowMapper<Todo> mapper = new TodoRowMapper();
        Todo todo;
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("id", id);
            todo = jdbcTemplate.queryForObject(sql, parameters, mapper);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("Todo not found for id ", id);
            throw new EntityFoundException("Todo not exist for id "+id, e);
        }

        return todo;
    }

    @Override
    public List<Todo> getTodos(TodoQuery query) {
        LOGGER.debug("Listing todos from database");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("limit", query.getSize());
        parameters.addValue("offset", query.getStart());
        StringBuilder whereClause = new StringBuilder();
        if (StringUtils.isNoneBlank(query.getTitle())) {
            whereClause.append(" WHERE title = :title ");
            parameters.addValue("title", query.getTitle());
        }

        String limitClause = " LIMIT :limit OFFSET :offset";
        StringBuilder sql = new StringBuilder("SELECT id, title, description FROM todo ");
        if(whereClause.length() > 0) {
            sql.append(whereClause.toString());
        }
        sql.append(limitClause);
        return jdbcTemplate.query(sql.toString()
                ,parameters ,new TodoRowMapper());
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
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        String sql = "DELETE FROM todo WHERE id = :id";
        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public int count(TodoQuery query) {
        LOGGER.debug("Counting todo in database");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("limit", query.getSize());
        parameters.addValue("offset", query.getStart());
        StringBuilder whereClause = new StringBuilder();
        if (StringUtils.isNoneBlank(query.getTitle())) {
            whereClause.append(" WHERE title = :title ");
            parameters.addValue("title", query.getTitle());
        }

        StringBuilder sql = new StringBuilder("SELECT count(id) FROM todo ");
        if(whereClause.length() > 0) {
            sql.append(whereClause.toString());
        }
        return jdbcTemplate.queryForObject( sql.toString(), parameters, Integer.class);
    }

    private Todo create(Todo todo) {
        LOGGER.debug("Inserting new todo in database");
        todo.setId(KeyGenerator.generateKey());
        String sql = "INSERT INTO todo (id, title, description) VALUES (:id, :title, :description)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("title", todo.getTitle());
        parameters.addValue("description", todo.getDescription());
        parameters.addValue("id", todo.getId());
        jdbcTemplate.update(sql, parameters);
        LOGGER.info("New todo is created with id {}", todo.getId());
        return todo;
    }

    private Todo updateTodo(Todo todo) {
        LOGGER.info("Updating todo id {}", todo.getId());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("title", todo.getTitle());
        parameters.addValue("description", todo.getDescription());
        parameters.addValue("id", todo.getId());
        jdbcTemplate.update("UPDATE todo SET title = :title," +
                        " description = :description" +
                        " WHERE id = :id", parameters);
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
}
