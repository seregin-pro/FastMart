package ru.zettatech.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String name;
    @JoinColumn(name = "parent_id")
    private Long parentId;
    private byte status;

    public Category() {
    }

    public Category(Long id, String image, String name, Long parentId, byte status) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.parentId = parentId;
        this.status = status;
    }

    public Category(String image, String name, Long parentId, byte status) {
        this.image = image;
        this.name = name;
        this.parentId = parentId;
        this.status = status;
    }
}
