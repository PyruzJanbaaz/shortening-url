package com.pyruz.shortening.model.dto.base;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionDTO(String error, List<String> messages, LocalDateTime dateTime) {
    public ExceptionDTO(String error, List<String> messages) {
        this(error, messages, LocalDateTime.now());
    }
}