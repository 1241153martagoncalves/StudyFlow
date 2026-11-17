package org.example;

import java.io.Serializable;

public class Assessment  implements Serializable {
    private AssessmentType type;
    private double weight;
    private Double grade;

    public Assessment(AssessmentType type, double weight, Double grade){
        this.type = type;
        this.weight = weight;
        this.grade = grade;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {

        if (grade != null && (grade < 0 || grade > 20)) {
            return;
        }

        this.grade = grade;
    }
}
