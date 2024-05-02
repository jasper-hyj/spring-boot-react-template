package com.example.security.authority;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.repository.JdbcRepository;
import com.example.repository.sql.SQLQueryLoader;
import com.example.security.authority.model.Authority;

/**
 * AuthorityRepository
 */
@Repository
public class AuthorityRepository extends JdbcRepository {

    public AuthorityRepository(NamedParameterJdbcTemplate jdbcTemplate, SQLQueryLoader loader) {
        super(jdbcTemplate, loader, "authorities");
    }

    /**
     * Get authorities from database based on userId
     *
     * @param userId userId of the user
     * @return list of all authorities the user have
     */
    public List<Authority> findAllByUserId(UUID userId) {
        String sql = loadSqlFile("select_authorities_list_by_user_id.sql");
        return jdbcTemplate.query(sql, new MapSqlParameterSource("id", userId),
                new BeanPropertyRowMapper<>(Authority.class));
    }
}