package service;

import api.IEmployeeRepository;
import model.Employee;
import utils.EmployeeRights;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Service {
    private IEmployeeRepository employeeRepo;

    public Service(IEmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }


    /**
     * @param start the date of start counting days
     * @param end   the date of end counting days
     * @return a map having as key a year and as value
     * the number of days for that year that are between start and end date
     */
    private Map<Integer, Integer> calculateDaysPerYear(LocalDate start, LocalDate end) {
        Map<Integer, Integer> map = new TreeMap<>();
        int days;

        if (start.getYear() == end.getYear()) {
            days = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(start, end))) + 1;
            map.put(start.getYear(), days);
            return map;
        }

        //get the number of days until the end of the employment year starting
        //with the employment date
        days = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(start,
                LocalDate.of(start.getYear(), 12, 31)))) + 1;
        map.put(start.getYear(), days);


        //get the number of days for the years mentioned in the contract excepting
        //employment start year and employment end year
        for (int year = start.getYear() + 1; year < end.getYear(); year++) {
            days = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(
                    LocalDate.of(year, 1, 1),
                    LocalDate.of(year, 12, 31)))) + 1;
            map.put(year, days);
        }

        //get the number of contractual days in the employment end year
        days = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(
                LocalDate.of(end.getYear(), 1, 1), end))) + 1;
        map.put(end.getYear(), days);

        return map;
    }

    /**
     * @param periods the suspension periods for the employee
     * @return a map containing the years in which the employee
     * has suspension periods together with the number of suspension days for each year
     */
    private Map<Integer, Integer> calculateSuspendedDaysPerYear(List<List<LocalDate>> periods) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (List<LocalDate> period : periods) {
            Map<Integer, Integer> periodDays = calculateDaysPerYear(period.get(0), period.get(1));
            for (Map.Entry<Integer, Integer> e : periodDays.entrySet()) {
                int year = e.getKey();
                int days = e.getValue();
                if (map.containsKey(year)) {
                    map.put(year, days + map.get(year));
                    continue;
                }
                map.put(year, days);
            }
        }
        return map;
    }

    /**
     * method to calculate the employee holiday rights for each contractual year
     */
    public void calculateEmployeeHolidayRights() {
        Employee emp = employeeRepo.getEmployee();
        Map<Integer, Integer> totalDaysPerYear = calculateDaysPerYear(emp.getEmploymentStartDate()
                , emp.getEmploymentEndDate());

        Map<Integer, Integer> daysSuspendedPerYear = calculateSuspendedDaysPerYear(emp.getSuspensionPeriodList());

        Map<Integer, Integer> holidayRightsPerYear = new TreeMap<>();

        int holidayDays = 20;
        for (Map.Entry<Integer, Integer> e : totalDaysPerYear.entrySet()) {
            int year = e.getKey();
            int daysInYear = Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(
                    LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)))) + 1;
            int totalDays = e.getValue();
            int suspendedDays = 0;
            if (daysSuspendedPerYear.containsKey(year)) {
                suspendedDays = daysSuspendedPerYear.get(year);
            }
            int holidayRights = Math.round((float) ((totalDays - suspendedDays) * holidayDays) / daysInYear);

            holidayRightsPerYear.put(year, holidayRights);
            if (holidayDays < 24) {
                holidayDays += 1;
            }
        }
        EmployeeRights rights = new EmployeeRights("", holidayRightsPerYear);
        employeeRepo.save(rights);
    }

    /**
     * if an exception occurs and the holiday rights can not
     * be calculated, an error  message will be saved
     *
     * @param err the error message
     */
    public void saveErrorMessage(String err) {
        EmployeeRights rights = new EmployeeRights(err, new TreeMap<>());
        employeeRepo.save(rights);
    }
}
