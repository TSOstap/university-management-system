package university.services;

import university.entities.Course;
import university.entities.Teacher;

public class CourseService {
    private Course[] courses = new Course[10];
    private int count;
    private int nextId = 1;

    public Course addCourse(String name, int credits, Teacher teacher) {
        ensureCapacity();
        Course course = new Course(nextId++, name, credits, teacher);
        courses[count++] = course;
        return course;
    }

    public Course[] getAllCourses() {
        Course[] result = new Course[count];
        System.arraycopy(courses, 0, result, 0, count);
        return result;
    }

    public Course findById(int id) {
        for (int i = 0; i < count; i++) {
            if (courses[i].getId() == id) {
                return courses[i];
            }
        }
        return null;
    }

    public Course requireById(int id) {
        Course course = findById(id);
        if (course == null) {
            throw new IllegalArgumentException("курс з ID " + id + " не знайдено");
        }
        return course;
    }

    public void updateCourse(int id, String name, int credits, Teacher teacher) {
        Course course = requireById(id);
        course.setName(name);
        course.setCredits(credits);
        course.setTeacher(teacher);
    }

    public void deleteCourse(int id) {
        int index = indexOf(id);
        if (index < 0) {
            throw new IllegalArgumentException("курс з ID " + id + " не знайдено");
        }
        for (int i = index; i < count - 1; i++) {
            courses[i] = courses[i + 1];
        }
        courses[--count] = null;
    }

    public Course[] filterByTeacher(int teacherId) {
        Course[] result = new Course[count];
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            if (courses[i].getTeacher().getId() == teacherId) {
                result[resultCount++] = courses[i];
            }
        }
        return trim(result, resultCount);
    }

    public Course[] filterByCredits(int credits) {
        if (credits < 1) {
            throw new IllegalArgumentException("кількість кредитів має бути не менше 1");
        }
        Course[] result = new Course[count];
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            if (courses[i].getCredits() == credits) {
                result[resultCount++] = courses[i];
            }
        }
        return trim(result, resultCount);
    }

    public boolean hasCoursesByTeacher(int teacherId) {
        for (int i = 0; i < count; i++) {
            if (courses[i].getTeacher().getId() == teacherId) {
                return true;
            }
        }
        return false;
    }

    private int indexOf(int id) {
        for (int i = 0; i < count; i++) {
            if (courses[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void ensureCapacity() {
        if (count == courses.length) {
            Course[] expanded = new Course[courses.length * 2];
            System.arraycopy(courses, 0, expanded, 0, courses.length);
            courses = expanded;
        }
    }

    private Course[] trim(Course[] source, int size) {
        Course[] result = new Course[size];
        System.arraycopy(source, 0, result, 0, size);
        return result;
    }
}
