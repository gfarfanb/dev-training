package com.legadi.ui.vacations.controller;

import static com.legadi.ui.vacations.common.ConfigConstants.FILE_TO_ANALYZE_LOCATION;
import static com.legadi.ui.vacations.common.ConfigConstants.START_DATE_FORMAT;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
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
