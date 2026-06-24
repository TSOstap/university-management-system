package university.entities;

import university.enums.StudentStatus;

public class Student extends Person {
    private int studyYear;
    private StudentStatus status;

    public Student(int id, String fullName, String email, int studyYear, StudentStatus status) {
        super(id, fullName, email);
        setStudyYear(studyYear);
        setStatus(status);
    }

    public int getStudyYear() {
        return studyYear;
    }

    public final void setStudyYear(int studyYear) {
        if (studyYear < 1) {
            throw new IllegalArgumentException("рік навчання має бути не менше 1");
        }
        this.studyYear = studyYear;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public final void setStatus(StudentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("статус студента не вказано");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{id=" + getId()
                + ", fullName='" + getFullName() + '\''
                + ", email='" + getEmail() + '\''
                + ", studyYear=" + studyYear
                + ", status=" + status + '}';
    }
}
