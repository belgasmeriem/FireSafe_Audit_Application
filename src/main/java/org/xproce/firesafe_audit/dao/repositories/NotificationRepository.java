package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Notification;
import org.xproce.firesafe_audit.dao.enums.TypeNotification;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByDestinataireId(Long destinataireId);

    List<Notification> findByDestinataireIdOrderByDateCreationDesc(Long destinataireId);

    List<Notification> findByDestinataireIdAndLuFalse(Long destinataireId);

    List<Notification> findByDestinataireIdAndLuFalseOrderByDateCreationDesc(Long destinataireId);

    List<Notification> findByType(TypeNotification type);

    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :destinataireId " +
            "AND n.lu = false ORDER BY n.dateCreation DESC")
    List<Notification> findNonLuesByDestinataire(@Param("destinataireId") Long destinataireId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataire.id = :destinataireId AND n.lu = false")
    long countNonLuesByDestinataire(@Param("destinataireId") Long destinataireId);

    @Modifying
    @Query("UPDATE Notification n SET n.lu = true, n.dateLecture = :dateLecture " +
            "WHERE n.destinataire.id = :destinataireId AND n.lu = false")
    int marquerToutesCommeLues(@Param("destinataireId") Long destinataireId,
                               @Param("dateLecture") LocalDateTime dateLecture);

    @Query("SELECT n FROM Notification n WHERE n.dateCreation < :date")
    List<Notification> findOlderThan(@Param("date") LocalDateTime date);

    void deleteByDestinataireIdAndLuTrueAndDateCreationBefore(Long destinataireId, LocalDateTime date);
}