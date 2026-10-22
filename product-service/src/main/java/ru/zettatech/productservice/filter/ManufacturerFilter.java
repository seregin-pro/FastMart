package ru.zettatech.productservice.filter;

public record ManufacturerFilter(
        String sortBy,
        String sortOrder,
        Integer pageNumber,
        Integer pageSize
) {
    public ManufacturerFilter {
        if (sortBy == null) sortBy = "name";
        if (sortOrder == null) sortOrder = "asc";
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 10;
    }
}
