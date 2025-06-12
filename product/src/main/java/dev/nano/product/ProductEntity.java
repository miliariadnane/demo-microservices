package dev.nano.product;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString @SuperBuilder
@Entity(name = "Product")
@Table(name = "product")
public class ProductEntity {

    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "image",
            columnDefinition = "TEXT"
    )
    private String image;

    @Column(
            name = "price",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private Integer price;

    @Column(
            name = "available_quantity",
            nullable = false,
            columnDefinition = "INTEGER DEFAULT 0"
    )
    private Integer availableQuantity;
}
