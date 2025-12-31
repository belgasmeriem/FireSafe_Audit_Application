package org.xproce.firesafe_audit.service.file;

import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dto.common.PreuveDTO;
import java.util.List;

public interface IPreuveService {
    List<PreuveDTO> getPreuvesByEvaluation(Long evaluationId);
    PreuveDTO getPreuveById(Long id);
    byte[] downloadPreuve(Long id);
    PreuveDTO uploadPreuve(Long evaluationId, MultipartFile file, String description);
    void deletePreuve(Long id);
    long countByEvaluation(Long evaluationId);
}