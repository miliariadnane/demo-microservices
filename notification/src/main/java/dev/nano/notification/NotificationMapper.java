package dev.nano.notification;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationEntity toEntity(NotificationDTO dto);
    List<NotificationEntity> toListEntity(List<NotificationDTO> listDTO);
    NotificationDTO toDTO(NotificationEntity entity);
    List<NotificationDTO> toListDTO(List<NotificationEntity> listEntity);
}
