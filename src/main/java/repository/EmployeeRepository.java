package repository;

import api.IEmployeeRepository;
import api.IValidator;
import model.Employee;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.EmployeeRights;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeRepository implements IEmployeeRepository {
    private Employee employee;
    private String inputFile;
    private String outputFile;
    private IValidator<Employee> validator;

    public EmployeeRepository(String inputFile, String outputFile, IValidator<Employee> validator) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.validator = validator;
        readFromFile();
    }

    /**
     * method responsible to read data from file
     */
    private void readFromFile() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonobj = (JSONObject) parser.parse(new FileReader(inputFile));
            JSONObject employeeData = (JSONObject) jsonobj.get("employeeData");

            LocalDate start = LocalDate.parse(employeeData.get("employmentStartDate").toString());
            LocalDate end = LocalDate.parse(employeeData.get("employmentEndDate").toString());

            JSONArray suspensionPeriodJSONList = (JSONArray) employeeData.get("suspensionPeriodList");
            List<List<LocalDate>> suspensionPeriodList = new ArrayList<>();
            for (Object x : suspensionPeriodJSONList) {
                JSONObject y = (JSONObject) x;
                LocalDate startDate = LocalDate.parse(y.get("startDate").toString());
                LocalDate endDate = LocalDate.parse(y.get("endDate").toString());
                List<LocalDate> suspensionPeriod = new ArrayList<>();
                suspensionPeriod.add(startDate);
                suspensionPeriod.add(endDate);
                suspensionPeriodList.add(suspensionPeriod);
            }

            List<List<LocalDate>> sortedList = suspensionPeriodList.stream()
                    .sorted((period1, period2) -> period1.get(0).isAfter(period2.get(0)) ? 1 : -1)
                    .collect(Collectors.toList());

            Employee emp = new Employee(start, end, sortedList);
            validator.validate(emp);
            this.employee = emp;
        } catch (ParseException e) {
            throw new RepositoryException("Error parsing input file!\n");
        } catch (FileNotFoundException e) {
            throw new RepositoryException("Input file couldn't be found!\n");
        } catch (IOException e) {
            throw new RepositoryException("Error when trying to read from file!\n");
        }
    }

    /**
     * method responsible to write data to file
     *
     * @param rights the employee's rights that will be written to file
     */
    private void writeToFile(EmployeeRights rights) {
        JSONObject output = new JSONObject();
        output.put("errorMessage", rights.getErrorMessage());
        JSONArray rightsList = new JSONArray();
        for (Map.Entry<Integer, Integer> e : rights.getRights().entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("year", e.getKey());
            obj.put("holidayDays", e.getValue());
            rightsList.add(obj);
        }
        output.put("holidayRightsPerYearList", rightsList);
        try (StringWriter out = new StringWriter();
             PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {

            output.writeJSONString(out);
            String jsonText = out.toString();
            pw.println(jsonText);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Employee getEmployee() {
        return this.employee;
    }

    @Override
    public void save(EmployeeRights rights) {
        this.writeToFile(rights);
    }
}
