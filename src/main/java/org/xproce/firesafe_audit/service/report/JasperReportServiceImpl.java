package org.xproce.firesafe_audit.service.report;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dao.entities.EvaluationCritere;
import org.xproce.firesafe_audit.dao.repositories.AuditRepository;
import org.xproce.firesafe_audit.dao.repositories.EvaluationCritereRepository;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JasperReportServiceImpl implements IReportService {

    private final AuditRepository auditRepository;
    private final EvaluationCritereRepository evaluationRepository;

    @Value("${app.reports.path:reports/}")
    private String reportsPath;

    @Value("${app.reports.logo:images/logo.png}")
    private String logoPath;

    @Override
    public byte[] generateAuditReport(Long auditId) throws Exception {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        Map<String, Object> parameters = prepareParameters(audit);

        List<EvaluationDTO> evaluations = evaluationRepository.findByAuditIdOrderByCritereCodeAsc(auditId)
                .stream()
                .map(this::toEvaluationDTO)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(evaluations);

        InputStream reportStream = new ClassPathResource("reports/audit-complet.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateConsolidatedReport(List<Long> auditIds) throws Exception {
        List<Audit> audits = auditRepository.findAllById(auditIds);

        if (audits.isEmpty()) {
            throw new RuntimeException("Aucun audit trouvé");
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nombreAudits", audits.size());
        parameters.put("dateGeneration", java.time.LocalDate.now());

        Double tauxMoyen = audits.stream()
                .filter(a -> a.getTauxConformite() != null)
                .mapToDouble(Audit::getTauxConformite)
                .average()
                .orElse(0.0);

        parameters.put("tauxMoyenConformite", tauxMoyen);

        List<AuditSummaryDTO> summaries = audits.stream()
                .map(this::toAuditSummaryDTO)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(summaries);

        InputStream reportStream = new ClassPathResource("reports/rapport-consolide.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public byte[] generateEtablissementReport(Long etablissementId) throws Exception {
        List<Audit> audits = auditRepository.findByEtablissementIdOrderByDateAuditDesc(etablissementId);

        if (audits.isEmpty()) {
            throw new RuntimeException("Aucun audit trouvé pour cet établissement");
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("etablissementNom", audits.get(0).getEtablissement().getNom());
        parameters.put("etablissementType", audits.get(0).getEtablissement().getType().toString());
        parameters.put("nombreAudits", audits.size());

        Double tauxMoyen = auditRepository.findTauxMoyenByEtablissement(etablissementId);
        parameters.put("tauxMoyenConformite", tauxMoyen != null ? tauxMoyen : 0.0);

        List<AuditSummaryDTO> summaries = audits.stream()
                .map(this::toAuditSummaryDTO)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(summaries);

        InputStream reportStream = new ClassPathResource("reports/rapport-etablissement.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private Map<String, Object> prepareParameters(Audit audit) throws Exception {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("auditId", audit.getId());
        parameters.put("etablissementNom", audit.getEtablissement().getNom());
        parameters.put("etablissementType", audit.getEtablissement().getType().toString());
        parameters.put("etablissementAdresse", audit.getEtablissement().getAdresse());
        parameters.put("etablissementVille", audit.getEtablissement().getVille());
        parameters.put("dateAudit", audit.getDateAudit());
        parameters.put("auditeurNom", audit.getAuditeur().getNomComplet());
        parameters.put("typeAudit", audit.getType().toString());
        parameters.put("tauxConformite", audit.getTauxConformite() != null ? audit.getTauxConformite() : 0.0);
        parameters.put("nbConformes", audit.getNbConformes() != null ? audit.getNbConformes() : 0);
        parameters.put("nbNonConformes", audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0);
        parameters.put("nbPartiels", audit.getNbPartiels() != null ? audit.getNbPartiels() : 0);
        parameters.put("observationGenerale", audit.getObservationGenerale());

        try {
            InputStream logoStream = new ClassPathResource(logoPath).getInputStream();
            parameters.put("logoPath", logoStream);
        } catch (Exception e) {
            parameters.put("logoPath", null);
        }

        BufferedImage chartImage = generatePieChart(audit);
        parameters.put("chartImage", chartImage);

        return parameters;
    }

    private BufferedImage generatePieChart(Audit audit) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        int conformes = audit.getNbConformes() != null ? audit.getNbConformes() : 0;
        int nonConformes = audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0;
        int partiels = audit.getNbPartiels() != null ? audit.getNbPartiels() : 0;

        if (conformes > 0) {
            dataset.setValue("Conformes (" + conformes + ")", conformes);
        }
        if (nonConformes > 0) {
            dataset.setValue("Non conformes (" + nonConformes + ")", nonConformes);
        }
        if (partiels > 0) {
            dataset.setValue("Partiels (" + partiels + ")", partiels);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Répartition des conformités",
                dataset,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(java.awt.Color.WHITE);

        return chart.createBufferedImage(400, 300);
    }

    private EvaluationDTO toEvaluationDTO(EvaluationCritere eval) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setCritereCode(eval.getCritere().getCode());
        dto.setCritereLibelle(eval.getCritere().getLibelle());
        dto.setCritereCategorie(eval.getCritere().getCategorie().toString());
        dto.setCritereCriticite(eval.getCritere().getCriticite().toString());
        dto.setStatut(eval.getStatut() != null ? eval.getStatut().toString() : "");
        dto.setObservation(eval.getObservation());
        dto.setActionCorrective(eval.getActionCorrective());
        dto.setResponsableAction(eval.getResponsableAction());

        if (eval.getPreuves() != null && !eval.getPreuves().isEmpty()) {
            dto.setPhotoPath("uploads/" + eval.getPreuves().iterator().next().getCheminFichier());
        }

        return dto;
    }

    private AuditSummaryDTO toAuditSummaryDTO(Audit audit) {
        AuditSummaryDTO dto = new AuditSummaryDTO();
        dto.setId(audit.getId());
        dto.setEtablissementNom(audit.getEtablissement().getNom());
        dto.setDateAudit(audit.getDateAudit());
        dto.setAuditeurNom(audit.getAuditeur().getNomComplet());
        dto.setTypeAudit(audit.getType().toString());
        dto.setStatut(audit.getStatut().toString());
        dto.setTauxConformite(audit.getTauxConformite() != null ? audit.getTauxConformite() : 0.0);
        dto.setNbConformes(audit.getNbConformes() != null ? audit.getNbConformes() : 0);
        dto.setNbNonConformes(audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0);
        return dto;
    }

    public static class EvaluationDTO {
        private String critereCode;
        private String critereLibelle;
        private String critereCategorie;
        private String critereCriticite;
        private String statut;
        private String observation;
        private String actionCorrective;
        private String responsableAction;
        private String photoPath;

        public String getCritereCode() { return critereCode; }
        public void setCritereCode(String critereCode) { this.critereCode = critereCode; }

        public String getCritereLibelle() { return critereLibelle; }
        public void setCritereLibelle(String critereLibelle) { this.critereLibelle = critereLibelle; }

        public String getCritereCategorie() { return critereCategorie; }
        public void setCritereCategorie(String critereCategorie) { this.critereCategorie = critereCategorie; }

        public String getCritereCriticite() { return critereCriticite; }
        public void setCritereCriticite(String critereCriticite) { this.critereCriticite = critereCriticite; }

        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }

        public String getObservation() { return observation; }
        public void setObservation(String observation) { this.observation = observation; }

        public String getActionCorrective() { return actionCorrective; }
        public void setActionCorrective(String actionCorrective) { this.actionCorrective = actionCorrective; }

        public String getResponsableAction() { return responsableAction; }
        public void setResponsableAction(String responsableAction) { this.responsableAction = responsableAction; }

        public String getPhotoPath() { return photoPath; }
        public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    }

    public static class AuditSummaryDTO {
        private Long id;
        private String etablissementNom;
        private java.time.LocalDate dateAudit;
        private String auditeurNom;
        private String typeAudit;
        private String statut;
        private Double tauxConformite;
        private Integer nbConformes;
        private Integer nbNonConformes;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getEtablissementNom() { return etablissementNom; }
        public void setEtablissementNom(String etablissementNom) { this.etablissementNom = etablissementNom; }

        public java.time.LocalDate getDateAudit() { return dateAudit; }
        public void setDateAudit(java.time.LocalDate dateAudit) { this.dateAudit = dateAudit; }

        public String getAuditeurNom() { return auditeurNom; }
        public void setAuditeurNom(String auditeurNom) { this.auditeurNom = auditeurNom; }

        public String getTypeAudit() { return typeAudit; }
        public void setTypeAudit(String typeAudit) { this.typeAudit = typeAudit; }

        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }

        public Double getTauxConformite() { return tauxConformite; }
        public void setTauxConformite(Double tauxConformite) { this.tauxConformite = tauxConformite; }

        public Integer getNbConformes() { return nbConformes; }
        public void setNbConformes(Integer nbConformes) { this.nbConformes = nbConformes; }

        public Integer getNbNonConformes() { return nbNonConformes; }
        public void setNbNonConformes(Integer nbNonConformes) { this.nbNonConformes = nbNonConformes; }
    }
}