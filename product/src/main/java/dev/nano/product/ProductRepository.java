package dev.nano.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value="SELECT * FROM product", nativeQuery=true)
    Page<ProductEntity> findAllProducts(Pageable pageableRequest);

    @Query(value="SELECT * FROM product p WHERE (p.name LIKE CONCAT('%', :search, '%')) ", nativeQuery=true)
    Page<ProductEntity> findAllProductsByCriteria(Pageable pageableRequest, @Param("search") String search);
}
