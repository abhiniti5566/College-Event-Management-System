package com.vcet.event;

public class Student {
    private int studentId;
    private String rollNo, name, department;

    public Student(int studentId, String rollNo, String name, String department) {
        this.studentId = studentId; this.rollNo = rollNo;
        this.name = name; this.department = department;
    }
    public int getStudentId() { return studentId; }
    public String getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
}
