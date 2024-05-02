package com.example.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.repository.sql.SQLQueryLoader;

/*
 * Standard abstact class for all @Repository class
 */
public abstract class JdbcRepository {

    // Use for query, update actions
    protected final NamedParameterJdbcTemplate jdbcTemplate;
    // Use for loading sql string from file
    protected final SQLQueryLoader loader;

    protected final String tableName;

    public JdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, SQLQueryLoader loader, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        this.loader = loader;
        this.tableName = tableName;
    }

    protected String loadSqlFile(String fileName) {
        return loader.loadSQL(tableName + "/" + fileName);
    }
}
