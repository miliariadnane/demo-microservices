package dev.nano.payment;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentEntity toEntity(PaymentDTO payment);
    List<PaymentEntity> toListEntity(List<PaymentDTO> paymentDTOList);
    PaymentDTO toDTO(PaymentEntity entity);
    List<PaymentDTO> toListDTO(List<PaymentEntity> paymentEntityList);
}
