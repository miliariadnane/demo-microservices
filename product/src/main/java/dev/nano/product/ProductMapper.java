package dev.nano.product;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(ProductEntity entity);
    ProductEntity toEntity(ProductDTO dto);
    List<ProductDTO> toListDTO(List<ProductEntity> entities);
    void updateProductFromDTO(ProductDTO dto, @MappingTarget ProductEntity entity);
}

