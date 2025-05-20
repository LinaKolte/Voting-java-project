package com.example.demo.student.model;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    
    private String aadharno;
    private LocalDate dob;
    @Transient
    private Integer age;
    private String email;
    private String name;
    private String password;
    @Column(name = "has_voted")
    private boolean hasVoted = false;  // Track if the user has voted
    @Column(name = "candidate_voted")
    private String candidateVoted;  

    public  Student() {
    }

    public Student(String aadharno, LocalDate dob, String email,String name,String password) {
        this.aadharno = aadharno;
        this.dob = dob;
        this.email = email;
        this.name=name;
        this.password=password;
        
     
    }

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    } 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getaadharno() {
        return aadharno;
    }

    public void setaadharno(String aadharno) {
        this.aadharno = aadharno;
    }

    

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }
    public boolean isHasVoted() {
        return hasVoted;
    }
    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public String getCandidateVoted() {
        return candidateVoted;
    }
    public void setCandidateVoted(String candidateVoted) {
        this.candidateVoted = candidateVoted;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" +", aadharno='"+ aadharno +
                ", name='" + dob + '\'' +
                ", dob=" + name+
                ", age=" + age +
                ", email='" + email + '\'' +",password='"+password+", hasVoted=" + hasVoted +
                ", candidateVoted='" + candidateVoted + '\'' +
                '}';
    }
}
