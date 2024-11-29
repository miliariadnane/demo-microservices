package dev.nano.payment;

import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toDTO(PaymentEntity entity);
    PaymentEntity toEntity(PaymentDTO dto);
    List<PaymentDTO> toListDTO(List<PaymentEntity> entities);
}

