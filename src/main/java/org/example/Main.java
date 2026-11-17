package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        StudyFlowRepository repository =
                new StudyFlowRepository();

        StudyFlow studyFlow =
                repository.load();

        StudyFlowController controller =
                new StudyFlowController(studyFlow, repository);

        Scanner sc = new Scanner(System.in);

        System.out.println("Choose interface:");
        System.out.println("1. Console");
        System.out.println("2. JavaFX");

        int option = sc.nextInt();

        if (option == 1) {

            StudyFlowConsoleUI ui =
                    new StudyFlowConsoleUI(controller);

            ui.run();

        } else if (option == 2) {

            StudyFlowJavaFXApp.setController(controller);
            StudyFlowJavaFXApp.launchApp();

        } else {

            System.out.println("Invalid option.");
        }
    }
}