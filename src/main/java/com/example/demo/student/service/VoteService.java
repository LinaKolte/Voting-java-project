package com.example.demo.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.student.repository.VoteRepository;

@Service
public class VoteService {
   
    private final VoteRepository voteRepository;


    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
        
        
    }

    @Transactional
    public void syncVotesFromStudentTable() {
        voteRepository.updateTotalVotes();
    }
    public String getElectionResult() {
        return voteRepository.findWinner();
    }
    
    
}
