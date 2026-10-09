import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class academicEntity {
    //This superclass of departments courses and programs
    protected String code;
    protected String name;
    protected String description;

    public academicEntity(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
class Course extends academicEntity implements Interface1 {
    private int credits;
    private String semester;
    private double totalGrade;
    private int numberOfStudents;
    private String program;
    private ArrayList<academicMember> academicMembers;
    //This list shows which students are taking this course.
    private ArrayList<Student> students;
    //This map shows how many points the student received in the courses.
    private Map<String, String> coursesGrades;
    // This map shows value of letter grade// courseCode -> grade
    private  Map<String, Double> grades;
    //This map shows how much is taken from each grade
    private Map<String, Integer> amoutOfGrades;

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Course(String code, String name, String description, int credits, String semester, String program) {
        super(code, name, description);
        this.credits = credits;
        this.semester = semester;
        this.program = program;
        this.coursesGrades = new HashMap<>();
        this.students = new ArrayList<>();
        this.academicMembers = new ArrayList<>();
        this.amoutOfGrades = new HashMap<>();
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
    //Those two methods add person who they have taken this course
    public void addStudent(Student student) {
        students.add(student);
    }
    public void addAcademicMember(academicMember academicMember) {
        academicMembers.add(academicMember);
    }
    //This method adds the grades that students get
    public void addGrade(String grade, String id) {
        coursesGrades.put(id, grade);
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    //This method calculates the average by adding up the grades of the students and dividing them by the number of people who received them.
    public double avarageGrade() {
        for (Student student : students) {
            String stİd= student.getId();
            if(coursesGrades.containsKey(stİd)){
                numberOfStudents++;
                String letterGrade = coursesGrades.get(stİd);
                Double gradePoint = grades.get(letterGrade);
                if (gradePoint != null) {
                    totalGrade += gradePoint;
                }
            }
        }

        if (totalGrade == 0) return 0.00;
        return totalGrade / numberOfStudents;
    }
    public void information(String arg) throws IOException {
        // This method show the information about courses like toString method
        FileWriter fw = new FileWriter(arg,true);
        fw.write("Course Code: " + getCode()+"\n");
        fw.write("Name: " + getName()+"\n");
        fw.write("Department: " + getDescription()+"\n");
        fw.write("Credits: " + getCredits()+"\n");
        fw.write("Semester: " + getSemester()+"\n");
        fw.write("\n");
        fw.close();
    }
    public void reports(String arg) throws IOException {
        //This method shows more detailed information.
        FileWriter fw = new FileWriter(arg,true);
        for (String grade: coursesGrades.values()){
            amoutOfGrades.put(grade,amoutOfGrades.getOrDefault(grade,0)+1);
        }
        List<String> orderOfGrade = Arrays.asList("A1", "A2", "B1", "B2", "C1", "C2", "D1", "D2", "F3");
        fw.write("\nCourse Code: " + getCode() + "\n");
        fw.write("Name: " + getName() + "\n");
        fw.write("Department: " + getDescription() + "\n");
        fw.write("Credits: " + getCredits() + "\n");
        fw.write("Semester: " + getSemester() + "\n");
        fw.write("\n");
        fw.write("Instructor: ");
        if (academicMembers.size() <1) {
            fw.write("Not assigned\n");
        }
        else {for (academicMember a : academicMembers) {
            fw.write(a.getName()+"\n");
            }
        }
        fw.write("\n");
        fw.write("Enrolled Students:\n" );

        for (Student student : students) {
            fw.write("- " + student.getName()+" (ID: "+student.getId()+")\n");
        }
        fw.write("\n");
        fw.write("Grade Distribution:\n");
        for (String grade : orderOfGrade) {
            if (amoutOfGrades.containsKey(grade)) {
                fw.write(grade+": "+amoutOfGrades.get(grade)+"\n");
            }
        }
        fw.write("\n");
        fw.write("Average Grade: " +String.format("%.2f",avarageGrade()) +"\n");
        fw.write("\n");
        fw.write("----------------------------------------");
        fw.write("\n");
        fw.close();





    }
}
class Program extends academicEntity implements Interface1 {
    private String department;
    private String degreeLevel;
    private int totalCredits;
    //This list shows the courses in this program.
    private ArrayList<Course> courses;
    //This list makes it easy to print the lessons as a whole.
    private ArrayList<String> cours;

    public Program(String code, String name, String description,String department,String degreeLevel,int totalCredits) {
        super(code, name, description);
        this.department = department;
        this.degreeLevel = degreeLevel;
        this.totalCredits = totalCredits;
        this.courses = new ArrayList<>();
        this.cours = new ArrayList<>();

    }
    //This method add course
    public void addCourse(Course course) {
        courses.add(course);

    }
    public void information(String arg) throws IOException {
        // This method show the information about programs like toString method
        FileWriter fw = new FileWriter(arg,true);
        for (Course c: courses){
            cours.add(c.getCode());
        }
        String coursess = "{"+ String.join(",", cours) + "}";
        fw.write("Program Code: "+getCode()+"\n");
        fw.write("Name: "+getName()+"\n");
        fw.write("Department: "+getDepartment()+"\n");
        fw.write("Degree Level: "+getDegreeLevel()+"\n");
        fw.write("Required Credits: "+getTotalCredits()+"\n");
        fw.write("Courses: ");
        if (courses.size() < 1) {
            fw.write("-\n");

        }
        else {
            fw.write(coursess+"\n");
        }
        fw.write("\n");
        fw.close();


    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }
}
class Department extends academicEntity implements Interface1 {
    private String head;
    public Department(String code, String name, String description, String head) {
        super(code, name, description);
        this.head = head;
    }
    public void information(String arg) throws IOException {
        // This method show the information about departments like toString method
        FileWriter fw = new FileWriter(arg,true);;
        fw.write("Department Code: "+getCode()+"\n");
        fw.write("Name: "+getName()+"\n");


        if(getHead().equals("non")){
            fw.write("Head: Not assigned"+"\n");
            System.out.println();
        }
        else{
            fw.write("Head: "+getHead()+"\n");
        }
        fw.write("\n");
        fw.close();

    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
