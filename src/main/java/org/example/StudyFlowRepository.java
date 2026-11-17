package org.example;

import java.io.*;

public class StudyFlowRepository {

    private static final String FILE_NAME = "studyflow.dat";

    public void save(StudyFlow studyFlow) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(studyFlow);

        } catch (IOException e) {
            System.out.println("Error saving StudyFlow.");
        }
    }

    public StudyFlow load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            return (StudyFlow) in.readObject();

        } catch (FileNotFoundException e) {
            return new StudyFlow();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading StudyFlow.");
            return new StudyFlow();
        }
    }
}