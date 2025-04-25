package interfaces;

import java.util.List;

import models.Applicant;
import models.Application;
import models.Project;
import models.User;
import models.enums.FlatType;

/**
 * Interface for applicant-related application logic.
 */
public interface IApplicantApplicationService {
    List<Project> getEligibleProjects(User user);
    List<Project> getEligibleProjects(User user, List<Project> allProjects);
    List<Application> getApplicationsByApplicant(Applicant applicant);
    boolean submitApplication(Applicant applicant, Project project, FlatType flatType);
    void withdrawApplication(Applicant applicant, String applicationId);
}
