package com.example.service.logging;

import java.sql.Timestamp;
import java.util.UUID;

import com.example.repository.sql.Operation;
import lombok.Data;

@Data
public class Log {
    private Timestamp timestamp;
    private UUID userId;

    private Operation operation;
    private UUID id;
    private Integer columnAffect;
    private String old_value;
    private String new_value;
}
