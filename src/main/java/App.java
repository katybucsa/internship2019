import api.IEmployeeRepository;
import api.IValidator;
import model.Employee;
import repository.EmployeeRepository;
import repository.EmployeeValidator;
import service.Service;
import ui.Ui;

public class App {
    public static void main(String[] args) {
        String inputFile = "src\\main\\resources\\input.json";
        String outputFile = "src\\main\\resources\\output.json";
        IValidator<Employee> valid = new EmployeeValidator();
        IEmployeeRepository employeeRepo = new EmployeeRepository(inputFile,outputFile, valid);
        Service service = new Service(employeeRepo);
        Ui ui = new Ui(service);
        ui.run();
    }
}
