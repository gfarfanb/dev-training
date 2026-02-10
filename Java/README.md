
# Java

## PoC Test naming convention

Name for the evaluation of the new objects functionalities
```java
// Template: [class-name]_[method-name]_usage
@Test
public void optional_of_usage() {
    // ...
}
```

Name for the evaluation of the new type of implementations
```java
// Template: [reference]_[implemented-feature]_implementation
@Test
public void module_classInstantiation_implementation() {
    // ...
}
```

## VS Code configuration

**\<PROJECT_DIR>/.vscode/settings.json**
```json title="settings.json"
{
    "java.jdt.ls.java.home": "<java-home-path>",
    "search.exclude": {
        "**/target": true,
        "**/.git": true,
        "**/tmp": true,
        "**/log": true,
    },
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

### VS Code
- [Running and debugging Java](https://code.visualstudio.com/docs/java/java-debugging)
- [Testing Java with Visual Studio Code](https://code.visualstudio.com/docs/java/java-testing)

### Blog Refs

- [Spring Version Compatibility Cheatsheet](https://stevenpg.com/posts/spring-compat-cheatsheet/)
- [JUnit 6 Tutorial with Examples](https://howtodoinjava.com/junit/junit-6-tutorial/)
- [Java Versions and Features](https://www.marcobehler.com/guides/a-guide-to-java-versions-and-features)
