package ru.zettatech.productservice.filter;

public record CategoryFilter(
        String sortBy,
        String sortOrder,
        Integer pageNumber,
        Integer pageSize
) {
    public CategoryFilter {
        if (sortBy == null) sortBy = "name";
        if (sortOrder == null) sortOrder = "asc";
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 10;
    }
}
