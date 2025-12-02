package com.legadi.ui.vacations.controller;

import org.springframework.stereotype.Component;

import com.legadi.ui.vacations.model.Employee;
import com.legadi.ui.vacations.service.EmployeeService;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

@Component
public class AppController {

    @FXML
    private MenuItem fileMenu;

    @FXML
    private MenuItem quitMenu;

    @FXML
    private ListView<Employee> employeeList;

    private final HostServices hostServices;

    private final EmployeeService employeeService;

    public AppController(HostServices hostServices,
            EmployeeService employeeService) {
        this.hostServices = hostServices;
        this.employeeService = employeeService;
    }

    @FXML
    public void initialize() {
        employeeList.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getName());
                }
            }
        });

        refreshEmployeeList();
    }

    public void refreshEmployeeList() {
        employeeList.setItems(FXCollections.observableList(employeeService.getEmployees()));
    }
}
