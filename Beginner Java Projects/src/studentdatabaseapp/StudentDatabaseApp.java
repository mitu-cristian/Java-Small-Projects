package studentdatabaseapp;

import java.util.Scanner;

public class StudentDatabaseApp {

    public static void main(String[] args){

//        Ask how many student we want to add
        System.out.print("Enter the number of students you want to enroll: ");
        Scanner in = new Scanner(System.in);
        int numberOfStudents = in.nextInt();
        Student[] students = new Student[numberOfStudents];

//        Create n number of new students
        for(int n=0; n<numberOfStudents; n++) {
            students[n] = new Student();
            students[n].enroll();
            students[n].payTuition();
        }

        for (int n=0; n<numberOfStudents; n++)
            System.out.println(students[n].showInfo());
    }
}