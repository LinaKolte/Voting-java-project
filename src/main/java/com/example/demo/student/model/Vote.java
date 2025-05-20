package com.example.demo.student.model;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
     @Column(name = "candidate_name")
    private String candidateName;
    @Column(name = "total_votes")
    private int totalVotes;
    
    @Column(name = "vote_time", nullable = false)
    private LocalDateTime voteTime;


    public Vote() {}

    public Vote(String candidateName, int totalVotes,LocalDateTime voteTime) {
        this.candidateName = candidateName;
        this.totalVotes = totalVotes;
        this.voteTime = voteTime;
    }

     @PrePersist
    public void prePersist() {
        this.voteTime = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }
}