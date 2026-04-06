package com.legadi.ui.vacations.controller;

import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_HORIZONTAL;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_VALUE_COLUMN;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_VALUE_ROW;
import static com.legadi.ui.vacations.common.ConfigConstants.ALLOWED_DAYS_YEAR_FIRST_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.BALANCE_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.BASE_YEAR;
import static com.legadi.ui.vacations.common.ConfigConstants.COMPANY_NAME_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.CREATE_BACKUP_ON_SAVE;
import static com.legadi.ui.vacations.common.ConfigConstants.EMPLOYEE_NAME_FIRST_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.FILE_TO_ANALYZE_LOCATION;
import static com.legadi.ui.vacations.common.ConfigConstants.INCLUDE_PREVIOUS_VACATIONS_DAYS;
import static com.legadi.ui.vacations.common.ConfigConstants.PREVIOUS_VACATIONS_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.RATIO_DAYS_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.START_DATE_CELL;
import static com.legadi.ui.vacations.common.ConfigConstants.START_DATE_FORMAT;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_DAYS_EXPIRATION;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_DAYS_YEAR;
import static com.legadi.ui.vacations.common.ConfigConstants.TOTAL_TAKEN_DAYS_COLUMN;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.legadi.ui.vacations.common.AlertMessage;
import com.legadi.ui.vacations.common.ComponentText;
import com.legadi.ui.vacations.common.functions.ToNumber;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@Component
public class AppController {

    private static final double FIELD_OPACITY_NORMAL = 1;
    private static final double FIELD_OPACITY_NOT_EDITABLE = 0.5;

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
    private TableColumn<YearRecord, LocalDate> expirationColumn;
    @FXML
    private Button calculateButton;
    @FXML
    private Button saveButton;

    @FXML
    private TextField configEmployeeField;
    @FXML
    private Button configEmployeeHelpButton;
    @FXML
    private TextField configTakenField;
    @FXML
    private Button configTakenHelpButton;
    @FXML
    private TextField configCompanyField;
    @FXML
    private Button configCompanyHelpButton;
    @FXML
    private TextField configStartField;
    @FXML
    private Button configStartHelpButton;
    @FXML
    private ComboBox<String> configFormatBox;
    @FXML
    private TextField configBalanceField;
    @FXML
    private Button configBalanceHelpButton;
    @FXML
    private TextField configPreviousField;
    @FXML
    private Button configPreviousHelpButton;
    @FXML
    private TextField configRatioField;
    @FXML
    private Button configRatioHelpButton;
    @FXML
    private TextField configYearField;
    @FXML
    private Button configYearHelpButton;
    @FXML
    private CheckBox configHorizontalCheck;
    @FXML
    private TextField configColDaysField;
    @FXML
    private Button configColDaysHelpButton;
    @FXML
    private TextField configRowDaysField;
    @FXML
    private Button configRowDaysHelpButton;
    @FXML
    private TextField configBaseYearField;
    @FXML
    private TextField configTotalYearField;
    @FXML
    private TextField configExpirationField;
    @FXML
    private CheckBox configIncludeCheck;
    @FXML
    private CheckBox configBackupCheck;
    @FXML
    private Button configResetButton;
    @FXML
    private Button configSaveButton;

    @Value("${app.date.formats}")
    private List<String> dateFormats;

    private final ApplicationContext context;
    private final ConfigService configService;
    private final AlertService alertService;
    private final EmployeeService employeeService;
    private final ComponentText componentText;
    private final AlertMessage alertMessage;

    private Stage primaryStage;

    private EmployeeBalance currentEmployeeBalance;

    public AppController(
            ApplicationContext context,
            ConfigService configService,
            AlertService alertService,
            EmployeeService employeeService,
            ComponentText componentText,
            AlertMessage alertMessage) {
        this.context = context;
        this.configService = configService;
        this.alertService = alertService;
        this.employeeService = employeeService;
        this.componentText = componentText;
        this.alertMessage = alertMessage;
    }

    @FXML
    public void initialize() {
        this.primaryStage = context.getBean(Stage.class);

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
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expiration"));

        calculateButton.setOnAction(event -> calculateBalance());
        saveButton.setOnAction(event -> saveEmployee());

        Dialog<ImageView> configEmployeeHelpDialog = createHelpDialog(
            componentText.getEmployeeNameFirstCellHelp(), "employeeNameFirstCell.png");
        configEmployeeHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configEmployeeHelpButton.setOnAction(event -> configEmployeeHelpDialog.show());

        Dialog<ImageView> configTakenHelpDialog = createHelpDialog(
            componentText.getTotalTakenDaysColumnHelp(), "totalTakenDaysColumn.png");
        configTakenHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configTakenHelpButton.setOnAction(event -> configTakenHelpDialog.show());

        Dialog<ImageView> configCompanyHelpDialog = createHelpDialog(
            componentText.getCompanyNameCellHelp(), "companyNameCell.png");
        configCompanyHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configCompanyHelpButton.setOnAction(event -> configCompanyHelpDialog.show());

        Dialog<ImageView> configStartHelpDialog = createHelpDialog(
            componentText.getStartDateCellHelp(), "startDateCell.png");
        configStartHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configStartHelpButton.setOnAction(event -> configStartHelpDialog.show());

        Dialog<ImageView> configBalanceHelpDialog = createHelpDialog(
            componentText.getBalanceDaysCellHelp(), "balanceDaysCell.png");
        configBalanceHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configBalanceHelpButton.setOnAction(event -> configBalanceHelpDialog.show());

        Dialog<ImageView> configPreviousHelpDialog = createHelpDialog(
            componentText.getPreviousVacationsDaysCellHelp(), "previousVacationsDaysCell.png");
        configPreviousHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configPreviousHelpButton.setOnAction(event -> configPreviousHelpDialog.show());

        Dialog<ImageView> configRatioHelpDialog = createHelpDialog(
            componentText.getRatioDaysCellHelp(), "ratioDaysCell.png");
        configRatioHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configRatioHelpButton.setOnAction(event -> configRatioHelpDialog.show());

        Dialog<ImageView> configYearHelpDialog = createHelpDialog(
            componentText.getAllowedDaysYearFirstCellHelp(), "allowedDaysYearFirstCell.png");
        configYearHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configYearHelpButton.setOnAction(event -> configYearHelpDialog.show());

        Dialog<ImageView> configColDaysHelpDialog = createHelpDialog(
            componentText.getAllowedDaysValueColumnHelp(), "allowedDaysValueColumn.png");
        configColDaysHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configColDaysHelpButton.setOnAction(event -> configColDaysHelpDialog.show());

        Dialog<ImageView> configRowDaysHelpDialog = createHelpDialog(
            componentText.getAllowedDaysValueRowHelp(), "allowedDaysValueRow.png");
        configRowDaysHelpButton.setGraphic(createImageView("icons8-help-16.png"));
        configRowDaysHelpButton.setOnAction(event -> configRowDaysHelpDialog.show());

        configFormatBox.setItems(FXCollections.observableList(dateFormats));

        configColDaysField.setDisable(configService.getBoolean(ALLOWED_DAYS_HORIZONTAL));
        configRowDaysField.setDisable(!configService.getBoolean(ALLOWED_DAYS_HORIZONTAL));

        configHorizontalCheck.setOnAction(event -> {
            configService.override(ALLOWED_DAYS_HORIZONTAL, configHorizontalCheck.isSelected());
            configColDaysField.setDisable(configHorizontalCheck.isSelected());
            configRowDaysField.setDisable(!configHorizontalCheck.isSelected());
        });

        previousField.setEditable(configService.getBoolean(INCLUDE_PREVIOUS_VACATIONS_DAYS));
        previousField.setOpacity(previousField.isEditable() ? FIELD_OPACITY_NORMAL : FIELD_OPACITY_NOT_EDITABLE);
        calculateButton.setVisible(configService.getBoolean(INCLUDE_PREVIOUS_VACATIONS_DAYS));

        configIncludeCheck.setOnAction(event -> {
            configService.override(INCLUDE_PREVIOUS_VACATIONS_DAYS, configIncludeCheck.isSelected());
            previousField.setEditable(configIncludeCheck.isSelected());
            previousField.setOpacity(previousField.isEditable() ? FIELD_OPACITY_NORMAL : FIELD_OPACITY_NOT_EDITABLE);
            calculateButton.setVisible(configIncludeCheck.isSelected());
            calculateBalance();
        });
        configBackupCheck.setOnAction(event -> {
            configService.override(CREATE_BACKUP_ON_SAVE, configIncludeCheck.isSelected());
        });

        configResetButton.setOnAction(event -> resetConfig());
        configSaveButton.setOnAction(event -> saveConfig());

        refreshEmployeeList();
        refreshConfig();
    }

    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

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

    private void resetEmployeeBalance() {
        companyField.setText(null);
        nameField.setText(null);
        startField.setText(null);
        previousField.setText(null);
        ratioField.setText(null);
        balanceField.setText(null);
        yearTable.setItems(FXCollections.emptyObservableList());
        saveButton.setDisable(true);
        currentEmployeeBalance = null;
    }

    private void refreshEmployeeBalance() {
        String datePatterm = configService.get(START_DATE_FORMAT);

        companyField.setText(currentEmployeeBalance.getCompanyName());
        nameField.setText(currentEmployeeBalance.getName());
        startField.setText(Optional.ofNullable(currentEmployeeBalance.getStartDate())
            .map(d -> d.format(DateTimeFormatter.ofPattern(datePatterm)))
            .orElse(null));
        previousField.setText(Integer.toString(currentEmployeeBalance.getPreviousVacationDays()));
        ratioField.setText(Integer.toString(currentEmployeeBalance.getRatioDays()));
        balanceField.setText(Integer.toString(currentEmployeeBalance.getBalanceDays()));

        List<YearRecord> yearRecords = currentEmployeeBalance.getYearRecords().values()
            .stream()
            .sorted(Comparator.comparing(YearRecord::getYear))
            .toList();
        yearTable.setItems(FXCollections.observableList(yearRecords));
    }

    private void loadEmployeeBalance(EmployeeYear employeeYear) {
        currentEmployeeBalance = employeeService.getEmployeeBalance(employeeYear);

        saveButton.setDisable(currentEmployeeBalance == null);

        if(currentEmployeeBalance == null) {
            alertService.warn(null,
                String.format(alertMessage.getBalanceNotFound(), employeeYear.getName()));
            return;
        }

        refreshEmployeeBalance();
    }

    private void calculateBalance() {
        if(currentEmployeeBalance != null) {
            currentEmployeeBalance.setPreviousVacationDays(getPreviousVacationDays());
            employeeService.calculateBalanceDays(currentEmployeeBalance);
            refreshEmployeeBalance();
        }
    }

    private void saveEmployee() {
        if(currentEmployeeBalance != null) {
            currentEmployeeBalance.setPreviousVacationDays(getPreviousVacationDays());
            employeeService.saveEmployee(currentEmployeeBalance);
        }
    }

    private void refreshConfig() {
        configEmployeeField.setText(configService.get(EMPLOYEE_NAME_FIRST_CELL));
        configTakenField.setText(configService.get(TOTAL_TAKEN_DAYS_COLUMN));
        configCompanyField.setText(configService.get(COMPANY_NAME_CELL));
        configStartField.setText(configService.get(START_DATE_CELL));
        configFormatBox.setValue(configService.get(START_DATE_FORMAT));
        configBalanceField.setText(configService.get(BALANCE_DAYS_CELL));
        configPreviousField.setText(configService.get(PREVIOUS_VACATIONS_DAYS_CELL));
        configRatioField.setText(configService.get(RATIO_DAYS_CELL));
        configYearField.setText(configService.get(ALLOWED_DAYS_YEAR_FIRST_CELL));
        configHorizontalCheck.setSelected(configService.getBoolean(ALLOWED_DAYS_HORIZONTAL));
        configColDaysField.setText(configService.get(ALLOWED_DAYS_VALUE_COLUMN));
        configRowDaysField.setText(configService.get(ALLOWED_DAYS_VALUE_ROW));
        configBaseYearField.setText(configService.get(BASE_YEAR));
        configTotalYearField.setText(configService.get(TOTAL_DAYS_YEAR));
        configExpirationField.setText(configService.get(TOTAL_DAYS_EXPIRATION));
        configIncludeCheck.setSelected(configService.getBoolean(INCLUDE_PREVIOUS_VACATIONS_DAYS));
        configBackupCheck.setSelected(configService.getBoolean(CREATE_BACKUP_ON_SAVE));
    }

    private void resetConfig() {
        configService.resetToDefaultConfig();
        refreshConfig();
    }

    private void saveConfig() {
        Map<String, Object> overrides = new HashMap<>();
        overrides.put(EMPLOYEE_NAME_FIRST_CELL, configEmployeeField.getText());
        overrides.put(TOTAL_TAKEN_DAYS_COLUMN, configTakenField.getText());
        overrides.put(COMPANY_NAME_CELL, configCompanyField.getText());
        overrides.put(START_DATE_CELL, configStartField.getText());
        overrides.put(START_DATE_FORMAT, configFormatBox.getValue());
        overrides.put(BALANCE_DAYS_CELL, configBalanceField.getText());
        overrides.put(PREVIOUS_VACATIONS_DAYS_CELL, configPreviousField.getText());
        overrides.put(RATIO_DAYS_CELL, configRatioField.getText());
        overrides.put(ALLOWED_DAYS_YEAR_FIRST_CELL, configYearField.getText());
        overrides.put(ALLOWED_DAYS_HORIZONTAL, configHorizontalCheck.isSelected());
        overrides.put(ALLOWED_DAYS_VALUE_COLUMN, configColDaysField.getText());
        overrides.put(ALLOWED_DAYS_VALUE_ROW, configRowDaysField.getText());
        overrides.put(BASE_YEAR, configBaseYearField.getText());
        overrides.put(TOTAL_DAYS_YEAR, configTotalYearField.getText());
        overrides.put(TOTAL_DAYS_EXPIRATION, configExpirationField.getText());
        configService.override(overrides);
    }

    private Dialog<ImageView> createHelpDialog(String title, String imageName) {
        Dialog<ImageView> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(createImageView(imageName));
        dialog.getDialogPane().getButtonTypes()
            .add(new ButtonType(componentText.getAcceptButton(), ButtonData.OK_DONE));
        return dialog;
    }

    private ImageView createImageView(String imageName) {
        return new ImageView(
            new Image(
                getClass().getResourceAsStream("/images/" + imageName)
            )
        );
    }

    private int getPreviousVacationDays() {
        return new ToNumber().applyOpt(previousField.getText())
            .map(Number::intValue)
            .orElse(0);
    }
}
