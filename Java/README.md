
# Java

## VS Code configuration

**\<PROJECT_DIR>/.vscode/settings.json**
```json title="settings.json"
{
    "java.jdt.ls.java.home": "<JAVA_HOME>"
}
```

**\<PROJECT_DIR>/.vscode/launch.json**
```json title="launch.json"
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "javaExec": "<JAVA_HOME>\\bin\\java.exe",
            "name": "[<ENV>] <CLASS-NAME> [<PROJECT>]",
            "request": "launch",
            "mainClass": "<MAIN-CLASS>",
            "projectName": "<PROJECT>",
            "args": [
                "<ARG-1>",
                "<ARG-2>",
            ],
            "vmArgs": [
                "<VM-ARG-1>=<VALUE>",
                "<VM-ARG-2>=<VALUE>",
            ],
            "env": {
                "<ENV-VAR-1>": "<VALUE>",
                "<ENV-VAR-2>": "<VALUE>"
            }
        }
    ]
}
```

## Resources

### Platform
- [Java Platform, Standard Edition Documentation](https://docs.oracle.com/en/java/javase/index.html)
- [Archived OpenJDK Releases](https://jdk.java.net/archive/)

### Java 8
- [Java™ Platform, Standard Edition 8 API Specification](https://docs.oracle.com/javase/8/docs/api/index.html)
