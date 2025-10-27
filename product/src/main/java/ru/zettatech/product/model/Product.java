package ru.zettatech.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sku;
    private int quantity;
    private String image;
    private String name;

    @Lob
    private String description;

    @ManyToOne @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
    private BigDecimal price;
    private BigDecimal weight;
    private byte status;
    private int viewed;

    @JsonProperty("date_added")
    private Date dateAdded;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="product_category",
            joinColumns=  @JoinColumn(name="product_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="category_id", referencedColumnName="id") )
    private Set<Category> categories = new HashSet<>();

    public Product() {
    }

    public Product(Long id, int sku, int quantity, String image, String name, String description, Manufacturer manufacturer, BigDecimal price, BigDecimal weight, byte status, int viewed, Date dateAdded) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.image = image;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.weight = weight;
        this.status = status;
        this.viewed = viewed;
        this.dateAdded = dateAdded;
    }

    public Product(int sku, int quantity, String image, String name, String description, Manufacturer manufacturer, BigDecimal price, BigDecimal weight, byte status, int viewed, Date dateAdded) {
        this.sku = sku;
        this.quantity = quantity;
        this.image = image;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.weight = weight;
        this.status = status;
        this.viewed = viewed;
        this.dateAdded = dateAdded;
    }
}
