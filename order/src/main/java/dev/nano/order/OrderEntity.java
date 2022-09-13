package dev.nano.order;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString @SuperBuilder
@Entity(name = "Order")
@Table(name = "orders")
public class OrderEntity {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "customer_id",
            nullable = false
    )
    private Long customerId;

    @Column(
            name = "product_id",
            nullable = false
    )
    private Long productId;

    @Column(
            name = "amount",
            nullable = false
    )
    private double amount;

    @Column(
            name = "create_at",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime createAt;
}
