
# vacations-balance

## VS Code configuration

**\<PROJECT_DIR>/.vscode/launch.json**
```json title="launch.json"
{
    "type": "java",
    "javaExec": "<JAVA_HOME>\\bin\\java.exe",
    "name": "MainApplication",
    "request": "launch",
    "mainClass": "com.legadi.ui.vacations.MainApplication",
    "projectName": "vacations-balance",
    "vmArgs": [
        "--module-path=<JAVAFX_SDK_HOME>\\lib",
        "--add-modules=javafx.controls,javafx.fxml"
    ]
}
```
