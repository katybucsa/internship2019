package ui;

import repository.RepositoryException;
import repository.ValidationException;
import service.Service;

public class Ui {
    private Service service;

    public Ui(Service service) {
        this.service = service;
    }

    public void run() {
        try {
            service.calculateEmployeeHolidayRights();
        } catch (RepositoryException ex) {
            service.saveErrorMessage(ex.getMessage());
        } catch (ValidationException ex) {
            service.saveErrorMessage(ex.getMessage());
        }
    }
}
