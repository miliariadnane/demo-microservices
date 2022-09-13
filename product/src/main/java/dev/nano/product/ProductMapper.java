package dev.nano.product;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductDTO dto);
    List<ProductEntity> toListEntity(List<ProductDTO> listDTO);
    ProductDTO toDTO(ProductEntity entity);
    List<ProductDTO> toListDTO(List<ProductEntity> listEntity);

}
