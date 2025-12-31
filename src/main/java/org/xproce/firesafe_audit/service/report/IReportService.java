package org.xproce.firesafe_audit.service.report;

import java.util.List;

public interface IReportService {
    byte[] generateAuditReport(Long auditId) throws Exception;
    byte[] generateConsolidatedReport(List<Long> auditIds) throws Exception;
    byte[] generateEtablissementReport(Long etablissementId) throws Exception;
}