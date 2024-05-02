package com.example.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.exception.request.NotFoundException;
import com.example.exception.request.StaleUpdateException;
import com.example.model.Model;
import com.example.repository.sql.Operation;
import com.example.repository.sql.SQLQueryLoader;

/*
 * Standard Abstract class providing implementation of (findById, findAll, save)
 * K: class for repository
 * V: type of table id
 * 
 * Standard Name for tables:
 * <tableName>/select_<tableName>_by_id.sql
 * <tableName>/select_<tableName>_all.sql
 * <tableName>/insert_to_<tableName>.sql
 * <tableName>/update_<tableName>_by_id.sql
 * <tableName>/delete_<tableName>_by_id.sql
 */
@Repository
public abstract class JdbcTable<K extends Model<K>, V> extends JdbcRepository {
    private Class<K> tableClass;

    public JdbcTable(Class<K> tableClass, String tableName, NamedParameterJdbcTemplate jdbcTemplate,
            SQLQueryLoader loader) {
        super(jdbcTemplate, loader, tableName);
        this.tableClass = tableClass;
    }

    public K findById(V id) throws NotFoundException {
        String sql = loadSqlFile(String.format("select_%s_by_id.sql", tableName));
        List<K> objects = jdbcTemplate.query(sql, new MapSqlParameterSource("id", id),
                new BeanPropertyRowMapper<>(tableClass));
        if (objects.isEmpty()) {
            throw new NotFoundException("No data found for id: " + id);
        }
        return objects.get(0);
    }

    public List<K> findAll() {
        String sql = loadSqlFile(String.format("select_%s_all.sql", tableName));
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(tableClass));
    }

    @Transactional(rollbackFor = { StaleUpdateException.class, RuntimeException.class })
    public void save(UUID userId, K oldValue, K newValue, Operation operation) throws StaleUpdateException {
        String sql = loadSqlFile(getOperationSqlFileName(operation));
        int updatedRows = jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(newValue) {
            @Override
            public Object getValue(@NonNull String paramName) throws IllegalArgumentException {
                Object value = super.getValue(paramName);

                // Return value.toString() if value is an Enum
                // (not defaultly support by the BeanPropertySqlParameterSource)
                if (value instanceof Enum) {
                    return value.toString();
                }
                return value;
            }
        });
        if (updatedRows == 0) {
            throw new StaleUpdateException("No rows affected while performing " + operation + " operation");
        }
        log(userId, oldValue, newValue, operation, updatedRows);
    }

    private void log(UUID userId, K oldValue, K newValue, Operation operation, int columnAffect)
            throws StaleUpdateException {
        String sql = loadSqlFile(tableName + "_log.sql");

        ObjectMapper mapper = new ObjectMapper();
        MapSqlParameterSource in = new MapSqlParameterSource();
        try {
            in.addValue("userId", userId);
            in.addValue("operation", operation.name());
            in.addValue("id", newValue.getId());
            in.addValue("columnAffect", columnAffect);
            in.addValue("newValue", mapper.writeValueAsString(newValue));

            if (operation == Operation.INSERT) {
                in.addValue("oldValue", "{}");
            } else {
                if (!oldValue.getId().equals(newValue.getId())) {
                    throw new RuntimeException("ID miss match: " + oldValue.getId() + " != " + newValue.getId());
                }
                in.addValue("oldValue", mapper.writeValueAsString(oldValue));
            }

            int updatedRows = jdbcTemplate.update(sql, in);

            if (updatedRows == 0) {
                throw new StaleUpdateException("No rows affected while performing " + operation + " operation");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Convert Object to JSON error: " + oldValue.getId());
        }

    }

    private String getOperationSqlFileName(Operation operation) {
        switch (operation) {
            case INSERT:
                return "insert_to_" + tableName + ".sql";
            case UPDATE:
                return "update_" + tableName + "_by_id.sql";
            case DELETE:
                return "delete_" + tableName + "_by_id.sql";
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }
}
