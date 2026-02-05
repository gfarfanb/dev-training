
# Java

## VS Code configuration

**\<PROJECT_DIR>/.vscode/settings.json**
```json title="settings.json"
{
    "java.jdt.ls.java.home": "<java-home-path>"
}
```

**\<PROJECT_DIR>/.vscode/launch.json**
```json title="launch.json"
{
    // ...
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "javaExec": "<java-exec-path>",
            "name": "[<env>] <class-name> [<project>]",
            "request": "launch",
            "mainClass": "<main-class>",
            "projectName": "<project>",
            "args": [
                "-xvf",
                "-Dspring.profiles.active=<profile>",
                "<other-arg>",
            ],
            "vmArgs": [
                "--module-path=<module-lib-path>",
                "--add-modules=<module-name,...,module-name-x>",
                "<other-vm-arg>=<value>",
            ],
            "env": {
                "PATH": "<path-value>",
                "SPRING_PROFILES_ACTIVE": "<profile>",
                "<other-env-var>": "<value>"
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

### VS Code
- [Running and debugging Java](https://code.visualstudio.com/docs/java/java-debugging)
- [Testing Java with Visual Studio Code](https://code.visualstudio.com/docs/java/java-testing)
