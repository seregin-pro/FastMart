package ru.zettatech.productservice.filter;

public record ProductFilter(
        Integer pageNumber,
        Integer pageSize
) { }
