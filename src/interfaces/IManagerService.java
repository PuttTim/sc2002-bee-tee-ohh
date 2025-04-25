package interfaces;

import java.util.List;
import java.util.Map;

import models.Project;

/**
 * Interface for manager-specific service operations.
 */
public interface IManagerService {
    List<Map<String, String>> generateApplicantReport(Project project, Map<String, String> filters);
    void exportReportToCsv(List<Map<String, String>> reportData, String filename);
}
