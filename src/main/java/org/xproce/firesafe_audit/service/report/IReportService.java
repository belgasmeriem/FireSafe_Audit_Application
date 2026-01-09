package org.xproce.firesafe_audit.service.report;

public interface IReportService {
    byte[] generateRapportPDF(Long auditId) throws Exception;
}