package dev.nano.order;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity toEntity(OrderDTO dto);
    List<OrderEntity> toListEntity(List<OrderDTO> listDTO);
    OrderDTO toDTO(OrderEntity entity);
    List<OrderDTO> toListDTO(List<OrderEntity> listEntity);

}
