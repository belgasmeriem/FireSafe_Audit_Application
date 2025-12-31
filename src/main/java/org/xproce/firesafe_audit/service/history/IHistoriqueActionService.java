package org.xproce.firesafe_audit.service.history;

import org.xproce.firesafe_audit.dao.entities.HistoriqueAction;
import org.xproce.firesafe_audit.dao.entities.User;
import java.time.LocalDateTime;
import java.util.List;

public interface IHistoriqueActionService {
    List<HistoriqueAction> getHistoriqueByUser(Long userId);
    List<HistoriqueAction> getHistoriqueByEntite(String entite, Long entiteId);
    List<HistoriqueAction> getHistoriqueByPeriod(LocalDateTime debut, LocalDateTime fin);
    void logAction(User utilisateur, String action, String entite, Long entiteId, String description);
    void cleanOldHistory(int days);
}