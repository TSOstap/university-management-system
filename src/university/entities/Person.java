package university.entities;

import java.util.Objects;

public abstract class Person {
    private final int id;
    private String fullName;
    private String email;

    protected Person(int id, String fullName, String email) {
        if (id < 1) {
            throw new IllegalArgumentException("ID має бути додатним числом");
        }
        this.id = id;
        setFullName(fullName);
        setEmail(email);
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public final void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ не може бути порожнім");
        }
        this.fullName = fullName.trim();
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        if (email == null || !email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("неправильний формат email");
        }
        this.email = email.trim();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Person person = (Person) object;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
