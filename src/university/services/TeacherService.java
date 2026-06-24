package university.services;

import university.entities.Teacher;
import university.enums.TeacherPosition;

public class TeacherService {
    private Teacher[] teachers = new Teacher[10];
    private int count;
    private int nextId = 1;

    public Teacher addTeacher(String fullName, String email, TeacherPosition position) {
        ensureCapacity();
        Teacher teacher = new Teacher(nextId++, fullName, email, position);
        teachers[count++] = teacher;
        return teacher;
    }

    public Teacher[] getAllTeachers() {
        Teacher[] result = new Teacher[count];
        System.arraycopy(teachers, 0, result, 0, count);
        return result;
    }

    public Teacher findById(int id) {
        for (int i = 0; i < count; i++) {
            if (teachers[i].getId() == id) {
                return teachers[i];
            }
        }
        return null;
    }

    public Teacher requireById(int id) {
        Teacher teacher = findById(id);
        if (teacher == null) {
            throw new IllegalArgumentException("викладача з ID " + id + " не знайдено");
        }
        return teacher;
    }

    public void updateTeacher(int id, String fullName, String email, TeacherPosition position) {
        Teacher teacher = requireById(id);
        teacher.setFullName(fullName);
        teacher.setEmail(email);
        teacher.setPosition(position);
    }

    public void deleteTeacher(int id, CourseService courseService) {
        Teacher teacher = requireById(id);
        if (courseService.hasCoursesByTeacher(teacher.getId())) {
            throw new IllegalArgumentException("спочатку видаліть або змініть курси цього викладача");
        }
        int index = indexOf(id);
        for (int i = index; i < count - 1; i++) {
            teachers[i] = teachers[i + 1];
        }
        teachers[--count] = null;
    }

    private int indexOf(int id) {
        for (int i = 0; i < count; i++) {
            if (teachers[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void ensureCapacity() {
        if (count == teachers.length) {
            Teacher[] expanded = new Teacher[teachers.length * 2];
            System.arraycopy(teachers, 0, expanded, 0, teachers.length);
            teachers = expanded;
        }
    }
}
