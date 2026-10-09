import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
// This is brain of the System everything is controlled here
public class studendManagementSystem {
    //These lists are the lists where entities are recorded.
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private ArrayList<academicMember> academicMembers;
    private ArrayList<Department> departments;
    private ArrayList<Program> programs;
    private ArrayList<String> letterOfGrades;
    //These help with error exceptions.
    boolean test;
    boolean test2;
    boolean test3;


    public studendManagementSystem() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        academicMembers = new ArrayList<>();
        departments = new ArrayList<>();
        programs = new ArrayList<>();
        test = true;
        test2 = true;
        test3 = true;
        letterOfGrades = new ArrayList<>();
        letterOfGrades.add("A1");
        letterOfGrades.add("A2");
        letterOfGrades.add("B1");
        letterOfGrades.add("B2");
        letterOfGrades.add("C1");
        letterOfGrades.add("C2");
        letterOfGrades.add("D1");
        letterOfGrades.add("D2");
        letterOfGrades.add("F3");

    }

    public void addStudent(String id, String name, String email, String department) {
        Student s = new Student(id, name, email, department);
        students.add(s);

    }
    public void addAcademicMember(String id, String name, String email, String department) {
        academicMembers.add(new academicMember(id, name, email, department));
    }
    public void addCourse(String code, String name, String description,int credits, String semester,String program) {
        Course c= new Course(code,name,description,credits,semester,program);
        courses.add(c);
    }
    public void addDepartment(String code, String name, String description, String head) {
        Department d= new Department(code,name,description,head);
        departments.add(d);
    }
    public void addProgram(String code, String name, String description,String department,String degreeLevel,int totalCredits) {
        Program p= new Program(code,name,description,department,degreeLevel,totalCredits);
        programs.add(p);
    }
    //This method shows how many points students received in that course and add them in course.
    public void addGradeCourse(String grade,String id,String course) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                for (Course c : courses) {
                    if (c.getCode().equals(course)) {
                        for (String str : letterOfGrades) {
                            if (grade.equals(str)) {

                                c.addGrade(grade, id);
                                break;
                            }
                        }

                    }
                }

            }
        }

    }
    //In this method, the points students receive from the courses are added up and errors are recorded as exceptions.
    public void addGradeStudent(String grade,String id,String course,String argv) throws IOException {
        FileWriter fw = new FileWriter(argv,true);
        try {

            test=true;
            test2=true;
            test3=true;
            for(Student s: students){
                if (s.getId().equals(id)){
                    test=false;
                    for (Course c: courses){
                        if (c.getCode().equals(course)){
                            test2=false;
                            for(String str: letterOfGrades){
                                if(grade.equals(str)){
                                    test3=false;
                                    s.addGrade(grade,course);
                                    break;
                                }
                            }
                            if(test3){
                                throw new wrongCase("The grade "+grade+" is not valid"+"\n");
                            }
                        }
                    }
                    if (test2){
                        throw new wrongCase("Course "+course+" Not Found"+"\n");
                    }
                }
            }
            if (test){
                throw new wrongCase("Student Not Found with ID "+id+"\n");
            }
        } catch (wrongCase e) {
            fw.write(e.getMessage());
        }
        fw.close();

    }
    //This method reads the files in order and prints the required ones and errors are recorded as exceptions.
    public void read(String argv0,String argv1,String argv2,String argv3,String argv4,String argv5,String argv6  ) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(argv0))) {
            FileWriter fw = new FileWriter(argv6,true);
            fw.write("Reading Person Information\n");;
            String line;

            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                try {
                    if(parts[0].equals("S")){
                        addStudent(parts[1],parts[2],parts[3],parts[4] );
                    }
                    else if(parts[0].equals("F")){
                        addAcademicMember(parts[1],parts[2],parts[3],parts[4]);
                    }
                    else {
                        throw new invalidPersonType("Invalid Person Type");
                    }

                }
                catch (invalidPersonType i ) {
                    fw.write(i.getMessage()+"\n");
                }

            }
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(argv1))) {
            String line;
            FileWriter fw = new FileWriter(argv6,true);
            fw.write("Reading Departments\n");;
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                try {
                    test=true;
                    for (academicMember a: academicMembers){
                        if (a.getId().equals(parts[3])){
                            test=false;
                            addDepartment(parts[0],parts[1],parts[2],a.getName());
                        }
                    }
                    if (test){
                        addDepartment(parts[0],parts[1],parts[2],"non");
                        throw new invalidPersonType("Academic Member Not Found with ID "+parts[3]);
                    }

                }
                catch (invalidPersonType i){
                    fw.write(i.getMessage()+"\n");
                }

                fw.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(argv2))) {
            String line;
            FileWriter fw = new FileWriter(argv6,true);
            fw.write("Reading Programs\n");;
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                addProgram(parts[0],parts[1],parts[2],parts[3],parts[4],Integer.parseInt(parts[5]) );
            }
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(argv3))) {
            String line;
            FileWriter fw = new FileWriter(argv6,true);
            fw.write("Reading Courses\n");;

            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                try {
                    test=true;
                    for (Program p: programs){
                        if (p.getCode().equals(parts[5])){
                            test=false;
                            //The reason I added both the program and the course here is to make it easier to process.
                            addCourse(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4],parts[5]);
                            p.addCourse(new Course(parts[0],parts[1],parts[2],Integer.parseInt(parts[3]),parts[4],parts[5]));
                        }
                    }
                    if (test){
                        throw new invalidProgram("Program "+parts[5]+" Not Found");
                    }
                } catch (invalidProgram e) {
                    fw.write(e.getMessage()+"\n");
                }

            }
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(argv4))) {
            String line;
            FileWriter fw = new FileWriter(argv6,true);
            fw.write("Reading Course Assignments\n");;
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                try {
                    if(parts[0].equals("S")){
                        test=true;
                        test2=true;
                        try {
                            for (Student s: students){
                                if (parts[1].equals(s.getId())){
                                    test = false;
                                    for (Course c: courses){
                                        if (c.getCode().equals(parts[2])){
                                            test2 = false;
                                            //The reason I added both the student and the course here is to make it easier to process.
                                            s.addCourse(c);
                                            c.addStudent(s);
                                        }
                                    }
                                    if (test2){throw new wrongCase("Course "+parts[2]+" Not Found");}
                                }
                            }
                            if (test){throw new wrongCase("Student Not Found with ID "+parts[1]);}

                        }
                        catch (wrongCase w){
                            fw.write(w.getMessage()+"\n");

                        }

                    }
                    else if(parts[0].equals("F")){
                        test=true;
                        test2=true;
                        try {
                            for (academicMember a: academicMembers){
                                if (parts[1].equals(a.getId())){
                                    test=false;
                                    for (Course c: courses){
                                        if (c.getCode().equals(parts[2])){
                                            test2 = false;
                                            //The reason I added both the academic member and the course here is to make it easier to process.
                                            a.addCourse(c);
                                            c.addAcademicMember(a);
                                        }

                                    }
                                    if (test2){throw new wrongCase("Course "+parts[2]+" Not Found");}


                                }

                            }
                            if (test){throw new wrongCase("Academic Member Not Found with ID "+parts[1]);}


                        }
                        catch (wrongCase w){
                            fw.write(w.getMessage()+"\n");
                        }

                    }
                    else {
                        throw new invalidPersonType("Invalid Person Type");
                    }
                }
                catch (invalidPersonType i){
                    fw.write(i.getMessage()+"\n");
                }


            }
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(argv5))) {
            String line;
            FileWriter fw = new FileWriter(argv6,true);
            fw.write("Reading Grades\n");;
            fw.close();
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                //Here, grades are added to both students and courses for easy processing.
                //It is enough to write only one of them the error exceptions.
                addGradeStudent(parts[0],parts[1],parts[2],argv6);
                addGradeCourse(parts[0],parts[1],parts[2]);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        // Below code write required things by obey the assigment
        FileWriter fw = new FileWriter(argv6,true);
        fw.write("----------------------------------------\n" +
                "            Academic Members\n" +
                "----------------------------------------\n");;
        fw.close();
        for (academicMember a: academicMembers){
            a.information(argv6);
        }
        FileWriter fw2 = new FileWriter(argv6,true);
        fw2.write("----------------------------------------\n" +
                "\n" +
                "----------------------------------------\n" +
                "                STUDENTS\n" +
                "----------------------------------------\n");
        fw2.close();
        for (Student s: students){
            s.information(argv6);
        }
        FileWriter fw3 = new FileWriter(argv6,true);
        fw3.write(
                "----------------------------------------\n" +
                "\n" +
                "---------------------------------------\n" +
                "              DEPARTMENTS\n" +
                "---------------------------------------\n");
        fw3.close();

        for (Department d: departments){
            d.information(argv6);
        }

        Collections.sort(programs, Comparator.comparing(program -> program.code));
        FileWriter fw4 = new FileWriter(argv6,true);
        fw4.write("----------------------------------------\n" +
                "\n" +
                "--------------------------------------\n" +
                "                PROGRAMS\n" +
                "---------------------------------------\n");
        fw4.close();
        for (Program p: programs){

            p.information(argv6);
        }

        FileWriter fw5 = new FileWriter(argv6,true);
        fw5.write("----------------------------------------\n" +
                "\n" +
                "---------------------------------------\n" +
                "                COURSES\n" +
                "---------------------------------------\n");
        fw5.close();
        Collections.sort(courses, Comparator.comparing(course -> course.code));
        for (Course c: courses){
            c.information(argv6);
        }

        FileWriter fw6 = new FileWriter(argv6,true);
        fw6.write("----------------------------------------\n" +
                "\n" +
                "----------------------------------------\n" +
                "             COURSE REPORTS\n" +
                "----------------------------------------");
        fw6.close();
        for (Course c: courses){
            c.reports(argv6);
        }
        FileWriter fw7 = new FileWriter(argv6,true);
        fw7.write(
                "\n----------------------------------------\n" +
                "            STUDENT REPORTS\n" +
                "----------------------------------------\n");
        fw7.close();
        for (Student s: students){
            s.reports(argv6);
        }
        fw.close();
    }
}
