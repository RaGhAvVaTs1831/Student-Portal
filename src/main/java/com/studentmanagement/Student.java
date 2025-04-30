package com.studentmanagement;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String name;
    private String mobileNumber;
    private String bloodGroup;
    private String address;
    private String course;
    private String program;
    private boolean isHosteller;
    private List<String> courses;
    private String stream;

    public Student(String username, String password, String email) {
        super(username, password, email);
        this.courses = new ArrayList<>();
    }

    public void setPersonalDetails(String name, String mobileNumber, String bloodGroup, String address,
                                 String course, String program, boolean isHosteller, String stream) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.course = course;
        this.program = program;
        this.isHosteller = isHosteller;
        this.stream = stream;
    }

    public void addCourse(String course) {
        courses.add(course);
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public String getCourse() {
        return course;
    }

    public String getProgram() {
        return program;
    }

    public boolean isHosteller() {
        return isHosteller;
    }

    public List<String> getCourses() {
        return courses;
    }

    public String getStream() {
        return stream;
    }
} 