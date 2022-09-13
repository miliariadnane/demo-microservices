package dev.nano.customer;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

        CustomerEntity toEntity(CustomerDTO dto);
        List<CustomerEntity> toListEntity(List<CustomerDTO> listDTO);
        CustomerDTO toDTO(CustomerEntity entity);
        List<CustomerDTO> toListDTO(List<CustomerEntity> listEntity);

}
