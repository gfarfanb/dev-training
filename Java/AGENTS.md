# AGENTS.md

Multi-version Java training repository with JDK feature examples (8–25).

## Repository Structure

This is a **multi-module, multi-version training repository** organized by JDK versions (Java 8–25).

- Most JDK versions (8, 10–25, except 9) are single Maven modules: `Java {N}/jdk{N}-features/pom.xml`
- **Java 9 is different**: It's a **parent pom with 4 sub-modules**: `module_a`, `module_b`, `module_c`, `poc`
  - The `poc` module depends on `module_b` and `module_c` and includes Spring Boot test dependencies
- Each version folder lives at the same level: `Java 8/`, `Java 9/`, ..., `Java 25/`

## Java Projects

Each JDK version lives at `Java {N}/jdk{N}-features/` with its own `pom.xml` and Maven wrapper (`mvnw`/`mvnw.cmd`).

**Java 9 is special**: Parent pom with 4 sub-modules (`module_a`, `module_b`, `module_c`, `poc`). The `poc` module depends on `module_b` and `module_c`. Run `./mvnw install` from the parent to resolve inter-module dependencies.

All other versions (8, 10–25) are single-module projects.

## Maven

- **Java version used locally**: according to the defined version in **pom** file, **JAVA_HOME** needs to be set to the proper JDK binaries (ask user to setup the required Java version).
- Build tool: **Maven 3.x** (uses wrapper scripts: `mvnw` / `mvnw.cmd`)
- **No parent pom** at repo root. Each version is independently versioned (`1.0.0-SNAPSHOT`)
- Compiler plugin always logs warnings and deprecations explicitly set in pom.xml

## Running Tests

From any module or parent directory, run tests with:
```sh
./mvnw test                    # All tests in module
./mvnw test -Dtest=ClassName   # Single test class
```

## Test Naming Convention

Tests use a strict naming convention. See `Java/README.md` for full details:
- **PoC tests** (usage/functionality): `[class-name]_[method-name]_usage`
  - Example: `optional_of_usage()`, `optional_ofAndEmpty_usage()`
- **Implementation tests**: `[reference]_[implemented-feature]_implementation`
  - Example: `module_classInstantiation_implementation()`

## Key Differences

- **Java 9 modules**: The parent pom must be built with `./mvnw install` before the `poc` module will resolve its inter-module dependencies (`module_b`, `module_c`)
- **VS Code configuration**: `.vscode/settings.json` and `.vscode/launch.json` are provided in some version folders to set `java.jdt.ls.java.home` and debug configs; they may need adjustment for your environment

## Build & Verify

```sh
# In any module directory:
./mvnw clean compile         # Compile only
./mvnw clean verify          # Compile + test
./mvnw clean install         # Compile + test + install to local repository (required for inter-module deps)
```

## VS Code

Some folders have `.vscode/settings.json` with `java.jdt.ls.java.home` that may need adjustment for your environment.

## Quick Paths

- `Java/README.md` — Full documentation, test naming, VS Code config templates
- `Java {N}/jdk{N}-features/` — Single-module feature showcase (all other versions)
- `Java 9/jdk9-features/` — Modular architecture example (only multi-module project)
