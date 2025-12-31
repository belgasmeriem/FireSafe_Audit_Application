package org.xproce.firesafe_audit.service.notification;

import org.xproce.firesafe_audit.dao.entities.*;
import org.xproce.firesafe_audit.dao.enums.TypeNotification;
import org.xproce.firesafe_audit.dto.common.NotificationDTO;
import java.util.List;

public interface INotificationService {
    List<NotificationDTO> getNotificationsByUser(Long userId);
    List<NotificationDTO> getUnreadNotificationsByUser(Long userId);
    long countUnreadByUser(Long userId);
    NotificationDTO createNotification(User destinataire, String titre, String message, TypeNotification type, String lien);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    void deleteNotification(Long id);
    void notifyAuditPlanifie(Audit audit);
    void notifyAuditTermine(Audit audit);
    void notifyNonConformiteCritique(EvaluationCritere evaluation);
}