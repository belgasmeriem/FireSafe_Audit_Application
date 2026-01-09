package org.xproce.firesafe_audit.service.audit;

import org.xproce.firesafe_audit.dao.entities.Critere;
import org.xproce.firesafe_audit.dto.evaluation.*;

import java.util.List;

public interface IEvaluationCritereService {


    List<EvaluationCritereDTO> getEvaluationsByAudit(Long auditId);

    EvaluationCritereDTO getEvaluationById(Long id);

    List<EvaluationCritereDTO> getNonConformitesByAudit(Long auditId);

    List<EvaluationCritereDTO> getNonConformitesCritiquesByAudit(Long auditId);

    List<EvaluationCritereDTO> getActionsNonCorrigees();

    List<EvaluationCritereDTO> getActionsNonCorrigeesByEtablissement(Long etablissementId);

    List<Critere> getCriteresByNorme(Long normeId);
    EvaluationCritereDTO getEvaluationByCritereAndAudit(Long critereId, Long auditId);


    List<Long> getNonConformeCritereIdsByAudit(Long auditId);


    EvaluationCritereDTO createEvaluation(EvaluationCreateDTO dto);

    EvaluationCritereDTO updateEvaluation(Long id, EvaluationUpdateDTO dto);

    void signalerCorrection(Long id);

    void deleteEvaluation(Long id);
}