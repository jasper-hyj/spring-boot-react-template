package com.example.exception.request;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QueryRejectedException extends Exception {

    public QueryRejectedException(String message) {
        super(message);
    }
}
