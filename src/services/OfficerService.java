package services;

import interfaces.IOfficerService;

import models.Officer;
import repositories.OfficerRepository;

public class OfficerService {
    public static boolean hasExistingProject(Officer officer) {
        return OfficerRepository.hasExistingProject(officer);
    }

    public static boolean hasExistingRegistration(Officer officer) {
        // TODO: Implement this
        return false;
    }

    public static void setOfficerRegistration(Officer officer) {
        // TODO: Implement this
    }
}
