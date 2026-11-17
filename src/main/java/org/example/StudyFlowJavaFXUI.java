
package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class StudyFlowJavaFXUI {

    private final StudyFlowController controller;

    public StudyFlowJavaFXUI(StudyFlowController controller) {
        this.controller = controller;
    }

    public void start(Stage stage) {
        showHome(stage);
        stage.setTitle("StudyFlow");
        stage.show();
    }

    private void showHome(Stage stage) {
        Label title = new Label("StudyFlow");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Label subtitle = new Label("Academic management made simple.");

        int numberOfCourses = controller.getCourseUnits().size();
        Label courseCount = new Label("Course Units: " + numberOfCourses);

        Button addCourseButton = new Button("Add Course Unit");
        Button viewCoursesButton = new Button("View Course Units");

        addCourseButton.setPrefWidth(200);
        viewCoursesButton.setPrefWidth(200);

        addCourseButton.setOnAction(event -> showAddCourseUnitForm(stage));
        viewCoursesButton.setOnAction(event -> showCourseUnits(stage));

        VBox layout = new VBox(15, title, subtitle, courseCount,
                addCourseButton, viewCoursesButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        stage.setScene(createStyledScene(layout, 700, 500));
    }

    private void showAddCourseUnitForm(Stage stage) {
        Label title = createTitle("Add Course Unit");

        TextField nameField = new TextField();
        nameField.setPromptText("Course Unit Name");

        TextField acronymField = new TextField();
        acronymField.setPromptText("Acronym");

        TextField ectsField = new TextField();
        ectsField.setPromptText("ECTS");

        ComboBox<Integer> yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(1, 2, 3, 4, 5, 6);
        yearComboBox.setPromptText("Year");

        ComboBox<Integer> semesterComboBox = new ComboBox<>();
        semesterComboBox.getItems().addAll(1, 2);
        semesterComboBox.setPromptText("Semester");

        Label messageLabel = new Label();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            try {
                String name = nameField.getText().trim();
                String acronym = acronymField.getText().trim();

                if (name.isEmpty() || acronym.isEmpty()) {
                    messageLabel.setText("Name and acronym are required.");
                    return;
                }

                int ects = Integer.parseInt(ectsField.getText());

                Integer year = yearComboBox.getValue();
                Integer semester = semesterComboBox.getValue();

                if (year == null || semester == null) {
                    messageLabel.setText("Select year and semester.");
                    return;
                }

                if (ects <= 0) {
                    messageLabel.setText("ECTS must be greater than 0.");
                    return;
                }

                boolean added = controller.addCourseUnit(
                        name, acronym, ects, year, semester
                );

                if (added) {
                    showInformation("Success", "Course Unit added successfully.");
                    showHome(stage);
                } else {
                    messageLabel.setText("Unable to add Course Unit.");
                }

            } catch (NumberFormatException e) {
                messageLabel.setText("ECTS must be a number.");
            }
        });

        cancelButton.setOnAction(event -> showHome(stage));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(15);

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Acronym:"), 0, 1);
        form.add(acronymField, 1, 1);
        form.add(new Label("ECTS:"), 0, 2);
        form.add(ectsField, 1, 2);
        form.add(new Label("Year:"), 0, 3);
        form.add(yearComboBox, 1, 3);
        form.add(new Label("Semester:"), 0, 4);
        form.add(semesterComboBox, 1, 4);

        HBox buttons = new HBox(10, saveButton, cancelButton);

        VBox layout = new VBox(20, title, form, messageLabel, buttons);
        layout.setPadding(new Insets(30));

        stage.setScene(createStyledScene(layout, 700, 500));
    }

    private void showCourseUnits(Stage stage) {
        Label title = createTitle("Course Units");

        List<CourseUnit> courseUnits = controller.getCourseUnits();
        ListView<String> listView = new ListView<>();

        for (CourseUnit courseUnit : courseUnits) {
            listView.getItems().add(
                    courseUnit.getName() + " (" + courseUnit.getAcronym() + ")"
            );
        }

        Button openButton = new Button("Open");
        Button removeButton = new Button("Remove");
        Button backButton = new Button("Back");

        openButton.setOnAction(event -> {
            int index = listView.getSelectionModel().getSelectedIndex();

            if (index < 0) {
                showError("Select a Course Unit first.");
                return;
            }

            showCourseUnit(stage, index);
        });

        removeButton.setOnAction(event -> {
            int index = listView.getSelectionModel().getSelectedIndex();

            if (index < 0) {
                showError("Select a Course Unit first.");
                return;
            }

            CourseUnit courseUnit = courseUnits.get(index);

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Remove Course Unit");
            confirmation.setHeaderText("Remove " + courseUnit.getName() + "?");
            confirmation.setContentText(
                    "All assessments associated with this Course Unit will also be removed."
            );

            confirmation.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    controller.removeCourseUnit(index);
                    showCourseUnits(stage);
                }
            });
        });

        backButton.setOnAction(event -> showHome(stage));

        HBox buttons = new HBox(10, openButton, removeButton, backButton);
        VBox layout = new VBox(15, title, listView, buttons);

        layout.setPadding(new Insets(30));
        stage.setScene(createStyledScene(layout, 750, 550));
    }

    private void showCourseUnit(Stage stage, int courseUnitIndex) {
        CourseUnit courseUnit =
                controller.getCourseUnits().get(courseUnitIndex);

        Label title = createTitle(
                courseUnit.getAcronym() + " — " + courseUnit.getName()
        );

        Label details = new Label(
                "ECTS: " + courseUnit.getEcts()
                        + "   |   Year: " + courseUnit.getYear()
                        + "   |   Semester: " + courseUnit.getSemester()
        );

        Label weight = new Label(
                "Assessment weight: "
                        + String.format("%.1f", courseUnit.getTotalWeight())
                        + "% / 100%"
        );

        ProgressBar weightProgress = new ProgressBar(
                courseUnit.getTotalWeight() / 100.0
        );
        weightProgress.setPrefWidth(350);

        Label assessmentsTitle = new Label("Assessments");
        assessmentsTitle.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        ListView<String> assessmentList = createAssessmentList(courseUnit);

        Button addAssessment = new Button("Add Assessment");
        Button gradeAssessment = new Button("Add / Update Grade");
        Button removeAssessment = new Button("Remove Assessment");
        Button currentGrade = new Button("Calculate Final Grade");
        Button requiredGrade = new Button("Calculate Required Grade");
        Button back = new Button("Back");

        addAssessment.setOnAction(event ->
                showAddAssessment(stage, courseUnitIndex)
        );

        gradeAssessment.setOnAction(event -> {
            int assessmentIndex =
                    assessmentList.getSelectionModel().getSelectedIndex();

            if (assessmentIndex < 0) {
                showError("Select an assessment first.");
                return;
            }

            showGradeDialog(stage, courseUnitIndex, assessmentIndex);
        });

        removeAssessment.setOnAction(event -> {
            int assessmentIndex =
                    assessmentList.getSelectionModel().getSelectedIndex();

            if (assessmentIndex < 0) {
                showError("Select an assessment first.");
                return;
            }

            Assessment assessment =
                    courseUnit.getAssessments().get(assessmentIndex);

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Remove Assessment");
            confirmation.setHeaderText(
                    "Remove " + formatType(assessment.getType()) + "?"
            );

            confirmation.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    controller.removeAssessment(
                            courseUnitIndex, assessmentIndex
                    );
                    showCourseUnit(stage, courseUnitIndex);
                }
            });
        });

        currentGrade.setOnAction(event -> showCurrentGrade(courseUnit));

        requiredGrade.setOnAction(event ->
                showRequiredGrade(stage, courseUnitIndex)
        );

        back.setOnAction(event -> showCourseUnits(stage));

        HBox assessmentButtons = new HBox(
                10, addAssessment, gradeAssessment, removeAssessment
        );

        HBox calculationButtons = new HBox(
                10, currentGrade, requiredGrade
        );

        VBox layout = new VBox(
                15,
                title,
                details,
                weight,
                weightProgress,
                assessmentsTitle,
                assessmentList,
                assessmentButtons,
                calculationButtons,
                back
        );

        layout.setPadding(new Insets(30));
        stage.setScene(createStyledScene(layout, 850, 650));
    }

    private void showAddAssessment(Stage stage, int courseUnitIndex) {
        CourseUnit courseUnit =
                controller.getCourseUnits().get(courseUnitIndex);

        Label title = createTitle("Add Assessment");

        Label weightInfo = new Label(
                "Used: " + courseUnit.getTotalWeight()
                        + "%   |   Remaining: "
                        + (100 - courseUnit.getTotalWeight()) + "%"
        );

        ComboBox<AssessmentType> type = new ComboBox<>();
        type.getItems().addAll(AssessmentType.values());
        type.setPromptText("Assessment Type");

        TextField weightField = new TextField();
        weightField.setPromptText("Weight (%)");

        CheckBox hasGrade = new CheckBox("I already have a grade");

        TextField gradeField = new TextField();
        gradeField.setPromptText("Grade (0-20)");
        gradeField.setDisable(true);

        hasGrade.setOnAction(event ->
                gradeField.setDisable(!hasGrade.isSelected())
        );

        Label message = new Label();

        Button save = new Button("Save Assessment");
        Button cancel = new Button("Cancel");

        save.setOnAction(event -> {
            try {
                AssessmentType selectedType = type.getValue();

                if (selectedType == null) {
                    message.setText("Select an assessment type.");
                    return;
                }

                double assessmentWeight =
                        Double.parseDouble(weightField.getText());

                if (assessmentWeight <= 0) {
                    message.setText("Weight must be greater than 0.");
                    return;
                }

                if (courseUnit.getTotalWeight()
                        + assessmentWeight > 100.001) {
                    message.setText(
                            "Total assessment weight cannot exceed 100%."
                    );
                    return;
                }

                Double grade = null;

                if (hasGrade.isSelected()) {
                    grade = Double.parseDouble(gradeField.getText());

                    if (grade < 0 || grade > 20) {
                        message.setText("Grade must be between 0 and 20.");
                        return;
                    }
                }

                boolean added = controller.addAssessment(
                        courseUnitIndex,
                        selectedType,
                        assessmentWeight,
                        grade
                );

                if (added) {
                    showInformation(
                            "Success",
                            "Assessment added successfully."
                    );
                    showCourseUnit(stage, courseUnitIndex);
                } else {
                    message.setText("Unable to add assessment.");
                }

            } catch (NumberFormatException e) {
                message.setText("Weight and grade must be numbers.");
            }
        });

        cancel.setOnAction(event ->
                showCourseUnit(stage, courseUnitIndex)
        );

        VBox layout = new VBox(
                15,
                title,
                weightInfo,
                new Label("Assessment Type"),
                type,
                new Label("Weight"),
                weightField,
                hasGrade,
                gradeField,
                message,
                new HBox(10, save, cancel)
        );

        layout.setPadding(new Insets(30));
        stage.setScene(createStyledScene(layout, 700, 550));
    }

    private void showGradeDialog(
            Stage stage,
            int courseUnitIndex,
            int assessmentIndex
    ) {
        CourseUnit courseUnit =
                controller.getCourseUnits().get(courseUnitIndex);

        Assessment assessment =
                courseUnit.getAssessments().get(assessmentIndex);

        TextInputDialog dialog = new TextInputDialog(
                assessment.getGrade() == null
                        ? ""
                        : assessment.getGrade().toString()
        );

        dialog.setTitle("Assessment Grade");
        dialog.setHeaderText(
                formatType(assessment.getType())
                        + " — " + assessment.getWeight() + "%"
        );
        dialog.setContentText("Grade (0-20):");

        dialog.showAndWait().ifPresent(value -> {
            try {
                double grade = Double.parseDouble(value);

                if (grade < 0 || grade > 20) {
                    showError("Grade must be between 0 and 20.");
                    return;
                }

                boolean updated = controller.addGradeToAssessment(
                        courseUnitIndex,
                        assessmentIndex,
                        grade
                );

                if (updated) {
                    showInformation(
                            "Success",
                            "Grade saved successfully."
                    );
                    showCourseUnit(stage, courseUnitIndex);
                }

            } catch (NumberFormatException e) {
                showError("Grade must be a number.");
            }
        });
    }

    private void showCurrentGrade(CourseUnit courseUnit) {
        if (!courseUnit.hasCompleteAssessmentWeight()) {
            showError(
                    "Unable to calculate the final grade.\n\n"
                            + "Assessment weights currently total "
                            + courseUnit.getTotalWeight()
                            + "%.\nThey must total 100%."
            );
            return;
        }

        Double grade = courseUnit.calculateCurrentGrade();

        if (grade == null) {
            showError(
                    "Unable to calculate the final grade.\n\n"
                            + "Some assessments do not have a grade yet."
            );
            return;
        }

        showInformation(
                "Final Grade",
                String.format("Final grade: %.2f / 20", grade)
        );
    }

    private void showRequiredGrade(Stage stage, int courseUnitIndex) {
        CourseUnit courseUnit =
                controller.getCourseUnits().get(courseUnitIndex);

        if (courseUnit.getAssessments().isEmpty()) {
            showError("This Course Unit has no assessments.");
            return;
        }

        if (!courseUnit.hasCompleteAssessmentWeight()) {
            showError(
                    "Assessment weights must total 100% before calculating a required grade."
            );
            return;
        }

        Label title = createTitle("Required Grade Calculator");

        Label explanation = new Label(
                "Enter the final grade you want.\n"
                        + "You may also enter grades you already have "
                        + "or grades you want to assume.\n"
                        + "Leave unknown assessments blank."
        );

        TextField targetField = new TextField();
        targetField.setPromptText("Target final grade (0-20)");

        GridPane assessmentGrid = new GridPane();
        assessmentGrid.setHgap(15);
        assessmentGrid.setVgap(10);

        assessmentGrid.add(new Label("Assessment"), 0, 0);
        assessmentGrid.add(new Label("Weight"), 1, 0);
        assessmentGrid.add(new Label("Known / Assumed Grade"), 2, 0);

        List<TextField> gradeFields = new ArrayList<>();

        for (int i = 0; i < courseUnit.getAssessments().size(); i++) {
            Assessment assessment = courseUnit.getAssessments().get(i);

            Label type = new Label(formatType(assessment.getType()));
            Label weight = new Label(assessment.getWeight() + "%");

            TextField grade = new TextField();
            grade.setPromptText("Unknown");

            if (assessment.getGrade() != null) {
                grade.setText(assessment.getGrade().toString());
            }

            gradeFields.add(grade);

            assessmentGrid.add(type, 0, i + 1);
            assessmentGrid.add(weight, 1, i + 1);
            assessmentGrid.add(grade, 2, i + 1);
        }

        Label resultLabel = new Label();
        resultLabel.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        Button calculate = new Button("Calculate");
        Button back = new Button("Back");

        calculate.setOnAction(event -> {
            try {
                double target =
                        Double.parseDouble(targetField.getText());

                if (target < 0 || target > 20) {
                    resultLabel.setText(
                            "Target grade must be between 0 and 20."
                    );
                    return;
                }

                Double[] originalGrades =
                        new Double[courseUnit.getAssessments().size()];

                try {
                    for (int i = 0;
                         i < courseUnit.getAssessments().size();
                         i++) {

                        Assessment assessment =
                                courseUnit.getAssessments().get(i);

                        originalGrades[i] = assessment.getGrade();

                        String valueText =
                                gradeFields.get(i).getText().trim();

                        if (valueText.isEmpty()) {
                            assessment.setGrade(null);
                        } else {
                            double value = Double.parseDouble(valueText);

                            if (value < 0 || value > 20) {
                                resultLabel.setText(
                                        "All grades must be between 0 and 20."
                                );
                                return;
                            }

                            assessment.setGrade(value);
                        }
                    }

                    Double required =
                            courseUnit.calculateRequiredGrade(target);

                    if (required == null) {
                        Double finalGrade =
                                courseUnit.calculateCurrentGrade();

                        if (finalGrade != null) {
                            resultLabel.setText(String.format(
                                    "All assessments have grades. Final grade: %.2f / 20",
                                    finalGrade
                            ));
                        } else {
                            resultLabel.setText(
                                    "There are no remaining assessments to calculate."
                            );
                        }

                    } else if (required > 20) {
                        resultLabel.setText(String.format(
                                "Required average: %.2f / 20 — target is not achievable.",
                                required
                        ));

                    } else if (required <= 0) {
                        resultLabel.setText(
                                "Your target grade is already secured."
                        );

                    } else {
                        resultLabel.setText(String.format(
                                "You need an average of %.2f / 20 "
                                        + "in the remaining assessments.",
                                required
                        ));
                    }

                } finally {
                    for (int i = 0; i < originalGrades.length; i++) {
                        courseUnit.getAssessments()
                                .get(i)
                                .setGrade(originalGrades[i]);
                    }
                }

            } catch (NumberFormatException e) {
                resultLabel.setText(
                        "Please enter valid numerical grades."
                );
            }
        });

        back.setOnAction(event ->
                showCourseUnit(stage, courseUnitIndex)
        );

        VBox layout = new VBox(
                15,
                title,
                explanation,
                new Label("Target Grade:"),
                targetField,
                assessmentGrid,
                resultLabel,
                new HBox(10, calculate, back)
        );

        layout.setPadding(new Insets(30));
        stage.setScene(createStyledScene(layout, 850, 650));
    }

    private ListView<String> createAssessmentList(CourseUnit courseUnit) {
        ListView<String> list = new ListView<>();

        for (Assessment assessment : courseUnit.getAssessments()) {
            String grade = assessment.getGrade() == null
                    ? "Not graded"
                    : String.format("%.2f", assessment.getGrade());

            list.getItems().add(
                    formatType(assessment.getType())
                            + "  |  "
                            + assessment.getWeight()
                            + "%  |  Grade: "
                            + grade
            );
        }

        if (list.getItems().isEmpty()) {
            list.getItems().add("No assessments yet.");
            list.setDisable(true);
        }

        return list;
    }

    private Label createTitle(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 26px; -fx-font-weight: bold;"
        );
        return label;
    }

    private String formatType(AssessmentType type) {
        String value = type.name()
                .replace("_", " ")
                .toLowerCase();

        String[] words = value.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)));
            result.append(word.substring(1));
            result.append(" ");
        }

        return result.toString().trim();
    }

    private void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("StudyFlow");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Scene createStyledScene(
            Parent root,
            double width,
            double height
    ) {
        Scene scene = new Scene(root, width, height);

        java.net.URL css = getClass().getResource("/studyflow.css");
        if (css == null) {
            throw new IllegalStateException(
                    "O ficheiro studyflow.css não foi encontrado em src/main/resources."
            );
        }

        scene.getStylesheets().add(css.toExternalForm());
        return scene;
    }
}
