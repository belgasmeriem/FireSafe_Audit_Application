package org.xproce.firesafe_audit.service.history;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.HistoriqueAction;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.repositories.HistoriqueActionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoriqueActionServiceImpl implements IHistoriqueActionService {

    private final HistoriqueActionRepository historiqueRepository;

    @Override
    public List<HistoriqueAction> getHistoriqueByUser(Long userId) {
        return historiqueRepository.findByUtilisateurIdOrderByDateActionDesc(userId);
    }

    @Override
    public List<HistoriqueAction> getHistoriqueByEntite(String entite, Long entiteId) {
        return historiqueRepository.findByEntiteAndEntiteId(entite, entiteId);
    }

    @Override
    public List<HistoriqueAction> getHistoriqueByPeriod(LocalDateTime debut, LocalDateTime fin) {
        return historiqueRepository.findByPeriod(debut, fin);
    }

    @Override
    @Transactional
    public void logAction(User utilisateur, String action, String entite, Long entiteId, String description) {
        HistoriqueAction historique = HistoriqueAction.builder()
                .utilisateur(utilisateur)
                .action(action)
                .entite(entite)
                .entiteId(entiteId)
                .description(description)
                .build();

        historiqueRepository.save(historique);
    }

    @Override
    @Transactional
    public void cleanOldHistory(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        historiqueRepository.deleteByDateActionBefore(cutoffDate);
    }
}