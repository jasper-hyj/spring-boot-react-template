package com.example.model;

import java.util.UUID;

public interface Model<T> {
    UUID getId();

    T clone();
}
