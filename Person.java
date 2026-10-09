import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class  Person {
    // This is person class it superclass of Students and Academic Members
    private String Id;
    private String Name;
    private String email;
    private String department;
    public Person(String id, String name, String email, String department) {
        this.Id = id;
        this.Name = name;
        this.email = email;
        this.department = department;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    // This is abstract method to use by use student and academic member to add course
    public abstract void addCourse(Course c);
}
class Student extends Person implements Interface1 {
    private double grade;
    //This list shows all courses taken by the student
    private ArrayList<Course> courses;
    //This map shows how many points the student received in the courses.
    private Map<String, String> coursesGrades;// courseCode -> grade
    // This map shows value of letter grade
    private  Map<String, Double> grades;
    //This list shows the student's active courses.
    private ArrayList<Course> enrolledcourses;



    public Student(String id, String name, String email, String department) {
        super(id, name, email, department);

        this.courses = new ArrayList<>();
        this.coursesGrades = new HashMap<>();
        this.enrolledcourses = new ArrayList<>();
        grades = new HashMap<>();
        grades.put("A1", 4.00);
        grades.put("A2", 3.50);
        grades.put("B1", 3.00);
        grades.put("B2", 2.50);
        grades.put("C1", 2.00);
        grades.put("C2", 1.50);
        grades.put("D1", 1.00);
        grades.put("D2", 0.50);
        grades.put("F3", 0.00);

    }
    //This method add course by Student Management System
    @Override
    public void addCourse(Course course) {
        courses.add(course);
    }
    // This code Find the enrolled courses
    public void addEnrolledCourse() {
        for (Course c : courses) {
            String courseCode = c.getCode();

            if (coursesGrades.containsKey(courseCode)==false) {
                enrolledcourses.add(c);
            }
        }
    }
    //This method add grade by Student Management System
    public void addGrade(String grade, String courseCode) {
        coursesGrades.put(courseCode, grade);
    }

    public double gpa() {
        //This code evaluate students gpa by using code below
        double totalGrade = 0;
        int totalCredits = 0;

        for (Course c : courses) {
            String courseCode = c.getCode();
            if (coursesGrades.containsKey(courseCode)) {
                String letterGrade = coursesGrades.get(courseCode);
                Double gradePoint = grades.get(letterGrade);

                if (gradePoint != null) {
                    totalCredits += c.getCredits();
                    totalGrade += gradePoint * c.getCredits();
                }
            }
        }

        if (totalCredits == 0) return 0.00;

        return totalGrade / totalCredits;
    }

    public void information(String argc) throws IOException {
        // This method show the information about student like toString method
        FileWriter fw =new FileWriter(argc,true);
        fw.write("Student ID: " + getId()+"\n");
        fw.write("Name: " + getName()+"\n");
        fw.write("Email: " + getEmail()+"\n");
        fw.write("Major: " + getDepartment()+"\n");
        fw.write("Status: Active\n");
        fw.write("\n");
        fw.close();
    }
    public void reports(String args) throws IOException {
        //This method shows more detailed information.
        FileWriter fw = new FileWriter(args,true);
        addEnrolledCourse();
        fw.write("Student ID: " + getId()+"\n");
        fw.write("Name: " + getName()+"\n");
        fw.write("Email: " + getEmail()+"\n");
        fw.write("Major: " + getDepartment()+"\n");
        fw.write("Status: Active\n");
        fw.write("\n");
        fw.write("\n");
        fw.write("Enrolled Courses:\n");

        for (Course c : enrolledcourses) {
            fw.write("- "+c.getName()+" ("+c.getCode()+")"+"\n");
        }
        fw.write("\n");
        fw.write("Completed Courses:\n");
        for (Course c : courses) {
            String courseCode = c.getCode();
            if (coursesGrades.containsKey(courseCode)) {
                String letterGrade = coursesGrades.get(courseCode);
                fw.write("- "+c.getName()+" ("+c.getCode()+"): "+letterGrade+"\n");
            }
        }
        fw.write("\n");
        fw.write("GPA: ");
        fw.write(String.format("%.2f", gpa())+"\n");
        fw.write("----------------------------------------\n");
        fw.write("\n");
        fw.close();

    }
}
class academicMember extends Person implements Interface1 {
    private ArrayList<Course>courses;
    public academicMember(String id, String name, String email, String department) {
        super(id, name, email, department);
        courses = new ArrayList<>();
    }

    @Override
    //This method add grade by Student Management System
    public void addCourse(Course course) {
        courses.add(course);
    }

    public void information(String arg) throws IOException {
        // This method show the information about academic members like toString method
        FileWriter fw = new FileWriter(arg,true);
        fw.write("Faculty ID: " + getId()+"\n");
        fw.write("Name: " + getName()+"\n");
        fw.write("Email: " + getEmail()+"\n");
        fw.write("Department: " + getDepartment()+"\n");
        fw.write("\n");
        fw.close();
    }
}
