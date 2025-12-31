package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Notification;
import org.xproce.firesafe_audit.dto.common.NotificationDTO;

@Component
public class NotificationMapper {

    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) return null;

        return NotificationDTO.builder()
                .id(notification.getId())
                .titre(notification.getTitre())
                .message(notification.getMessage())
                .type(notification.getType())
                .dateCreation(notification.getDateCreation())
                .lu(notification.getLu())
                .dateLecture(notification.getDateLecture())
                .lien(notification.getLien())
                .build();
    }
}