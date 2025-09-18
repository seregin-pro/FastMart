package ru.zettatech.product.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String message,
    String errorMessage,
    LocalDateTime errorTime
) {}
