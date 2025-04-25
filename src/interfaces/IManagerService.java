package interfaces;

import java.util.List;
import java.util.Map;

import models.Project;

/**
 * Interface for manager-specific service operations.
 */
public interface IManagerService {
    /**
     * Generates a report on applicants for a given project, with specific filters.
     *
     * @param project The project for which the report is generated.
     * @param filters A map of filters to apply to the report data.
     * @return A list of applicant data entries, each represented as a map of field names to values.
     */
    List<Map<String, String>> generateApplicantReport(Project project, Map<String, String> filters);

    /**
     * Exports the given report data to a CSV file.
     *
     * @param reportData The report data to export.
     * @param filename The name of the CSV file to create.
     */
    void exportReportToCsv(List<Map<String, String>> reportData, String filename);

}
