package services;

import interfaces.IProjectService;
import models.Officer;
import models.Project;

public class ProjectService implements IProjectService {
    //
//    public boolean registerForOfficer(Officer officer, Project project) {
//        if (project.getOfficerSlots() == 0) {
//            return false;
//        } else {
//            project.setOfficerSlots(project.getOfficerSlots()-1);
//            return true;
//        }
//    }
    public boolean hasOfficerSlots(Project project) {
        return project.getOfficerSlots() >= 0;
    }

}
