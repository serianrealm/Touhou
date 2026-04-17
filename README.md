# Repository

<p align="center">
  <strong>Language: <a href="README.md">English</a> | <a href="docs/README_zh.md">中文</a></strong>
</p>

This repository is my Software Construction homework. The project is a Java application built with Gradle.

## Requirements

- JDK 25
- Gradle Wrapper included in this repository

## Build

Run from the repository root:

```bash
./gradlew build
```

## Test

Run all tests from the repository root:

```bash
./gradlew test
```

## Run

Start the application from the repository root:

```bash
./gradlew :apps:run
```

## UML

- PlantUML source files are located in `docs/uml/`.
- The compatibility PNG backup directory is `docs/uml_png/`.

## Project Structure

```text
.
├── apps/          # Application source code and tests
├── docs/          # Project documentation
│   ├── uml/       # PlantUML files
│   └── uml_png/   # PNG backup directory for compatibility
├── gradle/        # Gradle wrapper files
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat
```

## Note

This project is for coursework and educational use.
