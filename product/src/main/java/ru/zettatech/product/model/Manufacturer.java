package ru.zettatech.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;

    public Manufacturer() {
    }

    public Manufacturer(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Manufacturer(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
