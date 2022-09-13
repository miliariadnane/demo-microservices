package dev.nano.payment;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString @SuperBuilder
@Entity(name = "Payment")
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @SequenceGenerator(
            name = "payment_sequence",
            sequenceName = "payment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_sequence"
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
            name = "order_id",
            nullable = false
    )
    private Long orderId;

    @Column(
            name = "create_at",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime createAt;
}
