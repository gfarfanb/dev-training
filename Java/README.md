
# Java

This is a multi-version Java repository containing JDK feature examples (Java 8-25), code challenges, and specific Java usage examples.


## PoC Test naming convention

Name for the evaluation of the new objects functionalities
```java
// Template: [class-name]_[method-name]_usage
@Test
public void optional_of_usage() {
    // ...
}
```

```java
// Template: [class-name]_[method-name]And[method-name]_usage
@Test
public void optional_ofAndEmpty_usage() {
    // ...
}
```

```java
// Template: [class-name]_[method-name]For[case-name]_usage
@Test
public void optional_ofForSpecificEdgeCase_usage() {
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
    "java.jdt.ls.java.home": "<java_home_path>",
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
            "javaExec": "<java_exec_path>",
            "name": "[<env>] <class_name> [<project>]",
            "request": "launch",
            "mainClass": "<main_class>",
            "projectName": "<project>",
            "args": [
                "-xvf",
                "-Dspring.profiles.active=<profile>",
                "<other_arg>",
            ],
            "vmArgs": [
                "--module-path=<module_lib_path>",
                "--add-modules=<module_name,...,module_name_x>",
                "<other_vm_arg>=<value>",
            ],
            "env": {
                "PATH": "<path_value>",
                "SPRING_PROFILES_ACTIVE": "<profile>",
                "<other_env_var>": "<value>"
            }
        }
    ]
}
```

## Features to review

- Foreign Function & Memory API
    - [JEP 412: Foreign Function & Memory API (Incubator)](https://openjdk.org/jeps/412)
    - [JEP 419: Foreign Function & Memory API (Second Incubator)](https://openjdk.org/jeps/419)
    - [JEP 424: Foreign Function & Memory API (Preview)](https://openjdk.org/jeps/424)
    - [JEP 434: Foreign Function & Memory API (Second Preview)](https://openjdk.org/jeps/434)
    - [JEP 434: Foreign Function & Memory API (Second Preview)](https://openjdk.org/jeps/434)
    - [JEP 442: Foreign Function & Memory API (Third Preview)](https://openjdk.org/jeps/442)
    - [JEP 454: Foreign Function & Memory API](https://openjdk.org/jeps/454)


## Resources

### Platform
- [Java Platform, Standard Edition Documentation](https://docs.oracle.com/en/java/javase/)
- [JEP 0: JEP Index](https://openjdk.org/jeps/0)
- [Archived OpenJDK Releases](https://jdk.java.net/archive/)
- [Class File Versions](https://javaalmanac.io/bytecode/versions/)


### VS Code
- [Running and debugging Java](https://code.visualstudio.com/docs/java/java-debugging)
- [Testing Java with Visual Studio Code](https://code.visualstudio.com/docs/java/java-testing)


### Blog Refs

- [Spring Version Compatibility Cheatsheet](https://stevenpg.com/posts/spring-compat-cheatsheet/)
- [JUnit 6 Tutorial with Examples](https://howtodoinjava.com/junit/junit-6-tutorial/)
