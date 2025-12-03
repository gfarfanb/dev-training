package com.legadi.ui.vacations.controller;

import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_HORIZONTAL;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_VALUE_COLUMN;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_VALUE_ROW;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_YEAR_FIRST_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.BALANCE_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.BASE_YEAR;
import static com.legadi.ui.vacations.common.ConfigConstants.COMPANY_NAME_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.FILE_TO_ANALYZE_LOCATION;
import static com.legadi.ui.vacations.common.ConfigConstants.PREVIOUS_VACATIONS_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.START_DATE_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.START_DATE_FORMAT;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_DAYS_HALF_YEAR;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_DAYS_YEAR;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.legadi.ui.vacations.common.ErrorMessage;
import com.legadi.ui.vacations.model.EmployeeBalance;
import com.legadi.ui.vacations.model.EmployeeYear;
import com.legadi.ui.vacations.model.YearRecord;
import com.legadi.ui.vacations.service.AlertService;
import com.legadi.ui.vacations.service.ConfigService;
import com.legadi.ui.vacations.service.EmployeeService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Component
public class AppController {

    @FXML
    private MenuItem fileMenu;
    @FXML
    private MenuItem quitMenu;
    @FXML
    private MenuItem balanceMenu;
    @FXML
    private MenuItem configMenu;

    @FXML
    private StackPane mainStack;
    @FXML
    private Pane balancePane;
    @FXML
    private Pane configPane;

    @FXML
    private ListView<EmployeeYear> employeeList;
    @FXML
    private Label fileLabel;
    @FXML
    private TextField companyField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField startField;
    @FXML
    private TextField previousField;
    @FXML
    private TextField ratioField;
    @FXML
    private TextField balanceField;

    @FXML
    private TableView<YearRecord> yearTable;
    @FXML
    private TableColumn<YearRecord, Integer> yearColumn;
    @FXML
    private TableColumn<YearRecord, Integer> allowedColumn;
    @FXML
    private TableColumn<YearRecord, Integer> takenColumn;

    @FXML
    private TextField configCompanyField;
    @FXML
    private TextField configStartField;
    @FXML
    private TextField configFormatField;
    @FXML
    private TextField configBalanceField;
    @FXML
    private TextField configPreviousField;
    @FXML
    private TextField configYearField;
    @FXML
    private CheckBox configHorizontalCheck;
    @FXML
    private TextField configColDaysField;
    @FXML
    private TextField configRowDaysField;
    @FXML
    private TextField configBaseYearField;
    @FXML
    private TextField configTotalYearField;
    @FXML
    private TextField configHalfYearField;
    @FXML
    private Button saveButton;

    private final ApplicationContext context;
    private final ConfigService configService;
    private final AlertService alertService;
    private final EmployeeService employeeService;
    private final ErrorMessage errorMessage;

    public AppController(
            ApplicationContext context,
            ConfigService configService,
            AlertService alertService,
            EmployeeService employeeService,
            ErrorMessage errorMessage) {
        this.context = context;
        this.configService = configService;
        this.alertService = alertService;
        this.employeeService = employeeService;
        this.errorMessage = errorMessage;
    }

    @FXML
    public void initialize() {
        employeeList.setCellFactory(view -> new ListCell<EmployeeYear>() {

            @Override
            protected void updateItem(EmployeeYear employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getName());
                }
            }
        });
        employeeList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                resetEmployeeBalance();
                if(newValue != null) {
                    loadEmployeeBalance(newValue);
                }
            });

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        allowedColumn.setCellValueFactory(new PropertyValueFactory<>("allowedByYear"));
        takenColumn.setCellValueFactory(new PropertyValueFactory<>("takenByYear"));

        fileMenu.setOnAction(event -> {
            mainStack.getChildren().forEach(n -> n.setVisible(false));
            balancePane.setVisible(true);
            selectFile();
        });
        quitMenu.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });
        balanceMenu.setOnAction(event -> {
            mainStack.getChildren().forEach(n -> n.setVisible(false));
            balancePane.setVisible(true);
        });
        configMenu.setOnAction(event -> {
            mainStack.getChildren().forEach(n -> n.setVisible(false));
            configPane.setVisible(true);
        });

        configHorizontalCheck.setOnAction(event -> {
            configService.override(ALLOWED_DAYS_HORIZONTAL, configHorizontalCheck.isSelected());
            configColDaysField.setDisable(configHorizontalCheck.isSelected());
            configRowDaysField.setDisable(!configHorizontalCheck.isSelected());
        });

        saveButton.setOnAction(event -> {
            saveConfig();
        });

        refreshEmployeeList();
        refreshConfig();
    }

    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(context.getBean(Stage.class));

        if (selectedFile != null) {
            configService.override(FILE_TO_ANALYZE_LOCATION, selectedFile.getAbsolutePath());
            refreshEmployeeList();
        }
    }

    private void refreshEmployeeList() {
        String balanceFile = configService.get(FILE_TO_ANALYZE_LOCATION);

        if(StringUtils.isBlank(balanceFile)) {
            return;
        }

        employeeList.setItems(FXCollections.observableList(employeeService.getEmployeesWithTakenDays()));
        fileLabel.setText(balanceFile);
    }

    private void refreshConfig() {
        configCompanyField.setText(configService.get(COMPANY_NAME_CELL));
        configStartField.setText(configService.get(START_DATE_CELL));
        configFormatField.setText(configService.get(START_DATE_FORMAT));
        configBalanceField.setText(configService.get(BALANCE_DAYS_CELL));
        configPreviousField.setText(configService.get(PREVIOUS_VACATIONS_DAYS_CELL));
        configYearField.setText(configService.get(ALLOWED_DAYS_YEAR_FIRST_CELL));
        configHorizontalCheck.setSelected(configService.getBoolean(ALLOWED_DAYS_HORIZONTAL));
        configColDaysField.setDisable(configService.getBoolean(ALLOWED_DAYS_HORIZONTAL));
        configColDaysField.setText(configService.get(ALLOWED_DAYS_VALUE_COLUMN));
        configRowDaysField.setDisable(!configService.getBoolean(ALLOWED_DAYS_HORIZONTAL));
        configRowDaysField.setText(configService.get(ALLOWED_DAYS_VALUE_ROW));
        configBaseYearField.setText(configService.get(BASE_YEAR));
        configTotalYearField.setText(configService.get(TOTAL_DAYS_YEAR));
        configHalfYearField.setText(configService.get(TOTAL_DAYS_HALF_YEAR));
    }

    private void saveConfig() {
        Map<String, Object> overrides = new HashMap<>();
        overrides.put(COMPANY_NAME_CELL, configCompanyField.getText());
        overrides.put(START_DATE_CELL, configStartField.getText());
        overrides.put(START_DATE_FORMAT, configFormatField.getText());
        overrides.put(BALANCE_DAYS_CELL, configBalanceField.getText());
        overrides.put(PREVIOUS_VACATIONS_DAYS_CELL, configPreviousField.getText());
        overrides.put(ALLOWED_DAYS_YEAR_FIRST_CELL, configYearField.getText());
        overrides.put(ALLOWED_DAYS_HORIZONTAL, configHorizontalCheck.isSelected());
        overrides.put(ALLOWED_DAYS_VALUE_COLUMN, configColDaysField.getText());
        overrides.put(ALLOWED_DAYS_VALUE_ROW, configRowDaysField.getText());
        overrides.put(BASE_YEAR, configBaseYearField.getText());
        overrides.put(TOTAL_DAYS_YEAR, configTotalYearField.getText());
        overrides.put(TOTAL_DAYS_HALF_YEAR, configHalfYearField.getText());
        configService.override(overrides);
    }

    private void resetEmployeeBalance() {
        companyField.setText(null);
        nameField.setText(null);
        startField.setText(null);
        previousField.setText(null);
        ratioField.setText(null);
        balanceField.setText(null);
        yearTable.setItems(FXCollections.emptyObservableList());
    }

    private void loadEmployeeBalance(EmployeeYear employeeYear) {
        EmployeeBalance employeeBalance = employeeService.getEmployeeBalance(employeeYear);

        if(employeeBalance == null) {
            String message = String.format("%s: %s", errorMessage.getBalanceNotFound(), employeeYear.getName());
            alertService.warn(null, message);
            return;
        }

        String datePatterm = configService.get(START_DATE_FORMAT);

        companyField.setText(employeeBalance.getCompanyName());
        nameField.setText(employeeBalance.getName());
        startField.setText(Optional.ofNullable(employeeBalance.getStartDate())
            .map(d -> d.format(DateTimeFormatter.ofPattern(datePatterm)))
            .orElse(null));
        previousField.setText(Integer.toString(employeeBalance.getPreviousVacationDays()));
        ratioField.setText(Integer.toString(employeeBalance.getRatioDays()));
        balanceField.setText(Integer.toString(employeeBalance.getBalanceDays()));

        List<YearRecord> yearRecords = employeeBalance.getYearRecords().values()
                .stream()
                .sorted(Comparator.comparing(YearRecord::getYear))
                .toList();
        yearTable.setItems(FXCollections.observableList(yearRecords));
    }
}
