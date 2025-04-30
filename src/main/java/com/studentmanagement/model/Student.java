package com.studentmanagement.model;

public class Student extends User {
    private String name;
    private String mobileNumber;
    private String bloodGroup;
    private String address;
    private String course;
    private String program;
    private String stream;
    private boolean hosteller;

    public Student(String username, String password, String email) {
        super(username, password, email);
    }

    public void setPersonalDetails(String name, String mobileNumber, String bloodGroup, 
                                 String address, String course, String program, 
                                 boolean hosteller, String stream) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.course = course;
        this.program = program;
        this.hosteller = hosteller;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public boolean isHosteller() {
        return hosteller;
    }

    public void setHosteller(boolean hosteller) {
        this.hosteller = hosteller;
    }
} 