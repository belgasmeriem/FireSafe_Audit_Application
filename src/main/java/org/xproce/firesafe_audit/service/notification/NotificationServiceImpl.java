package org.xproce.firesafe_audit.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.*;
import org.xproce.firesafe_audit.dao.enums.TypeNotification;
import org.xproce.firesafe_audit.dao.repositories.NotificationRepository;
import org.xproce.firesafe_audit.dto.common.NotificationDTO;
import org.xproce.firesafe_audit.dto.mapper.NotificationMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        return notificationRepository.findByDestinataireIdOrderByDateCreationDesc(userId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getUnreadNotificationsByUser(Long userId) {
        return notificationRepository.findNonLuesByDestinataire(userId).stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countUnreadByUser(Long userId) {
        return notificationRepository.countNonLuesByDestinataire(userId);
    }

    @Override
    @Transactional
    public NotificationDTO createNotification(User destinataire, String titre, String message, TypeNotification type, String lien) {
        Notification notification = Notification.builder()
                .titre(titre)
                .message(message)
                .type(type)
                .lien(lien)
                .destinataire(destinataire)
                .lu(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));

        notification.marquerCommeLu();
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.marquerToutesCommeLues(userId, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void notifyAuditPlanifie(Audit audit) {
        createNotification(
                audit.getAuditeur(),
                "Audit planifié",
                "Un audit a été planifié pour l'établissement " + audit.getEtablissement().getNom() +
                        " le " + audit.getDateAudit(),
                TypeNotification.AUDIT_PLANIFIE,
                "/audits/" + audit.getId()
        );
    }

    @Override
    @Transactional
    public void notifyAuditTermine(Audit audit) {
        createNotification(
                audit.getAuditeur(),
                "Audit terminé",
                "L'audit de l'établissement " + audit.getEtablissement().getNom() + " est terminé",
                TypeNotification.AUDIT_TERMINE,
                "/audits/" + audit.getId()
        );
    }

    @Override
    @Transactional
    public void notifyNonConformiteCritique(EvaluationCritere evaluation) {
        Audit audit = evaluation.getAudit();

        createNotification(
                audit.getAuditeur(),
                "Non-conformité critique détectée",
                "Une non-conformité critique a été détectée : " + evaluation.getCritere().getLibelle(),
                TypeNotification.NON_CONFORMITE_CRITIQUE,
                "/audits/" + audit.getId() + "/evaluations/" + evaluation.getId()
        );
    }
}