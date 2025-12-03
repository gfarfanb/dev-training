package com.legadi.ui.vacations.service;

import static com.legadi.ui.vacations.common.ConfigConstants.BALANCE_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.BASE_YEAR;
import static com.legadi.ui.vacations.common.ConfigConstants.COMPANY_NAME_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.EMPLOYEE_NAME_FIRST_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.FILE_TO_ANALYZE_LOCATION;
import static com.legadi.ui.vacations.common.ConfigConstants.PREVIOUS_VACATIONS_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.RATIO_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.START_DATE_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_TAKEN_DAYS_COLUMN;
import static com.legadi.ui.vacations.common.Utils.isNumber;

import java.io.FileInputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.compress.utils.Lists;
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
import com.legadi.ui.vacations.model.Employee;
import com.legadi.ui.vacations.model.EmployeeBalance;
import com.legadi.ui.vacations.model.EmployeeYear;
import com.legadi.ui.vacations.model.YearRecord;

@Service
public class EmployeeService {

    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final ConfigService configService;
    private final AlertService alertService;
    private final ErrorMessage errorMessage;

    public EmployeeService(ConfigService configService,
            AlertService alertService,
            ErrorMessage errorMessage) {
        this.configService = configService;
        this.alertService = alertService;
        this.errorMessage = errorMessage;
    }

    public EmployeeBalance getEmployeeBalance(EmployeeYear employeeYear) {
        return readFile(workbook -> getEmployeeBalance(employeeYear, workbook), null);
    }

    public List<EmployeeYear> getEmployeesWithTakenDays() {
        return readFile(workbook -> {
            Map<String, EmployeeYear> employees = getEmployeesWithTakenDays(workbook);

            return employees.values()
                .stream()
                .sorted(Comparator.comparing(EmployeeYear::getName))
                .toList();
        }, Lists.newArrayList());
    }

    private <T> T readFile(FileProcessor<T> processor, T defaultValue) {
        String balanceFile = configService.get(FILE_TO_ANALYZE_LOCATION);

        try (FileInputStream file = new FileInputStream(balanceFile);
                Workbook workbook = WorkbookFactory.create(file)) {
            return processor.apply(workbook);
        } catch (Exception ex) {
            String message = String.format(errorMessage.getReadBalanceFile(), balanceFile);
            logger.error(message, ex);
            alertService.warn(null, message);
            return defaultValue;
        }
    }

    private EmployeeBalance getEmployeeBalance(EmployeeYear employeeYear, Workbook workbook) {
        Sheet sheet = workbook.getSheet(employeeYear.getName());
        CellRef companyCell = configService.getCell(COMPANY_NAME_CELL);
        CellRef startDateCell = configService.getCell(START_DATE_CELL);
        CellRef previousCell = configService.getCell(PREVIOUS_VACATIONS_DAYS_CELL);
        CellRef ratioCell = configService.getCell(RATIO_DAYS_CELL);
        CellRef balanceCell = configService.getCell(BALANCE_DAYS_CELL);
        CellValue cellValue = new CellValue(workbook);

        EmployeeBalance employeeBalance = new EmployeeBalance();
        employeeBalance.setCompanyName(cellValue.asString(sheet, companyCell));
        employeeBalance.setName(employeeYear.getName());
        employeeBalance.setBalanceDays(cellValue.asInt(sheet, balanceCell));
        employeeBalance.setPreviousVacationDays(cellValue.asInt(sheet, previousCell));
        employeeBalance.setRatioDays(cellValue.asInt(sheet, ratioCell));
        employeeBalance.setStartDate(cellValue.asLocalDate(sheet, startDateCell));
        employeeBalance.setYearRecords(employeeYear.getYearRecords());
        return employeeBalance;
    }

    private Map<String, EmployeeYear> getEmployeesWithTakenDays(Workbook workbook) {
        CellRef employeeFirstCell = configService.getCell(EMPLOYEE_NAME_FIRST_CELL);
        CellRef totalTakenCol = configService.getCell(TOTAL_TAKEN_DAYS_COLUMN);
        int baseYear = configService.getInt(BASE_YEAR);
        
        CellValue cellValue = new CellValue(workbook);
        Map<String, EmployeeYear> employees = new HashMap<>();

        for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);

            if(isNumber(sheet.getSheetName())
                    && Integer.parseInt(sheet.getSheetName()) >= baseYear) {

                logger.debug("Reading sheet: {}", sheet.getSheetName());

                Map<String, EmployeeYear> employeesOnYear = findEmployees(cellValue,
                    employeeFirstCell, totalTakenCol, sheet);

                employeesOnYear.forEach((k, v) -> employees.merge(k, v, mergeEmployeeYear()));

                logger.debug("Extracted names for sheet [{}]: {}", sheet.getSheetName(), employees.keySet());
            } else {
                logger.debug("Discarding sheet for employee name list: {}", sheet.getSheetName());
            }
        }

        return employees;
    }

    private Map<String, EmployeeYear> findEmployees(CellValue cellValue, CellRef employeeFirstCell,
            CellRef totalTakenCol, Sheet sheet) {
        int year = Integer.parseInt(sheet.getSheetName());
        return StreamSupport.stream(sheet.spliterator(), false)
            .filter(row -> row.getRowNum() >= employeeFirstCell.getRow())
            .filter(row -> hasTakenDays(cellValue, totalTakenCol, row))
            .map(row -> toTakenByEmployee(cellValue, employeeFirstCell, totalTakenCol, year, row))
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Employee::getName, e -> e, mergeEmployeeYear()));
    }

    private boolean hasTakenDays(CellValue cellValue, CellRef totalTakenCol, Row row) {
        return isNumber(cellValue.asString(row.getCell(totalTakenCol.getCol())));
    }

    private EmployeeYear toTakenByEmployee(CellValue cellValue, CellRef employeeFirstCell,
            CellRef totalTakenCol, int year, Row row) {
        String name = cellValue.asString(row.getCell(employeeFirstCell.getCol()));

        if(name == null) {
            return null;
        }

        int totalTaken = cellValue.asInt(row.getCell(totalTakenCol.getCol()));

        EmployeeYear employee = new EmployeeYear();
        employee.setName(name.trim());
        employee.setYearRecords(new HashMap<>());

        YearRecord yearRecord = new YearRecord();
        yearRecord.setYear(year);
        yearRecord.setTakenByYear(totalTaken);

        employee.getYearRecords().put(year, yearRecord);
        return employee;
    }

    private BinaryOperator<EmployeeYear> mergeEmployeeYear() {
        return (e1, e2) -> {
            e1.getYearRecords().values().forEach(left -> {
                YearRecord right = e2.getYearRecords().getOrDefault(left.getYear(), new YearRecord());
                if(left.getAllowedByYear() == 0) {
                    left.setAllowedByYear(right.getAllowedByYear());
                }
                if(left.getTakenByYear() == 0) {
                    left.setTakenByYear(right.getTakenByYear());
                }
            });
            e2.getYearRecords().keySet().removeAll(e1.getYearRecords().keySet());
            e1.getYearRecords().putAll(e2.getYearRecords());
            return e1;
        };
    }

    @FunctionalInterface
    public static interface FileProcessor<T> {

        T apply(Workbook workbook) throws Exception;
    }
}
