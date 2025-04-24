package services;

import models.Manager;
import models.Project;
import models.Registration;
import models.enums.RegistrationStatus;
import repositories.ProjectRepository;
import repositories.RegistrationRepository;
import views.CommonView;

import java.util.List;
import java.util.stream.Collectors;

public class RegistrationService {

    public static List<Registration> getProjectRegistrations(Project project) {
        return RegistrationRepository.getByProject(project.getProjectID());
    }

    public static List<Registration> getPendingProjectRegistrations(Project project) {
        return RegistrationRepository.getByProject(project.getProjectID()).stream()
                .filter(r -> r.getRegistrationStatus() == RegistrationStatus.PENDING)
                .collect(Collectors.toList());
    }

    public static boolean approveRegistration(Registration registration, Manager manager) {
        Project project = ProjectRepository.getById(registration.getProjectID());
        if (project == null) {
            CommonView.displayError("Project not found for this registration.");
            return false;
        }

        if (project.getOfficers().size() >= project.getOfficerSlots()) {
             CommonView.displayError("No available officer slots in project: " + project.getProjectName());
             return false;
        }

        try {
            // CommonView.displayError("ERROR 0");
            registration.approve(manager);
            // CommonView.displayError("ERROR 1");
            project.addOfficer(registration.getOfficer().getUserNRIC()); 

            // CommonView.displayError("ERROR 2");

            // ProjectRepository.getById(
            //     registration.getProjectID()
            // ).getOfficers().forEach(
            //     officer -> CommonView.displayMessage("Officer " + officer + project.getProjectName())
            // ); 


            RegistrationRepository.update(registration);
            ProjectRepository.update(project);
            return true;
        } catch (Exception e) {
            // CommonView.displayError("Error approving registration: " + e.getMessage());
            return true;
        }
    }

    public static boolean rejectRegistration(Registration registration, Manager manager) {
         try {
            registration.reject(manager);

            RegistrationRepository.update(registration);
            return true;
        } catch (Exception e) {
            CommonView.displayError("Error rejecting registration: " + e.getMessage());
            return false;
        }
    }
}