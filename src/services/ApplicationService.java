package services;

import java.util.List;
import java.util.stream.Collectors;

import models.Application;
import models.Officer;
import models.Project;
import models.Receipt;
import models.enums.ApplicationStatus;
import repositories.ApplicationRepository;
import repositories.ProjectRepository;
import repositories.ReceiptRepository;
import views.CommonView;

public class ApplicationService {

    public static List<Application> getProjectApplications(Project project) {
        return ApplicationRepository.getAll().stream()
                .filter(app -> app.getProjectId().equals(project.getProjectID()))
                .collect(Collectors.toList());
    }

    public static List<Application> getSuccessfulProjectApplications(Project project) {
        return getProjectApplications(project).stream()
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL)
                .collect(Collectors.toList());
    }

    public static boolean approveApplication(Application application, Officer officer) {
        if (application == null || !application.canApprove()) {
            return false;
        }
        try {
            application.approve(officer.getUserNRIC());
            ApplicationRepository.saveAll();
            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error approving application: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during approval: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rejectApplication(Application application, Officer officer) {
        if (application == null || !application.canReject()) {
            return false;
        }
        try {
            application.reject(officer.getUserNRIC());
            ApplicationRepository.saveAll();
            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error rejecting application: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during rejection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean bookApplication(Application application, Officer officer, String selectedUnitNumber) { // Added unitNumber parameter
        if (application == null || !application.canBook()) {
            return false;
        }

        Project project = ProjectRepository.getById(application.getProjectId());
        List<Receipt> receipts = ReceiptRepository.getByProjectID(project.getProjectID());
        if (receipts.stream().anyMatch(r -> r.getUnitNumber().equals(selectedUnitNumber))) {
            CommonView.displayError("Unit number already booked. Please select a different unit.");
            return false;
        }

        if (project == null) {
            return false;
        }

        if (project.getAvailableUnits(application.getSelectedFlatType()) <= 0) {
            return false;
        }

        if (selectedUnitNumber == null || selectedUnitNumber.trim().isEmpty()) { // Basic validation for unit number
             System.err.println("Error booking application: Unit number cannot be empty.");
             return false;
        }

        try {
            project.reduceFlatCount(application.getSelectedFlatType());
            application.book();

            ProjectRepository.saveAll();
            ApplicationRepository.saveAll();

            return true;
        } catch (IllegalStateException e) {
            System.err.println("Error booking application: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
