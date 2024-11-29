package dev.nano.customer;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDTO(CustomerEntity entity);
    CustomerEntity toEntity(CustomerDTO dto);
    List<CustomerDTO> toListDTO(List<CustomerEntity> listEntity);
    void updateCustomerFromDTO(CustomerDTO dto, @MappingTarget CustomerEntity entity);
}
