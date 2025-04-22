package com.example.qlsv.model;

public class Student {
    private String name;
    private String studentId;
    private String className; // Thêm trường lớp

    public Student(String name, String studentId, String className) {
        this.name = name;
        this.studentId = studentId;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
