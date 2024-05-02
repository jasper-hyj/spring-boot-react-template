package com.example.security.role;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.repository.JdbcRepository;
import com.example.repository.sql.SQLQueryLoader;
import com.example.security.role.model.Role;

@Repository
public class RoleRepository extends JdbcRepository {

    public RoleRepository(NamedParameterJdbcTemplate jdbcTemplate, SQLQueryLoader loader) {
        super(jdbcTemplate, loader, "roles");
    }

    /**
     * Get roles from database based on userId
     *
     * @param userId userId of the user
     * @return list of all roles the user have
     */
    public List<Role> findAllByUserId(UUID userId) {
        String sql = loadSqlFile("select_roles_list_by_user_id.sql");
        return jdbcTemplate.query(sql, new MapSqlParameterSource("id", userId),
                new BeanPropertyRowMapper<>(Role.class));
    }
}
