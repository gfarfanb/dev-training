package com.legadi.ui.vacations.service;

import static com.legadi.ui.vacations.common.ConfigConstants.BASE_YEAR;
import static com.legadi.ui.vacations.common.ConfigConstants.EMPLOYEE_NAME_FIRST_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.FILE_TO_ANALYZE_LOCATION;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_TAKEN_DAYS_COLUMN;
import static com.legadi.ui.vacations.common.Utils.isNumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.legadi.ui.vacations.common.CellRef;
import com.legadi.ui.vacations.common.CellValue;
import com.legadi.ui.vacations.common.ErrorMessage;
import com.legadi.ui.vacations.exception.VacationsBalanceException;
import com.legadi.ui.vacations.model.Employee;

@Service
public class EmployeeService {

    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final ConfigService configService;
    private final ErrorMessage errorMessage;

    public EmployeeService(ConfigService configService,
            ErrorMessage errorMessage) {
        this.configService = configService;
        this.errorMessage = errorMessage;
    }

    public List<Employee> getEmployees() {
        String balanceFile = configService.get(FILE_TO_ANALYZE_LOCATION);

        try (FileInputStream file = new FileInputStream(balanceFile);
                Workbook workbook = WorkbookFactory.create(file)) {
            Map<String, Employee> employees = getEmployeeAndTakenDays(workbook);

            return employees.values()
                .stream()
                .sorted(Comparator.comparing(Employee::getName))
                .toList();
        } catch (IOException ex) {
            throw new VacationsBalanceException(errorMessage.getReadBalanceFile(), balanceFile, ex);
        }
    }

    private Map<String, Employee> getEmployeeAndTakenDays(Workbook workbook) {
        CellRef employeeFirstCell = configService.getCell(EMPLOYEE_NAME_FIRST_CELL);
        CellRef totalTakenCol = configService.getCell(TOTAL_TAKEN_DAYS_COLUMN);
        int baseYear = configService.getInt(BASE_YEAR);
        
        CellValue cellValue = new CellValue(workbook);
        Map<String, Employee> employees = new HashMap<>();

        for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);

            if(isNumber(sheet.getSheetName())
                    && Integer.parseInt(sheet.getSheetName()) >= baseYear) {

                logger.debug("Reading sheet: {}", sheet.getSheetName());

                Map<String, Employee> employeesOnYear = findEmployees(cellValue,
                    employeeFirstCell, totalTakenCol, sheet);

                employeesOnYear.forEach((k, v) -> employees.merge(k, v, mergeEmployee()));

                logger.debug("Extracted names for sheet [{}]: {}", sheet.getSheetName(), employees.keySet());
            } else {
                logger.debug("Discarding sheet for employee name list: {}", sheet.getSheetName());
            }
        }

        return employees;
    }

    private Map<String, Employee> findEmployees(CellValue cellValue, CellRef employeeFirstCell,
            CellRef totalTakenCol, Sheet sheet) {
        int year = Integer.parseInt(sheet.getSheetName());
        return StreamSupport.stream(sheet.spliterator(), false)
            .filter(row -> row.getRowNum() >= employeeFirstCell.getRow())
            .filter(row -> hasTakenDays(cellValue, totalTakenCol, row))
            .map(row -> toTakenByEmployee(cellValue, employeeFirstCell, totalTakenCol, year, row))
            .collect(Collectors.toMap(Employee::getName, e -> e, mergeEmployee()));
    }

    private boolean hasTakenDays(CellValue cellValue, CellRef totalTakenCol, Row row) {
        return isNumber(cellValue.asString(row.getCell(totalTakenCol.getCol())));
    }

    private Employee toTakenByEmployee(CellValue cellValue, CellRef employeeFirstCell,
            CellRef totalTakenCol, int year, Row row) {
        String name = cellValue.asString(row.getCell(employeeFirstCell.getCol()));
        int totalTaken = cellValue.asInt(row.getCell(totalTakenCol.getCol()));
        Employee employee = new Employee();
        employee.setName(name);
        employee.getTakenByYear().put(year, totalTaken);
        return employee;
    }

    private BinaryOperator<Employee> mergeEmployee() {
        return (e1, e2) -> {
            if(StringUtils.isBlank(e1.getCompanyName())) {
                e1.setCompanyName(e2.getCompanyName());
            }
            if(e1.getStartDate() == null) {
                e1.setStartDate(e2.getStartDate());
            }
            if(e1.getPreviousVacationDays() != e2.getPreviousVacationDays()) {
                e1.setPreviousVacationDays(e2.getPreviousVacationDays());
            }
            if(e1.getBalanceDays() != e2.getBalanceDays()) {
                e1.setBalanceDays(e2.getBalanceDays());
            }
            if(e1.getRatioDays() != e2.getRatioDays()) {
                e1.setRatioDays(e2.getRatioDays());
            }
            e1.getAllowedByYear().putAll(e2.getAllowedByYear());
            e1.getTakenByYear().putAll(e2.getTakenByYear());
            return e1;
        };
    }
}
