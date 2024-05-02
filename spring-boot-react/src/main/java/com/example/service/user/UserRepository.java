package com.example.service.user;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.exception.request.NotFoundException;
import com.example.repository.JdbcTable;
import com.example.repository.sql.SQLQueryLoader;
import com.example.service.user.model.User;

@Repository
public class UserRepository extends JdbcTable<User, UUID> {

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate, SQLQueryLoader loader) {
        super(User.class, "users", jdbcTemplate, loader);
    }

    /**
     * Find user by their email
     *
     * @param email email of the user
     * @return user query result (Nullable)
     * @throws NotFoundException
     * @throws DataAccessException
     */
    public User findByEmail(String email) throws NotFoundException {
        List<User> users = jdbcTemplate.query(loadSqlFile("select_users_by_email.sql"),
                new MapSqlParameterSource("email", email),
                new BeanPropertyRowMapper<>(User.class));
        if (users.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return users.get(0);
    }

    /**
     * Find user by their id
     *
     * @param publicId publicId of the user
     * @return user query result (Nullable)
     * @throws DataAccessException
     * @throws NotFoundException
     */
    public User findByPublicId(String publicId) throws NotFoundException {
        List<User> users = jdbcTemplate.query(loadSqlFile("select_users_by_public_id.sql"),
                new MapSqlParameterSource("publicId", publicId),
                new BeanPropertyRowMapper<>(User.class));
        if (users.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return users.get(0);
    }
}
