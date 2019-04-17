package model;

import java.time.LocalDate;
import java.util.List;


public class Employee {
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
    private List<List<LocalDate>> suspensionPeriodList;

    public Employee(LocalDate employmentStartDate, LocalDate employmentEndDate, List<List<LocalDate>> suspensionPeriodList) {
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.suspensionPeriodList = suspensionPeriodList;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    public void setEmploymentEndDate(LocalDate employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
    }

    public List<List<LocalDate>> getSuspensionPeriodList() {
        return suspensionPeriodList;
    }

    public void setSuspensionPeriodList(List<List<LocalDate>> suspensionPeriodList) {
        this.suspensionPeriodList = suspensionPeriodList;
    }
}
