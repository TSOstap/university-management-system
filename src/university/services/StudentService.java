package university.services;

import university.entities.Student;
import university.enums.StudentStatus;

public class StudentService {
    private Student[] students = new Student[10];
    private int count;
    private int nextId = 1;

    public Student addStudent(String fullName, String email, int studyYear, StudentStatus status) {
        ensureCapacity();
        Student student = new Student(nextId++, fullName, email, studyYear, status);
        students[count++] = student;
        return student;
    }

    public Student[] getAllStudents() {
        Student[] result = new Student[count];
        System.arraycopy(students, 0, result, 0, count);
        return result;
    }

    public Student findById(int id) {
        for (int i = 0; i < count; i++) {
            if (students[i].getId() == id) {
                return students[i];
            }
        }
        return null;
    }

    public Student requireById(int id) {
        Student student = findById(id);
        if (student == null) {
            throw new IllegalArgumentException("студента з ID " + id + " не знайдено");
        }
        return student;
    }

    public void updateStudent(int id, String fullName, String email, int studyYear) {
        Student student = requireById(id);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setStudyYear(studyYear);
    }

    public void changeStatus(int id, StudentStatus status) {
        requireById(id).setStatus(status);
    }

    public void deleteStudent(int id) {
        int index = indexOf(id);
        if (index < 0) {
            throw new IllegalArgumentException("студента з ID " + id + " не знайдено");
        }
        removeAt(index);
    }

    public Student[] filterByStatus(StudentStatus status) {
        Student[] result = new Student[count];
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            if (students[i].getStatus() == status) {
                result[resultCount++] = students[i];
            }
        }
        return trim(result, resultCount);
    }

    public Student[] filterByStudyYear(int studyYear) {
        if (studyYear < 1) {
            throw new IllegalArgumentException("рік навчання має бути не менше 1");
        }
        Student[] result = new Student[count];
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            if (students[i].getStudyYear() == studyYear) {
                result[resultCount++] = students[i];
            }
        }
        return trim(result, resultCount);
    }

    public Student[] search(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("пошуковий запит не може бути порожнім");
        }
        String normalizedQuery = query.trim().toLowerCase();
        Student[] result = new Student[count];
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            Student student = students[i];
            if (student.getFullName().toLowerCase().contains(normalizedQuery)
                    || student.getEmail().toLowerCase().contains(normalizedQuery)) {
                result[resultCount++] = student;
            }
        }
        return trim(result, resultCount);
    }

    public Student[] getStudentsSortedByFullName() {
        Student[] result = getAllStudents();
        for (int end = result.length - 1; end > 0; end--) {
            for (int i = 0; i < end; i++) {
                if (result[i].getFullName().compareToIgnoreCase(result[i + 1].getFullName()) > 0) {
                    Student temporary = result[i];
                    result[i] = result[i + 1];
                    result[i + 1] = temporary;
                }
            }
        }
        return result;
    }

    private int indexOf(int id) {
        for (int i = 0; i < count; i++) {
            if (students[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void removeAt(int index) {
        for (int i = index; i < count - 1; i++) {
            students[i] = students[i + 1];
        }
        students[--count] = null;
    }

    private void ensureCapacity() {
        if (count == students.length) {
            Student[] expanded = new Student[students.length * 2];
            System.arraycopy(students, 0, expanded, 0, students.length);
            students = expanded;
        }
    }

    private Student[] trim(Student[] source, int size) {
        Student[] result = new Student[size];
        System.arraycopy(source, 0, result, 0, size);
        return result;
    }
}
