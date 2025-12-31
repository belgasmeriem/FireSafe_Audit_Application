package org.xproce.firesafe_audit.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeNotification;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long id;

    private String titre;

    private String message;

    private TypeNotification type;

    private LocalDateTime dateCreation;

    private Boolean lu;

    private LocalDateTime dateLecture;

    private String lien;
}