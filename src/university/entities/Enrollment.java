package university.entities;

import university.enums.Grade;
import university.interfaces.Payable;

import java.util.Objects;

public class Enrollment implements Payable {
    private final int id;
    private final Student student;
    private final Course course;
    private final String semester;
    private Grade grade;
    private boolean paid;

    public Enrollment(int id, Student student, Course course, String semester) {
        if (id < 1) {
            throw new IllegalArgumentException("ID має бути додатним числом");
        }
        if (student == null) {
            throw new IllegalArgumentException("студента не вказано");
        }
        if (course == null) {
            throw new IllegalArgumentException("курс не вказано");
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("семестр не може бути порожнім");
        }
        this.id = id;
        this.student = student;
        this.course = course;
        this.semester = semester.trim();
        this.grade = Grade.NA;
        this.paid = false;
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("оцінку не вказано");
        }
        this.grade = grade;
    }

    @Override
    public boolean isPaid() {
        return paid;
    }

    @Override
    public void markAsPaid() {
        paid = true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Enrollment)) {
            return false;
        }
        Enrollment that = (Enrollment) object;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Enrollment{id=" + id
                + ", student=" + student.getFullName() + " (ID " + student.getId() + ')'
                + ", course=" + course.getName() + " (ID " + course.getId() + ')'
                + ", semester='" + semester + '\''
                + ", grade=" + grade
                + ", paid=" + paid + '}';
    }
}
