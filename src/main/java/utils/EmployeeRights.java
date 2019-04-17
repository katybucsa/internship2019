package utils;

import java.util.Map;

public class EmployeeRights {
    private String errorMessage;
    private Map<Integer, Integer> rights;

    public EmployeeRights(String errorMessage, Map<Integer, Integer> rights) {
        this.errorMessage = errorMessage;
        this.rights = rights;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<Integer, Integer> getRights() {
        return rights;
    }
}
