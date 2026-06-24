package university.services;

import university.entities.Course;
import university.entities.Enrollment;
import university.entities.Student;
import university.enums.Grade;

public class EnrollmentService {
    private Enrollment[] enrollments = new Enrollment[10];
    private int count;
    private int nextId = 1;

    public Enrollment addEnrollment(Student student, Course course, String semester) {
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("семестр не може бути порожнім");
        }
        for (int i = 0; i < count; i++) {
            Enrollment enrollment = enrollments[i];
            if (enrollment.getStudent().getId() == student.getId()
                    && enrollment.getCourse().getId() == course.getId()
                    && enrollment.getSemester().equalsIgnoreCase(semester.trim())) {
                throw new IllegalArgumentException("таке зарахування вже існує");
            }
        }
        ensureCapacity();
        Enrollment enrollment = new Enrollment(nextId++, student, course, semester);
        enrollments[count++] = enrollment;
        return enrollment;
    }

    public Enrollment[] getAllEnrollments() {
        Enrollment[] result = new Enrollment[count];
        System.arraycopy(enrollments, 0, result, 0, count);
        return result;
    }

    public Enrollment findById(int id) {
        for (int i = 0; i < count; i++) {
            if (enrollments[i].getId() == id) {
                return enrollments[i];
            }
        }
        return null;
    }

    public Enrollment requireById(int id) {
        Enrollment enrollment = findById(id);
        if (enrollment == null) {
            throw new IllegalArgumentException("зарахування з ID " + id + " не знайдено");
        }
        return enrollment;
    }

    public void setGrade(int enrollmentId, Grade grade) {
        requireById(enrollmentId).setGrade(grade);
    }

    public void markAsPaid(int enrollmentId) {
        requireById(enrollmentId).markAsPaid();
    }

    public Enrollment[] findByStudent(int studentId) {
        Enrollment[] result = new Enrollment[count];
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            if (enrollments[i].getStudent().getId() == studentId) {
                result[resultCount++] = enrollments[i];
            }
        }
        return trim(result, resultCount);
    }

    public void removeByStudent(int studentId) {
        int index = 0;
        while (index < count) {
            if (enrollments[index].getStudent().getId() == studentId) {
                removeAt(index);
            } else {
                index++;
            }
        }
    }

    public void removeByCourse(int courseId) {
        int index = 0;
        while (index < count) {
            if (enrollments[index].getCourse().getId() == courseId) {
                removeAt(index);
            } else {
                index++;
            }
        }
    }

    private void removeAt(int index) {
        for (int i = index; i < count - 1; i++) {
            enrollments[i] = enrollments[i + 1];
        }
        enrollments[--count] = null;
    }

    private void ensureCapacity() {
        if (count == enrollments.length) {
            Enrollment[] expanded = new Enrollment[enrollments.length * 2];
            System.arraycopy(enrollments, 0, expanded, 0, enrollments.length);
            enrollments = expanded;
        }
    }

    private Enrollment[] trim(Enrollment[] source, int size) {
        Enrollment[] result = new Enrollment[size];
        System.arraycopy(source, 0, result, 0, size);
        return result;
    }
}
