package university.entities;

import java.util.Objects;

public class Course {
    private final int id;
    private String name;
    private int credits;
    private Teacher teacher;

    public Course(int id, String name, int credits, Teacher teacher) {
        if (id < 1) {
            throw new IllegalArgumentException("ID має бути додатним числом");
        }
        this.id = id;
        setName(name);
        setCredits(credits);
        setTeacher(teacher);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("назва курсу не може бути порожньою");
        }
        this.name = name.trim();
    }

    public int getCredits() {
        return credits;
    }

    public final void setCredits(int credits) {
        if (credits < 1) {
            throw new IllegalArgumentException("кількість кредитів має бути не менше 1");
        }
        this.credits = credits;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public final void setTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("викладача курсу не вказано");
        }
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Course)) {
            return false;
        }
        Course course = (Course) object;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{id=" + id
                + ", name='" + name + '\''
                + ", credits=" + credits
                + ", teacher=" + teacher.getFullName() + " (ID " + teacher.getId() + ")}";
    }
}
