package com.example.demo.student.model;



public class VoteResult {
    private String candidateName;
    private int totalVotes;

    public VoteResult(String candidateName, int totalVotes) {
        this.candidateName = candidateName;
        this.totalVotes = totalVotes;
    }

    // Getters
    public String getCandidateName() {
        return candidateName;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    // Optional: Setters (only if needed)
    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }
}
