package api;

import model.Employee;
import utils.EmployeeRights;

public interface IEmployeeRepository {
    /**
     * method to get the information about the employee
     * @return the employee information
     */
    Employee getEmployee();

    /**
     * saves the employee's rights to file
     * @param rights the employee's rights
     */
    void save(EmployeeRights rights);
}
