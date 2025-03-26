package services;

import models.Officer;
import models.Project;

public class ProjectService {
    //
    public boolean registerForOfficer(Officer officer, Project project) {
        if (project.getOfficerSlots() == 0) {
            System.out.println("Failed to register. Project has no more officer slots.");
            return false;
        } else {
            project.setOfficerSlots(project.getOfficerSlots()-1);
            return true;
        }
    }
}
