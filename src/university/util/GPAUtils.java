package university.util;

import university.entities.Enrollment;
import university.entities.Student;

public final class GPAUtils {
    private GPAUtils() {
    }

    public static double calculateStudentGpa(Enrollment[] enrollments, int studentId) {
        double weightedPoints = 0.0;
        int totalCredits = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudent().getId() == studentId && enrollment.getGrade().isGraded()) {
                int credits = enrollment.getCourse().getCredits();
                weightedPoints += enrollment.getGrade().getPoints() * credits;
                totalCredits += credits;
            }
        }
        return totalCredits == 0 ? Double.NaN : weightedPoints / totalCredits;
    }

    public static double calculateCourseGpa(Enrollment[] enrollments, int courseId) {
        double points = 0.0;
        int gradesCount = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourse().getId() == courseId && enrollment.getGrade().isGraded()) {
                points += enrollment.getGrade().getPoints();
                gradesCount++;
            }
        }
        return gradesCount == 0 ? Double.NaN : points / gradesCount;
    }

    public static double calculateSemesterGpa(Enrollment[] enrollments, String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("семестр не може бути порожнім");
        }
        double weightedPoints = 0.0;
        int totalCredits = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getSemester().equalsIgnoreCase(semester.trim())
                    && enrollment.getGrade().isGraded()) {
                int credits = enrollment.getCourse().getCredits();
                weightedPoints += enrollment.getGrade().getPoints() * credits;
                totalCredits += credits;
            }
        }
        return totalCredits == 0 ? Double.NaN : weightedPoints / totalCredits;
    }

    public static Student[] topStudents(Student[] students, Enrollment[] enrollments, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("N має бути не менше 1");
        }
        Student[] gradedStudents = new Student[students.length];
        int gradedCount = 0;
        for (Student student : students) {
            if (!Double.isNaN(calculateStudentGpa(enrollments, student.getId()))) {
                gradedStudents[gradedCount++] = student;
            }
        }

        for (int end = gradedCount - 1; end > 0; end--) {
            for (int i = 0; i < end; i++) {
                double currentGpa = calculateStudentGpa(enrollments, gradedStudents[i].getId());
                double nextGpa = calculateStudentGpa(enrollments, gradedStudents[i + 1].getId());
                if (currentGpa < nextGpa) {
                    Student temporary = gradedStudents[i];
                    gradedStudents[i] = gradedStudents[i + 1];
                    gradedStudents[i + 1] = temporary;
                }
            }
        }

        int resultSize = Math.min(limit, gradedCount);
        Student[] result = new Student[resultSize];
        System.arraycopy(gradedStudents, 0, result, 0, resultSize);
        return result;
    }
}
