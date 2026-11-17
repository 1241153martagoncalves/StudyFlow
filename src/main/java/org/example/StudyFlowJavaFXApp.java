package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class StudyFlowJavaFXApp extends Application {

    private static StudyFlowController controller;

    public static void setController(StudyFlowController controller) {
        StudyFlowJavaFXApp.controller = controller;
    }

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage stage) {

        StudyFlowJavaFXUI ui =
                new StudyFlowJavaFXUI(controller);

        ui.start(stage);
    }
}