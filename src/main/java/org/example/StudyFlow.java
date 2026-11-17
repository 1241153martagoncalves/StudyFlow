package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudyFlow implements Serializable {

    private List<CourseUnit> courseUnits;

    public StudyFlow() {
        this.courseUnits = new ArrayList<>();
    }

    public List<CourseUnit> getCourseUnits() {
        return courseUnits;
    }

    public boolean addCourseUnit(CourseUnit courseUnit) {

        if (courseUnit == null) {
            return false;
        }

        courseUnits.add(courseUnit);

        return true;
    }

    public boolean removeCourseUnit(int index) {

        if (index < 0 || index >= courseUnits.size()) {
            return false;
        }

        courseUnits.remove(index);

        return true;
    }
}