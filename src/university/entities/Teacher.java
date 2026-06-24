package university.entities;

import university.enums.TeacherPosition;

public class Teacher extends Person {
    private TeacherPosition position;

    public Teacher(int id, String fullName, String email, TeacherPosition position) {
        super(id, fullName, email);
        setPosition(position);
    }

    public TeacherPosition getPosition() {
        return position;
    }

    public final void setPosition(TeacherPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("посаду викладача не вказано");
        }
        this.position = position;
    }

    @Override
    public String toString() {
        return "Teacher{id=" + getId()
                + ", fullName='" + getFullName() + '\''
                + ", email='" + getEmail() + '\''
                + ", position=" + position + '}';
    }
}
