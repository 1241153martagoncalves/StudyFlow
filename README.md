# 🎓 StudyFlow

**Academic management made simple.**

StudyFlow is a personal Java application designed to help students organize their academic progress, manage course units and assessments, and calculate their grades.

The application provides two user interfaces — a graphical interface built with **JavaFX** and a console-based interface — both connected to the same application logic.

## Features

### Course Unit Management
- Add and remove course units.
- View registered course units and their details.
- Organize courses by academic year and semester.
- Store information such as course name, acronym and ECTS credits.

### Assessment Management
- Create assessments with different types and weights.
- Add, update and remove assessment grades.
- Track assessment weight distribution.
- Validate assessment information.

### Grade Calculations
- Calculate grades based on assessment weights.
- Estimate the grade required to reach a target final grade.
- Monitor academic progress throughout the semester.

### Two User Interfaces
- **JavaFX:** Graphical interface with interactive forms, navigation and custom styling.
- **Console:** Menu-driven interface for managing academic information through the terminal.

### Data Persistence
- Save academic information locally.
- Load previously saved data when the application starts.
- Use Java object serialization for persistent storage.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 23 | Application logic and object-oriented programming |
| JavaFX 23.0.1 | Graphical user interface |
| CSS | Interface styling |
| Maven | Dependency and build management |
| Java Serialization | Local data persistence |
| Git & GitHub | Version control and project hosting |

## Project Architecture

StudyFlow separates application data, business logic, persistence and user interfaces.

**Main components:**

- `StudyFlow` — Manages the collection of course units.
- `CourseUnit` — Represents a course and its assessments.
- `Assessment` — Stores assessment information, weights and grades.
- `StudyFlowController` — Coordinates application operations.
- `StudyFlowRepository` — Handles local data persistence.
- `StudyFlowConsoleUI` — Implements the console interface.
- `StudyFlowJavaFXUI` — Implements the graphical interface.
- `StudyFlowJavaFXApp` — Launches the JavaFX application.

## Getting Started

### Prerequisites

- JDK 23
- Apache Maven
- IntelliJ IDEA or another Java-compatible IDE (optional)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/1241153martagoncalves/StudyFlow.git
   ```

2. Navigate to the project directory:

   ```bash
   cd StudyFlow
   ```

3. Compile the application:

   ```bash
   mvn clean compile
   ```

4. Launch the JavaFX graphical interface:

   ```bash
   mvn javafx:run
   ```

The application also includes a console interface accessible through the Java application entry point.

## Project Structure

```text
StudyFlow/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/example/
│       │       ├── Assessment.java
│       │       ├── AssessmentType.java
│       │       ├── CourseUnit.java
│       │       ├── Main.java
│       │       ├── StudyFlow.java
│       │       ├── StudyFlowController.java
│       │       ├── StudyFlowRepository.java
│       │       ├── StudyFlowConsoleUI.java
│       │       ├── StudyFlowJavaFXApp.java
│       │       └── StudyFlowJavaFXUI.java
│       └── resources/
│           ├── studyflow.css
│           └── studyflow-background.png
├── .gitignore
└── pom.xml
```

## Future Improvements

- Add automated unit tests.
- Improve grade statistics and academic progress visualization.
- Introduce data export and import functionality.
- Further enhance the JavaFX user experience.

## About the Project

StudyFlow is an independent personal project developed in 2026 as part of my learning journey in Computer Engineering at the Instituto Superior de Engenharia do Porto (ISEP).

The project allows me to apply object-oriented programming, software organization, user interface development and data persistence concepts to a practical problem.

## Author

**Marta Gonçalves**

Computer Engineering Student — ISEP
