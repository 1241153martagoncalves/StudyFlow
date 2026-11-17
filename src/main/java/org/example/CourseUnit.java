package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseUnit implements Serializable {

    private String name;
    private String acronym;
    private int ects;
    private int year;
    private int semester;

    private List<Assessment> assessments;

    public CourseUnit(String name, String acronym, int ects, int year, int semester) {
        this.name = name;
        this.acronym = acronym;
        this.ects = ects;
        this.year = year;
        this.semester = semester;
        this.assessments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public boolean addAssessment(Assessment assessment) {

        if (assessment == null) {
            return false;
        }

        if (assessment.getWeight() <= 0 || assessment.getWeight() > 100) {
            return false;
        }

        if (getTotalWeight() + assessment.getWeight() > 100.001) {
            return false;
        }

        assessments.add(assessment);

        return true;
    }

    public double getTotalWeight() {

        double totalWeight = 0;

        for (Assessment assessment : assessments) {
            totalWeight += assessment.getWeight();
        }

        return totalWeight;
    }

    public boolean hasCompleteAssessmentWeight() {

        return Math.abs(getTotalWeight() - 100.0) < 0.001;
    }

    public Double calculateCurrentGrade() {

        if (!hasCompleteAssessmentWeight()) {
            return null;
        }

        double finalGrade = 0;

        for (Assessment assessment : assessments) {

            if (assessment.getGrade() == null) {
                return null;
            }

            finalGrade +=
                    assessment.getGrade()
                            * (assessment.getWeight() / 100.0);
        }

        return finalGrade;
    }

    public Double calculateRequiredGrade(double targetGrade) {

        if (targetGrade < 0 || targetGrade > 20) {
            return null;
        }

        if (!hasCompleteAssessmentWeight()) {
            return null;
        }

        double currentContribution = 0;
        double remainingWeight = 0;

        for (Assessment assessment : assessments) {

            if (assessment.getGrade() != null) {

                currentContribution +=
                        assessment.getGrade()
                                * (assessment.getWeight() / 100.0);

            } else {

                remainingWeight += assessment.getWeight();
            }
        }

        if (remainingWeight == 0) {
            return null;
        }

        return (targetGrade - currentContribution)
                / (remainingWeight / 100.0);
    }
}