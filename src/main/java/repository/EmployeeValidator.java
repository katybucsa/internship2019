package repository;

import api.IValidator;
import model.Employee;

import java.time.LocalDate;
import java.util.List;


public class EmployeeValidator implements IValidator<Employee> {
    @Override
    public void validate(Employee employee) {
        String err = "";

        if (employee.getEmploymentStartDate().compareTo(employee.getEmploymentEndDate()) >= 0) {
            err += "Invalid employment period!\n";
        }

        List<List<LocalDate>> periods = employee.getSuspensionPeriodList();
        for (int i = 0; i < periods.size(); i++) {
            LocalDate startDate = periods.get(i).get(0);
            LocalDate endDate = periods.get(i).get(1);
            if (startDate.isAfter(endDate) || startDate.isEqual(endDate)
                    || startDate.isBefore(employee.getEmploymentStartDate())
                    || endDate.isAfter(employee.getEmploymentEndDate())) {
                err += "Invalid suspension period: " + startDate + " - " + endDate + "\n";
            }
            else if (i > 0 && periods.get(i - 1).get(1).isAfter(startDate)) {
                err += "Invalid suspension period: " + startDate + " - " + endDate + "\n";
            }
        }
        if (!err.equals("")) {
            throw new ValidationException(err);
        }
    }
}
