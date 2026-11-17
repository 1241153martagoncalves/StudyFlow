package org.example;

import java.util.List;
import java.util.Scanner;

public class StudyFlowConsoleUI {

    private StudyFlowController controller;
    private Scanner sc;

    public StudyFlowConsoleUI(StudyFlowController controller) {
        this.controller = controller;
        this.sc = new Scanner(System.in);
    }

    public void run() {

        System.out.println("Welcome to StudyFlow!");

        int option = 0;

        while (option != 4) {

            showMainMenu();

            option = sc.nextInt();

            switch (option) {

                case 1:
                    addCourseUnit();
                    break;

                case 2:
                    listAndSelectCourseUnit();
                    break;

                case 3:
                    removeCourseUnit();
                    break;

                case 4:
                    System.out.println("Exiting StudyFlow...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void showMainMenu() {

        System.out.println("\n------- MAIN MENU -------");
        System.out.println("1. Add a New CourseUnit");
        System.out.println("2. List / Select CourseUnit");
        System.out.println("3. Remove CourseUnit");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private void addCourseUnit() {

        sc.nextLine();

        System.out.print("Enter the name of the CourseUnit: ");
        String name = sc.nextLine();

        System.out.print("Enter the acronym of the CourseUnit: ");
        String acronym = sc.next();

        System.out.print("Enter the ECTS of the CourseUnit: ");
        int ects = sc.nextInt();

        System.out.print("Enter the year of the CourseUnit: ");
        int year = sc.nextInt();

        System.out.print("Enter the semester of the CourseUnit: ");
        int semester = sc.nextInt();

        boolean added = controller.addCourseUnit(
                name,
                acronym,
                ects,
                year,
                semester
        );

        if (added) {
            System.out.println("CourseUnit added successfully!");
        } else {
            System.out.println("Failed to add CourseUnit.");
        }
    }

    private void listAndSelectCourseUnit() {

        List<CourseUnit> courseUnits = controller.getCourseUnits();

        if (courseUnits.isEmpty()) {
            System.out.println("There are no CourseUnits yet.");
            return;
        }

        System.out.println("\n---- CourseUnits ----");

        for (int i = 0; i < courseUnits.size(); i++) {
            System.out.println(
                    (i + 1) + ". "
                            + courseUnits.get(i).getName()
                            + " (" + courseUnits.get(i).getAcronym() + ")"
            );
        }

        System.out.print("Select a CourseUnit: ");
        int option = sc.nextInt();

        if (option < 1 || option > courseUnits.size()) {
            System.out.println("Invalid CourseUnit.");
            return;
        }

        CourseUnit selectedCourseUnit =
                courseUnits.get(option - 1);

        showCourseUnitMenu(selectedCourseUnit, option - 1);
    }

    private void showCourseUnitMenu(
            CourseUnit courseUnit,
            int courseUnitIndex
    ) {

        int option = 0;

        while (option != 8) {

            System.out.println(
                    "\n------- " + courseUnit.getAcronym() + " -------"
            );

            System.out.println("1. View CourseUnit Details");
            System.out.println("2. Add Assessment");
            System.out.println("3. List Assessments");
            System.out.println("4. Add Grade to Assessment");
            System.out.println("5. Calculate Current Grade");
            System.out.println("6. Calculate Required Grade");
            System.out.println("7. Remove Assessment");
            System.out.println("8. Back to Main Menu");

            System.out.print("Choose an option: ");
            option = sc.nextInt();

            switch (option) {

                case 1:
                    showCourseUnitDetails(courseUnit);
                    break;

                case 2:
                    addAssessments(courseUnitIndex);
                    break;

                case 3:
                    listAssessments(courseUnit);
                    break;

                case 4:
                    addGradeToAssessment(courseUnitIndex, courseUnit);
                    break;

                case 5:
                    showCurrentGrade(courseUnit);
                    break;

                case 6:
                    showRequiredGrade(courseUnit);
                    break;

                case 7:
                    removeAssessment(courseUnitIndex, courseUnit);
                    break;

                case 8:
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void showCourseUnitDetails(CourseUnit courseUnit) {

        System.out.println("\n---- CourseUnit Details ----");

        System.out.println("Name: " + courseUnit.getName());
        System.out.println("Acronym: " + courseUnit.getAcronym());
        System.out.println("ECTS: " + courseUnit.getEcts());
        System.out.println("Year: " + courseUnit.getYear());
        System.out.println("Semester: " + courseUnit.getSemester());
    }

    private void addAssessments(int courseUnitIndex) {

        CourseUnit courseUnit =
                controller.getCourseUnits().get(courseUnitIndex);

        String addAnother = "yes";

        while (addAnother.equalsIgnoreCase("yes")) {

            System.out.println("\n---- Assessment Types ----");

            for (int i = 0; i < AssessmentType.values().length; i++) {
                System.out.println(
                        (i + 1) + ". " + AssessmentType.values()[i]
                );
            }

            System.out.print("Select the assessment type: ");
            int typeOption = sc.nextInt();

            if (typeOption < 1 ||
                    typeOption > AssessmentType.values().length) {

                System.out.println("Invalid assessment type.");
                continue;
            }

            AssessmentType selectedType =
                    AssessmentType.values()[typeOption - 1];

            System.out.println(
                    "Current assessment weight: "
                            + courseUnit.getTotalWeight()
                            + "%"
            );

            System.out.println(
                    "Remaining weight available: "
                            + (100 - courseUnit.getTotalWeight())
                            + "%"
            );

            System.out.print(
                    "How much does this assessment weight? (0-100%): "
            );

            double weight = sc.nextDouble();

            if (weight <= 0 ||
                    courseUnit.getTotalWeight() + weight > 100.001) {

                System.out.println(
                        "Invalid weight. Total assessment weight cannot exceed 100%."
                );

                continue;
            }

            System.out.print(
                    "Do you have the grade for this assessment yet? (yes/no): "
            );

            String hasGrade = sc.next();

            Double grade = null;

            if (hasGrade.equalsIgnoreCase("yes")) {

                System.out.print("Enter the grade (0-20): ");
                grade = sc.nextDouble();

                if (grade < 0 || grade > 20) {
                    System.out.println("Invalid grade.");
                    continue;
                }
            }

            boolean added =
                    controller.addAssessment(
                            courseUnitIndex,
                            selectedType,
                            weight,
                            grade
                    );

            if (added) {
                System.out.println("Assessment added successfully!");
            } else {
                System.out.println("Failed to add assessment.");
            }

            if (courseUnit.getTotalWeight() >= 99.999) {

                System.out.println(
                        "Assessment weights now total 100%."
                );

                break;
            }

            System.out.print(
                    "Do you want to add another assessment? (yes/no): "
            );

            addAnother = sc.next();
        }
    }

    private void listAssessments(CourseUnit courseUnit) {

        List<Assessment> assessments =
                courseUnit.getAssessments();

        if (assessments.isEmpty()) {
            System.out.println("There are no assessments yet.");
            return;
        }

        System.out.println("\n---- Assessments ----");

        for (int i = 0; i < assessments.size(); i++) {

            Assessment assessment =
                    assessments.get(i);

            System.out.println(
                    (i + 1)
                            + ". "
                            + assessment.getType()
                            + " | Weight: "
                            + assessment.getWeight()
                            + "%"
                            + " | Grade: "
                            + (
                            assessment.getGrade() == null
                                    ? "Not graded"
                                    : assessment.getGrade()
                    )
            );
        }
    }

    private void removeCourseUnit() {

        List<CourseUnit> courseUnits =
                controller.getCourseUnits();

        if (courseUnits.isEmpty()) {
            System.out.println("There are no CourseUnits to remove.");
            return;
        }

        System.out.println("\n---- Remove CourseUnit ----");

        for (int i = 0; i < courseUnits.size(); i++) {

            System.out.println(
                    (i + 1)
                            + ". "
                            + courseUnits.get(i).getName()
            );
        }

        System.out.print("Select the CourseUnit to remove: ");
        int option = sc.nextInt();

        boolean removed =
                controller.removeCourseUnit(option - 1);

        if (removed) {
            System.out.println("CourseUnit removed successfully!");
        } else {
            System.out.println("Failed to remove CourseUnit.");
        }
    }

    private void removeAssessment(
            int courseUnitIndex,
            CourseUnit courseUnit
    ) {

        List<Assessment> assessments =
                courseUnit.getAssessments();

        if (assessments.isEmpty()) {
            System.out.println("There are no assessments to remove.");
            return;
        }

        listAssessments(courseUnit);

        System.out.print("Select the assessment to remove: ");
        int option = sc.nextInt();

        boolean removed =
                controller.removeAssessment(
                        courseUnitIndex,
                        option - 1
                );

        if (removed) {
            System.out.println("Assessment removed successfully!");
        } else {
            System.out.println("Failed to remove assessment.");
        }
    }

    private void showCurrentGrade(CourseUnit courseUnit) {

        if (!courseUnit.hasCompleteAssessmentWeight()) {

            System.out.println(
                    "\nUnable to calculate the final grade."
            );

            System.out.println(
                    "Current total assessment weight: "
                            + courseUnit.getTotalWeight()
                            + "%"
            );

            System.out.println(
                    "Assessment weights must total 100%."
            );

            return;
        }

        Double currentGrade =
                courseUnit.calculateCurrentGrade();

        if (currentGrade == null) {

            System.out.println(
                    "Unable to calculate the final grade."
            );

            System.out.println(
                    "Some assessments do not have a grade yet."
            );

            return;
        }

        System.out.printf(
                "Final grade: %.2f%n",
                currentGrade
        );
    }

    private void showRequiredGrade(CourseUnit courseUnit) {

        if (courseUnit.getAssessments().isEmpty()) {
            System.out.println("This CourseUnit has no assessments.");
            return;
        }

        if (!courseUnit.hasCompleteAssessmentWeight()) {

            System.out.println(
                    "\nUnable to calculate required grade."
            );

            System.out.println(
                    "Current total assessment weight: "
                            + courseUnit.getTotalWeight()
                            + "%"
            );

            System.out.println(
                    "Assessment weights must total 100%."
            );

            return;
        }

        System.out.print("What final grade do you want? ");
        double targetGrade = sc.nextDouble();

        if (targetGrade < 0 || targetGrade > 20) {
            System.out.println("Invalid target grade.");
            return;
        }

        Double[] originalGrades =
                new Double[courseUnit.getAssessments().size()];

        for (int i = 0; i < courseUnit.getAssessments().size(); i++) {

            originalGrades[i] =
                    courseUnit.getAssessments().get(i).getGrade();
        }

        System.out.print(
                "Do you already have or want to assume any assessment grades? (yes/no): "
        );

        String answer = sc.next();

        while (answer.equalsIgnoreCase("yes")) {

            System.out.println("\n---- Assessments ----");

            for (int i = 0; i < courseUnit.getAssessments().size(); i++) {

                Assessment assessment =
                        courseUnit.getAssessments().get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + assessment.getType()
                                + " | Weight: "
                                + assessment.getWeight()
                                + "%"
                                + " | Grade: "
                                + (
                                assessment.getGrade() == null
                                        ? "Unknown"
                                        : assessment.getGrade()
                        )
                );
            }

            System.out.println(
                    (courseUnit.getAssessments().size() + 1)
                            + ". Finish"
            );

            System.out.print("Select an assessment: ");
            int option = sc.nextInt();

            if (option == courseUnit.getAssessments().size() + 1) {
                break;
            }

            if (option < 1 ||
                    option > courseUnit.getAssessments().size()) {

                System.out.println("Invalid assessment.");
                continue;
            }

            Assessment selectedAssessment =
                    courseUnit.getAssessments().get(option - 1);

            System.out.print(
                    "Enter the grade you have or want to assume for "
                            + selectedAssessment.getType()
                            + ": "
            );

            double grade = sc.nextDouble();

            if (grade < 0 || grade > 20) {
                System.out.println("Invalid grade.");
                continue;
            }

            selectedAssessment.setGrade(grade);

            System.out.print(
                    "Do you want to add another known/assumed grade? (yes/no): "
            );

            answer = sc.next();
        }

        Double requiredGrade =
                courseUnit.calculateRequiredGrade(targetGrade);

        if (requiredGrade == null) {

            System.out.println(
                    "There are no remaining assessments to calculate."
            );

        } else if (requiredGrade > 20) {

            System.out.printf(
                    "%nYou would need an average of %.2f in the remaining assessments.%n",
                    requiredGrade
            );

            System.out.println(
                    "This target grade is not achievable because the required grade is above 20."
            );

        } else if (requiredGrade <= 0) {

            System.out.println(
                    "\nYou have already secured the target grade with the entered grades."
            );

        } else {

            System.out.printf(
                    "%nYou need an average of %.2f in the remaining assessments.%n",
                    requiredGrade
            );
        }

        for (int i = 0; i < courseUnit.getAssessments().size(); i++) {

            courseUnit.getAssessments()
                    .get(i)
                    .setGrade(originalGrades[i]);
        }
    }

    private void addGradeToAssessment(
            int courseUnitIndex,
            CourseUnit courseUnit
    ) {

        List<Assessment> assessments =
                courseUnit.getAssessments();

        if (assessments.isEmpty()) {
            System.out.println("There are no assessments.");
            return;
        }

        System.out.println("\n---- Assessments ----");

        for (int i = 0; i < assessments.size(); i++) {

            Assessment assessment =
                    assessments.get(i);

            System.out.println(
                    (i + 1)
                            + ". "
                            + assessment.getType()
                            + " | Weight: "
                            + assessment.getWeight()
                            + "%"
                            + " | Grade: "
                            + (
                            assessment.getGrade() == null
                                    ? "Not graded"
                                    : assessment.getGrade()
                    )
            );
        }

        System.out.print("Select an assessment: ");
        int option = sc.nextInt();

        if (option < 1 || option > assessments.size()) {
            System.out.println("Invalid assessment.");
            return;
        }

        Assessment selectedAssessment =
                assessments.get(option - 1);

        if (selectedAssessment.getGrade() != null) {

            System.out.println(
                    "This assessment already has grade: "
                            + selectedAssessment.getGrade()
            );

            System.out.print(
                    "Do you want to replace it? (yes/no): "
            );

            String replace = sc.next();

            if (!replace.equalsIgnoreCase("yes")) {
                return;
            }
        }

        System.out.print("Enter the grade (0-20): ");
        double grade = sc.nextDouble();

        if (grade < 0 || grade > 20) {
            System.out.println("Invalid grade.");
            return;
        }

        boolean updated =
                controller.addGradeToAssessment(
                        courseUnitIndex,
                        option - 1,
                        grade
                );

        if (updated) {
            System.out.println("Grade added successfully!");
        } else {
            System.out.println("Failed to add grade.");
        }
    }
}