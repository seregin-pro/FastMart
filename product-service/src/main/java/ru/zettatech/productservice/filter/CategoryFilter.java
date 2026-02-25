package ru.zettatech.productservice.filter;

public record CategoryFilter(
        Integer pageNumber,
        Integer pageSize
) { }