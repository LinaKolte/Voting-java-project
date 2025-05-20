package com.example.demo.student.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.student.model.Voter;
import com.example.demo.student.repository.VoterRepositorye;

@Service
public class VoterServicee {

    private final VoterRepositorye voterRepositorye;

    @Autowired
    public VoterServicee(VoterRepositorye voterRepositorye) {
        this.voterRepositorye = voterRepositorye;
    }

    // Fetch voter by Aadhaar number (id_number)
    public Optional<Voter> getVoterByAadhaar(String aadharno) {
        return voterRepositorye.findByAadharno(aadharno);
    }

    // Fetch voter by Voter ID
    public Optional<Voter> getVoterByVoterId(String voterId) {
        return voterRepositorye.findByVoterId(voterId);
    }

    // Fetch voter by Aadhaar, DOB, and Email (used for verification)
    public Optional<Voter> getVoterByAadhaarDobEmail(String aadharno, java.time.LocalDate dob, String email) {
        return voterRepositorye.findByAadharnoAndDobAndEmail(aadharno, dob, email);
    }

    // Save or update a voter
    public Voter saveVoter(Voter voter) {
        return voterRepositorye.save(voter);
    }
}

