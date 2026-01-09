
package org.xproce.firesafe_audit.service.report;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dao.repositories.AuditRepository;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JasperReportServiceImpl implements IReportService {

    private final AuditRepository auditRepository;

    @Override
    public byte[] generateRapportPDF(Long auditId) throws Exception {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("etablissementNom", audit.getEtablissement().getNom());
        parameters.put("etablissementType", audit.getEtablissement().getType().getLibelle());
        parameters.put("etablissementAdresse", audit.getEtablissement().getAdresseComplete());
        parameters.put("dateAudit", audit.getDateAudit());
        parameters.put("auditeurNom", audit.getAuditeur().getNomComplet());
        parameters.put("tauxConformite", audit.getTauxConformite() != null ? audit.getTauxConformite() : 0.0);

        InputStream reportStream = new ClassPathResource("reports/rapport-global.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}