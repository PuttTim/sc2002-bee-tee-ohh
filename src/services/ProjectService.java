package services;

import java.util.List;

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

    @Override
    public List<Project> findHandledProjects(Officer officer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findHandledProjects'");
    }

}
