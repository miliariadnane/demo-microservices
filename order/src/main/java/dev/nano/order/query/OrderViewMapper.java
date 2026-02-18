package dev.nano.order.query;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderViewMapper {
    OrderViewDTO toDTO(OrderViewEntity entity);
    List<OrderViewDTO> toListDTO(List<OrderViewEntity> entities);
}
