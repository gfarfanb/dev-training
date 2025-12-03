package com.legadi.ui.vacations.service;

import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_HORIZONTAL;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_VALUE_COLUMN;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_VALUE_ROW;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_YEAR_FIRST_CELL;
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
import org.apache.poi.ss.usermodel.Cell;
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

    public List<EmployeeYear> getEmployeesWithTakenDays() {
        return readFile(workbook -> {
            Map<String, EmployeeYear> employees = getEmployeesWithTakenDays(workbook);

            return employees.values()
                .stream()
                .sorted(Comparator.comparing(EmployeeYear::getName))
                .toList();
        }, Lists.newArrayList());
    }

    public EmployeeBalance getEmployeeBalance(EmployeeYear employeeYear) {
        return readFile(workbook -> getEmployeeBalance(employeeYear, workbook), null);
    }

    public void calculateBalance(EmployeeBalance employeeBalance) {

    }

    public void saveEmployee(EmployeeBalance employeeBalance) {
        CellRef previousCell = configService.getCell(PREVIOUS_VACATIONS_DAYS_CELL);
        CellRef ratioCell = configService.getCell(RATIO_DAYS_CELL);
        CellRef balanceCell = configService.getCell(BALANCE_DAYS_CELL);
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

        Map<Integer, YearRecord> yearRecords = getAllowedDays(cellValue, sheet);
        employeeYear.getYearRecords().forEach((k, v) -> yearRecords.merge(k, v, mergeYearRecord()));
        employeeBalance.setYearRecords(yearRecords);

        calculateBalance(employeeBalance);

        return employeeBalance;
    }

    private Map<Integer, YearRecord> getAllowedDays(CellValue cellValue, Sheet sheet) {
        boolean isHorizontal = configService.getBoolean(ALLOWED_DAYS_HORIZONTAL);
        CellRef firstCell = configService.getCell(ALLOWED_DAYS_YEAR_FIRST_CELL);
        CellRef colCell = configService.getCell(ALLOWED_DAYS_VALUE_COLUMN);
        CellRef rowCell = configService.getCell(ALLOWED_DAYS_VALUE_ROW);

        if(isHorizontal) {
            Row yearRow = sheet.getRow(firstCell.getRow());
            Row allowedRow = sheet.getRow(rowCell.getRow());

            return StreamSupport.stream(yearRow.spliterator(), false)
                .filter(cell -> cell.getColumnIndex() >= firstCell.getCol())
                .filter(cell -> isValidYear(cellValue, cell, yearRow))
                .filter(cell -> isNumeric(cellValue, cell, allowedRow))
                .map(cell -> toYearRecord(cellValue, cell, yearRow, allowedRow))
                .collect(Collectors.toMap(
                    YearRecord::getYear,
                    y -> y,
                    (y1, y2) -> y2));
        } else {
            return StreamSupport.stream(sheet.spliterator(), false)
                .filter(row -> row.getRowNum() >= firstCell.getRow())
                .filter(row -> isValidYear(cellValue, firstCell, row))
                .filter(row -> isNumeric(cellValue, colCell, row))
                .map(row -> toYearRecord(cellValue, firstCell, colCell, row))
                .collect(Collectors.toMap(
                    YearRecord::getYear,
                    y -> y,
                    (y1, y2) -> y2));
        }
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
            .filter(row -> isNumeric(cellValue, totalTakenCol, row))
            .map(row -> toTakenByEmployee(cellValue, employeeFirstCell, totalTakenCol, year, row))
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Employee::getName, e -> e, mergeEmployeeYear()));
    }

    private boolean isNumeric(CellValue cellValue, CellRef numberCell, Row row) {
        return isNumber(cellValue.asString(row.getCell(numberCell.getCol())));
    }

    private boolean isNumeric(CellValue cellValue, Cell cell, Row row) {
        return isNumber(cellValue.asString(row.getCell(cell.getColumnIndex())));
    }

    private boolean isValidYear(CellValue cellValue, CellRef yearCell, Row row) {
        int baseYear = configService.getInt(BASE_YEAR);
        return cellValue.asInt(row.getCell(yearCell.getCol())) >= baseYear;
    }

    private boolean isValidYear(CellValue cellValue, Cell cell, Row row) {
        int baseYear = configService.getInt(BASE_YEAR);
        return cellValue.asInt(row.getCell(cell.getColumnIndex())) >= baseYear;
    }

    private YearRecord toYearRecord(CellValue cellValue, CellRef yearCell, CellRef allowedCell, Row row) {
        YearRecord yearRecord = new YearRecord();
        yearRecord.setYear(cellValue.asInt(row.getCell(yearCell.getCol())));
        yearRecord.setAllowedByYear(cellValue.asInt(row.getCell(allowedCell.getCol())));
        return yearRecord;
    }

    private YearRecord toYearRecord(CellValue cellValue, Cell cell, Row yearRow, Row allowedRow) {
        YearRecord yearRecord = new YearRecord();
        yearRecord.setYear(cellValue.asInt(yearRow.getCell(cell.getColumnIndex())));
        yearRecord.setAllowedByYear(cellValue.asInt(allowedRow.getCell(cell.getColumnIndex())));
        return yearRecord;
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

    private BinaryOperator<YearRecord> mergeYearRecord() {
        return (e1, e2) -> {
            if(e1.getAllowedByYear() == 0) {
                e1.setAllowedByYear(e2.getAllowedByYear());
            }
            if(e1.getTakenByYear() == 0) {
                e1.setTakenByYear(e2.getTakenByYear());
            };
            return e1;
        };
    }

    @FunctionalInterface
    public static interface FileProcessor<T> {

        T apply(Workbook workbook) throws Exception;
    }
}
