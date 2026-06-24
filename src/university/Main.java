package university;

import university.entities.Course;
import university.entities.Enrollment;
import university.entities.Student;
import university.entities.Teacher;
import university.enums.Grade;
import university.enums.StudentStatus;
import university.enums.TeacherPosition;
import university.services.CourseService;
import university.services.EnrollmentService;
import university.services.StudentService;
import university.services.TeacherService;
import university.util.GPAUtils;

import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentService = new StudentService();
    private final TeacherService teacherService = new TeacherService();
    private final CourseService courseService = new CourseService();
    private final EnrollmentService enrollmentService = new EnrollmentService();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            try {
                int choice = readInt("Оберіть пункт: ");
                switch (choice) {
                    case 1:
                        studentMenu();
                        break;
                    case 2:
                        teacherMenu();
                        break;
                    case 3:
                        courseMenu();
                        break;
                    case 4:
                        enrollmentMenu();
                        break;
                    case 5:
                        reportsMenu();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Роботу програми завершено.");
                        break;
                    default:
                        System.out.println("Невідомий пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception);
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== Система управління університетом ===");
        System.out.println("1. Студенти");
        System.out.println("2. Викладачі");
        System.out.println("3. Курси");
        System.out.println("4. Зарахування");
        System.out.println("5. Звіти / Пошук");
        System.out.println("0. Вихід");
    }

    private void studentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Студенти ---");
            System.out.println("1. Додати студента");
            System.out.println("2. Показати всіх студентів");
            System.out.println("3. Оновити студента");
            System.out.println("4. Видалити студента");
            System.out.println("5. Змінити статус студента");
            System.out.println("6. Фільтр за статусом");
            System.out.println("7. Фільтр за роком навчання");
            System.out.println("8. Сортувати за ПІБ");
            System.out.println("0. Назад");
            try {
                int choice = readInt("Оберіть пункт: ");
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        printStudents(studentService.getAllStudents());
                        break;
                    case 3:
                        updateStudent();
                        break;
                    case 4:
                        deleteStudent();
                        break;
                    case 5:
                        changeStudentStatus();
                        break;
                    case 6:
                        printStudents(studentService.filterByStatus(readStudentStatus()));
                        break;
                    case 7:
                        printStudents(studentService.filterByStudyYear(readInt("Рік навчання: ")));
                        break;
                    case 8:
                        printStudents(studentService.getStudentsSortedByFullName());
                        break;
                    case 0:
                        inMenu = false;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception);
            }
        }
    }

    private void addStudent() {
        String fullName = readText("ПІБ: ");
        String email = readText("Email: ");
        int studyYear = readInt("Рік навчання: ");
        StudentStatus status = readStudentStatus();
        Student student = studentService.addStudent(fullName, email, studyYear, status);
        System.out.println("Студента додано. ID: " + student.getId());
    }

    private void updateStudent() {
        int id = readInt("ID студента: ");
        String fullName = readText("Нове ПІБ: ");
        String email = readText("Новий email: ");
        int studyYear = readInt("Новий рік навчання: ");
        studentService.updateStudent(id, fullName, email, studyYear);
        System.out.println("Дані студента оновлено.");
    }

    private void deleteStudent() {
        int id = readInt("ID студента: ");
        studentService.requireById(id);
        enrollmentService.removeByStudent(id);
        studentService.deleteStudent(id);
        System.out.println("Студента та його зарахування видалено.");
    }

    private void changeStudentStatus() {
        int id = readInt("ID студента: ");
        studentService.changeStatus(id, readStudentStatus());
        System.out.println("Статус студента змінено.");
    }

    private void teacherMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Викладачі ---");
            System.out.println("1. Додати викладача");
            System.out.println("2. Показати всіх викладачів");
            System.out.println("3. Оновити викладача");
            System.out.println("4. Видалити викладача");
            System.out.println("0. Назад");
            try {
                int choice = readInt("Оберіть пункт: ");
                switch (choice) {
                    case 1:
                        addTeacher();
                        break;
                    case 2:
                        printTeachers(teacherService.getAllTeachers());
                        break;
                    case 3:
                        updateTeacher();
                        break;
                    case 4:
                        deleteTeacher();
                        break;
                    case 0:
                        inMenu = false;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception);
            }
        }
    }

    private void addTeacher() {
        String fullName = readText("ПІБ: ");
        String email = readText("Email: ");
        TeacherPosition position = readTeacherPosition();
        Teacher teacher = teacherService.addTeacher(fullName, email, position);
        System.out.println("Викладача додано. ID: " + teacher.getId());
    }

    private void updateTeacher() {
        int id = readInt("ID викладача: ");
        String fullName = readText("Нове ПІБ: ");
        String email = readText("Новий email: ");
        TeacherPosition position = readTeacherPosition();
        teacherService.updateTeacher(id, fullName, email, position);
        System.out.println("Дані викладача оновлено.");
    }

    private void deleteTeacher() {
        int id = readInt("ID викладача: ");
        teacherService.deleteTeacher(id, courseService);
        System.out.println("Викладача видалено.");
    }

    private void courseMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Курси ---");
            System.out.println("1. Додати курс");
            System.out.println("2. Показати всі курси");
            System.out.println("3. Оновити курс");
            System.out.println("4. Видалити курс");
            System.out.println("5. Фільтр за викладачем");
            System.out.println("6. Фільтр за кількістю кредитів");
            System.out.println("0. Назад");
            try {
                int choice = readInt("Оберіть пункт: ");
                switch (choice) {
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        printCourses(courseService.getAllCourses());
                        break;
                    case 3:
                        updateCourse();
                        break;
                    case 4:
                        deleteCourse();
                        break;
                    case 5:
                        filterCoursesByTeacher();
                        break;
                    case 6:
                        printCourses(courseService.filterByCredits(readInt("Кількість кредитів: ")));
                        break;
                    case 0:
                        inMenu = false;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception);
            }
        }
    }

    private void addCourse() {
        String name = readText("Назва курсу: ");
        int credits = readInt("Кількість кредитів: ");
        Teacher teacher = teacherService.requireById(readInt("ID викладача: "));
        Course course = courseService.addCourse(name, credits, teacher);
        System.out.println("Курс додано. ID: " + course.getId());
    }

    private void updateCourse() {
        int id = readInt("ID курсу: ");
        String name = readText("Нова назва: ");
        int credits = readInt("Нова кількість кредитів: ");
        Teacher teacher = teacherService.requireById(readInt("ID нового викладача: "));
        courseService.updateCourse(id, name, credits, teacher);
        System.out.println("Дані курсу оновлено.");
    }

    private void deleteCourse() {
        int id = readInt("ID курсу: ");
        courseService.requireById(id);
        enrollmentService.removeByCourse(id);
        courseService.deleteCourse(id);
        System.out.println("Курс та пов'язані зарахування видалено.");
    }

    private void filterCoursesByTeacher() {
        int teacherId = readInt("ID викладача: ");
        teacherService.requireById(teacherId);
        printCourses(courseService.filterByTeacher(teacherId));
    }

    private void enrollmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Зарахування ---");
            System.out.println("1. Створити зарахування");
            System.out.println("2. Поставити оцінку");
            System.out.println("3. Позначити оплату");
            System.out.println("4. Зарахування студента та GPA");
            System.out.println("5. Транскрипт студента");
            System.out.println("0. Назад");
            try {
                int choice = readInt("Оберіть пункт: ");
                switch (choice) {
                    case 1:
                        addEnrollment();
                        break;
                    case 2:
                        setEnrollmentGrade();
                        break;
                    case 3:
                        payEnrollment();
                        break;
                    case 4:
                        showStudentEnrollments();
                        break;
                    case 5:
                        showTranscript();
                        break;
                    case 0:
                        inMenu = false;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception);
            }
        }
    }

    private void addEnrollment() {
        Student student = studentService.requireById(readInt("ID студента: "));
        Course course = courseService.requireById(readInt("ID курсу: "));
        String semester = readText("Семестр: ");
        Enrollment enrollment = enrollmentService.addEnrollment(student, course, semester);
        System.out.println("Зарахування створено. ID: " + enrollment.getId());
    }

    private void setEnrollmentGrade() {
        int enrollmentId = readInt("ID зарахування: ");
        enrollmentService.setGrade(enrollmentId, readGrade());
        System.out.println("Оцінку встановлено.");
    }

    private void payEnrollment() {
        int enrollmentId = readInt("ID зарахування: ");
        enrollmentService.markAsPaid(enrollmentId);
        System.out.println("Оплату позначено.");
    }

    private void showStudentEnrollments() {
        Student student = studentService.requireById(readInt("ID студента: "));
        Enrollment[] enrollments = enrollmentService.findByStudent(student.getId());
        printEnrollments(enrollments);
        printGpa(student, enrollments);
    }

    private void showTranscript() {
        Student student = studentService.requireById(readInt("ID студента: "));
        Enrollment[] enrollments = enrollmentService.findByStudent(student.getId());
        System.out.println("\n=== Транскрипт студента ===");
        System.out.println(student);
        if (enrollments.length == 0) {
            System.out.println("Зарахувань не знайдено.");
        } else {
            for (Enrollment enrollment : enrollments) {
                System.out.println(enrollment.getCourse().getName()
                        + " | семестр: " + enrollment.getSemester()
                        + " | кредити: " + enrollment.getCourse().getCredits()
                        + " | оцінка: " + enrollment.getGrade()
                        + " | оплачено: " + (enrollment.isPaid() ? "так" : "ні"));
            }
        }
        printGpa(student, enrollments);
    }

    private void reportsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Звіти / Пошук ---");
            System.out.println("1. Пошук студента");
            System.out.println("2. Студенти з неоплаченими курсами");
            System.out.println("3. Середній GPA за курсом");
            System.out.println("4. Середній GPA за семестром");
            System.out.println("5. Топ-N студентів за GPA");
            System.out.println("0. Назад");
            try {
                int choice = readInt("Оберіть пункт: ");
                switch (choice) {
                    case 1:
                        printStudents(studentService.search(readText("Частина ПІБ або email: ")));
                        break;
                    case 2:
                        showStudentsWithUnpaidCourses();
                        break;
                    case 3:
                        showCourseGpa();
                        break;
                    case 4:
                        showSemesterGpa();
                        break;
                    case 5:
                        showTopStudents();
                        break;
                    case 0:
                        inMenu = false;
                        break;
                    default:
                        System.out.println("Невідомий пункт меню.");
                }
            } catch (IllegalArgumentException exception) {
                printError(exception);
            }
        }
    }

    private void showStudentsWithUnpaidCourses() {
        Student[] students = studentService.getAllStudents();
        Enrollment[] enrollments = enrollmentService.getAllEnrollments();
        boolean found = false;
        for (Student student : students) {
            boolean hasUnpaidCourse = false;
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudent().getId() == student.getId() && !enrollment.isPaid()) {
                    hasUnpaidCourse = true;
                    break;
                }
            }
            if (hasUnpaidCourse) {
                System.out.println(student);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Студентів з неоплаченими курсами не знайдено.");
        }
    }

    private void showCourseGpa() {
        Course course = courseService.requireById(readInt("ID курсу: "));
        double gpa = GPAUtils.calculateCourseGpa(enrollmentService.getAllEnrollments(), course.getId());
        printAverageGpa("Середній GPA курсу " + course.getName(), gpa);
    }

    private void showSemesterGpa() {
        String semester = readText("Семестр: ");
        double gpa = GPAUtils.calculateSemesterGpa(enrollmentService.getAllEnrollments(), semester);
        printAverageGpa("Середній GPA за семестр " + semester, gpa);
    }

    private void showTopStudents() {
        int limit = readInt("Кількість студентів N: ");
        Enrollment[] enrollments = enrollmentService.getAllEnrollments();
        Student[] students = GPAUtils.topStudents(studentService.getAllStudents(), enrollments, limit);
        if (students.length == 0) {
            System.out.println("Немає студентів з виставленими оцінками.");
            return;
        }
        for (int i = 0; i < students.length; i++) {
            double gpa = GPAUtils.calculateStudentGpa(enrollments, students[i].getId());
            System.out.printf("%d. %s | GPA: %.2f%n", i + 1, students[i], gpa);
        }
    }

    private StudentStatus readStudentStatus() {
        System.out.println("Доступні статуси: ACTIVE, ON_LEAVE, EXPELLED, GRADUATED");
        String value = readText("Статус: ").toUpperCase();
        try {
            return StudentStatus.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("невідомий статус студента");
        }
    }

    private TeacherPosition readTeacherPosition() {
        System.out.println("Доступні посади: ASSISTANT, LECTURER, PROFESSOR");
        String value = readText("Посада: ").toUpperCase();
        try {
            return TeacherPosition.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("невідома посада викладача");
        }
    }

    private Grade readGrade() {
        System.out.println("Доступні оцінки: A, B, C, D, F, NA");
        String value = readText("Оцінка: ").toUpperCase();
        try {
            return Grade.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("невідома оцінка");
        }
    }

    private int readInt(String prompt) {
        String value = readText(prompt);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("потрібно ввести ціле число");
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("значення не може бути порожнім");
        }
        return value;
    }

    private void printStudents(Student[] students) {
        if (students.length == 0) {
            System.out.println("Студентів не знайдено.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void printTeachers(Teacher[] teachers) {
        if (teachers.length == 0) {
            System.out.println("Викладачів не знайдено.");
            return;
        }
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }

    private void printCourses(Course[] courses) {
        if (courses.length == 0) {
            System.out.println("Курсів не знайдено.");
            return;
        }
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private void printEnrollments(Enrollment[] enrollments) {
        if (enrollments.length == 0) {
            System.out.println("Зарахувань не знайдено.");
            return;
        }
        for (Enrollment enrollment : enrollments) {
            System.out.println(enrollment);
        }
    }

    private void printGpa(Student student, Enrollment[] enrollments) {
        double gpa = GPAUtils.calculateStudentGpa(enrollments, student.getId());
        if (Double.isNaN(gpa)) {
            System.out.println("GPA не розраховано: немає виставлених оцінок.");
        } else {
            System.out.printf("GPA студента: %.2f%n", gpa);
        }
    }

    private void printAverageGpa(String label, double gpa) {
        if (Double.isNaN(gpa)) {
            System.out.println("GPA не розраховано: немає виставлених оцінок.");
        } else {
            System.out.printf("%s: %.2f%n", label, gpa);
        }
    }

    private void printError(IllegalArgumentException exception) {
        System.out.println("Помилка: " + exception.getMessage());
    }
}
