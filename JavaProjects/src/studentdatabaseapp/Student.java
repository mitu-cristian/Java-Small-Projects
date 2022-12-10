package studentdatabaseapp;
import java.util.Scanner;
public class Student {
    private String firstName;
    private String lastName;
    private int gradeYear;
    private String studentID;
    private String courses = "";
    private int tuitionBalance = 0;
    private static int costOfCourse = 600;
    private static int id = 1000;

//    Constructor prompts user to enter student's name and year
    public Student() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter student first name: ");
        this.firstName = in.nextLine();
        System.out.println("Enter student last name: ");
        this.lastName = in.nextLine();
        System.out.println("1 - Freshmen\n2 - Sophomore\n3 - Junior\n4 - Senior\nEnter student class level: ");
        this.gradeYear = in.nextInt();
        setStudentId();
    }

//    Generate an ID
    private void setStudentId() {
//        Grade Level = ID
        id++;
        this.studentID = gradeYear + "" + id;
    }

//    Enroll in courses
    public void enroll() {
//        Get inside a loop, user hits 0
        do {
            System.out.println("Enter course to enroll (press Q to quit): ");
            Scanner in = new Scanner(System.in);
            String course = in.nextLine();
            if (!course.equals("Q")) {
                if(courses.length() == 0)
                    courses = course;
                else courses = courses + ", " + course;
                tuitionBalance = tuitionBalance + costOfCourse;
            }
            else { break; }
        }   while(1!=0);
    }

//    View balance
    public void viewBalance() {
        System.out.println("Your balance is: $" + tuitionBalance);
    }

//    Pay Tuition
    public void payTuition() {
        viewBalance();
        System.out.println("Enter your payment: $");
        Scanner in = new Scanner (System.in);
        int payment = in.nextInt();
        tuitionBalance = tuitionBalance - payment;
        System.out.println("Thank you for your payment of: $" + payment);
        viewBalance();
    }

//    Show status
    public String showInfo() {
        return "Name: " + firstName + " " + lastName +
                "\nGrade level: " + gradeYear +
                "\nStudent ID: " + studentID +
                "\nCourses Enrolled: " + courses +
                "\nBalance: $" + tuitionBalance;
    }

}


