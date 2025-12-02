package com.legadi.ui.vacations.common;

import static com.legadi.ui.vacations.common.Utils.isNumber;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CellValue {

    private final Logger logger = LoggerFactory.getLogger(CellValue.class);

    private final Workbook workbook;

    public CellValue(Workbook workbook) {
        this.workbook = workbook;
    }

    public String asString(Sheet sheet, CellRef cellRef) {
        Row row = sheet.getRow(cellRef.getRow());
        Cell cell = row.getCell(cellRef.getCol());
        return asString(cell);
    }

    public String asString(Cell cell) {
        return getValueOrDefault(cell, Object::toString, null);
    }

    public int asInt(Sheet sheet, CellRef cellRef) {
        Row row = sheet.getRow(cellRef.getRow());
        Cell cell = row.getCell(cellRef.getCol());
        return asInt(cell);
    }

    public int asInt(Cell cell) {
        return getValueOrDefault(cell, new ToNumber(), 0).intValue();
    }

    public LocalDate asLocalDate(Sheet sheet, CellRef cellRef) {
        Row row = sheet.getRow(cellRef.getRow());
        Cell cell = row.getCell(cellRef.getCol());
        return asLocalDate(cell);
    }

    public LocalDate asLocalDate(Cell cell) {
        double dateRaw = getValueOrDefault(cell, new ToNumber(), 0).doubleValue();
        if(dateRaw == 0.0) {
            return null;
        }
        Date date = DateUtil.getJavaDate(dateRaw);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private <T> T getValueOrDefault(Cell cell, Function<Object, T> transformer, T defaultValue) {
        T value = getValue(cell, transformer);
        logger.trace("Reading cell: cell={} value={}", cell, value);
        return value != null ? value : defaultValue;
    }

    private <T> T getValue(Cell cell, Function<Object, T> transformer) {
        if(cell == null) {
            return null;
        }
        switch(cell.getCellType()) {
            case FORMULA:
                return evaluate(cell, transformer);
            case BOOLEAN:
                return transformer.apply(cell.getBooleanCellValue());
            case NUMERIC:
                return transformer.apply(cell.getNumericCellValue());
            case STRING:
                return transformer.apply(cell.getStringCellValue());
            default:
                return null;
        }
    }

    private <T> T evaluate(Cell cell, Function<Object, T> transformer) {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        switch (evaluator.evaluateFormulaCell(cell)) {
            case BOOLEAN:
                return transformer.apply(cell.getBooleanCellValue());
            case NUMERIC:
                return transformer.apply(cell.getNumericCellValue());
            case STRING:
                return transformer.apply(cell.getStringCellValue());
            default:
                return null;
        }
    }

    public static class ToNumber implements Function<Object, Number> {

        @Override
        public Number apply(Object value) {
            if(value instanceof Boolean) {
                return ((boolean) value) ? 1 : 0;
            }
            if(value instanceof Double) {
                return (double) value;
            }
            if(value instanceof String) {
                String numberRaw = (String) value;
                return isNumber(numberRaw) ? Double.valueOf(numberRaw) : null;
            }
            return null;
        }
    }
}
