package ru.zettatech.productservice.filter;

import java.math.BigDecimal;

public record ProductFilter(
        String query,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Long categoryId,
        Long manufacturerId,
        String sortBy,
        String sortOrder,
        Integer pageNumber,
        Integer pageSize
) {
    public ProductFilter {
        if (query == null) query = "";
        if (sortBy == null) sortBy = "id";
        if (sortOrder == null) sortOrder = "asc";
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 10;
    }
}
