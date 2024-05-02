package com.example.repository.sql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class SQLQueryLoader {

    public static class SQLFileNotFoundException extends RuntimeException {
        public SQLFileNotFoundException(String message) {
            super(message);
        }
    }

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Load sql query string from file
     *
     * @param filePath default to folder in sql/, file path in sql/
     * @return complete sql command from .sql file specified
     */
    public String loadSQL(String filePath) throws SQLFileNotFoundException {
        Resource resource = resourceLoader.getResource("classpath:sql/" + filePath);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SQLFileNotFoundException("Failed to load SQL query from file: " + filePath);
        }
    }
}
