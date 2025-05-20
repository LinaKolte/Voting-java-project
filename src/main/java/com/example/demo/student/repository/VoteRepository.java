package com.example.demo.student.repository;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.student.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO votes (candidate_name, total_votes) " +
                   "VALUES (?1, 1) ON DUPLICATE KEY UPDATE total_votes = total_votes + 1", 
           nativeQuery = true)
    void updateVoteCount(String candidateName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO votes (candidate_name, total_votes) " +
                   "SELECT candidate_voted, COUNT(*) FROM student WHERE has_voted = TRUE GROUP BY candidate_voted " +
                   "ON DUPLICATE KEY UPDATE total_votes = VALUES(total_votes)", 
           nativeQuery = true)
    void updateTotalVotes();


    @Modifying
    @Transactional
    @Query(value = "SELECT candidate_name FROM votes ORDER BY total_votes DESC LIMIT 1", nativeQuery = true)
String findWinner();





}

