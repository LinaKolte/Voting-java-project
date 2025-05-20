

package com.example.demo.student.repository;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.student.model.Voter;

public interface VoterRepositorye extends JpaRepository<Voter, Long> {

    Optional<Voter> findByAadharnoAndDobAndEmail(String aadharno,LocalDate dob,String email);
    Optional<Voter> findByAadharno(String aadharno);

    // Find Voter by Voter ID
    Optional<Voter> findByVoterId(String voterId);
        // For Aadhaar
         // Assumes `aadharno` is a field in your Voter entity
}
