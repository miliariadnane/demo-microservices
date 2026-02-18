package dev.nano.order.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderViewRepository extends JpaRepository<OrderViewEntity, Long> {
    List<OrderViewEntity> findByCustomerId(Long customerId);
}
