package org.example;

import java.util.List;

public class StudyFlowController {

    private StudyFlow studyFlow;
    private StudyFlowRepository repository;

    public StudyFlowController(
            StudyFlow studyFlow,
            StudyFlowRepository repository
    ) {
        this.studyFlow = studyFlow;
        this.repository = repository;
    }

    public boolean addCourseUnit(
            String name,
            String acronym,
            int ects,
            int year,
            int semester
    ) {

        CourseUnit courseUnit =
                new CourseUnit(name, acronym, ects, year, semester);

        boolean added =
                studyFlow.addCourseUnit(courseUnit);

        if (added) {
            repository.save(studyFlow);
        }

        return added;
    }

    public List<CourseUnit> getCourseUnits() {
        return studyFlow.getCourseUnits();
    }

    public boolean addAssessment(
            int courseUnitIndex,
            AssessmentType type,
            double weight,
            Double grade
    ) {

        if (courseUnitIndex < 0 ||
                courseUnitIndex >= studyFlow.getCourseUnits().size()) {
            return false;
        }

        CourseUnit courseUnit =
                studyFlow.getCourseUnits().get(courseUnitIndex);

        Assessment assessment =
                new Assessment(type, weight, grade);

        boolean added =
                courseUnit.addAssessment(assessment);

        if (added) {
            repository.save(studyFlow);
        }

        return added;
    }

    public boolean removeCourseUnit(int index) {

        boolean removed =
                studyFlow.removeCourseUnit(index);

        if (removed) {
            repository.save(studyFlow);
        }

        return removed;
    }

    public boolean removeAssessment(
            int courseUnitIndex,
            int assessmentIndex
    ) {

        if (courseUnitIndex < 0 ||
                courseUnitIndex >= studyFlow.getCourseUnits().size()) {
            return false;
        }

        CourseUnit courseUnit =
                studyFlow.getCourseUnits().get(courseUnitIndex);

        if (assessmentIndex < 0 ||
                assessmentIndex >= courseUnit.getAssessments().size()) {
            return false;
        }

        courseUnit.getAssessments().remove(assessmentIndex);

        repository.save(studyFlow);

        return true;
    }

    public boolean addGradeToAssessment(
            int courseUnitIndex,
            int assessmentIndex,
            double grade
    ) {

        if (courseUnitIndex < 0 ||
                courseUnitIndex >= studyFlow.getCourseUnits().size()) {
            return false;
        }

        CourseUnit courseUnit =
                studyFlow.getCourseUnits().get(courseUnitIndex);

        if (assessmentIndex < 0 ||
                assessmentIndex >= courseUnit.getAssessments().size()) {
            return false;
        }

        if (grade < 0 || grade > 20) {
            return false;
        }

        Assessment assessment =
                courseUnit.getAssessments().get(assessmentIndex);

        assessment.setGrade(grade);

        repository.save(studyFlow);

        return true;
    }
}