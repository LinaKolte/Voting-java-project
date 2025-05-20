package com.example.demo.student.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.student.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByByEmail(String email);  // Fixed Typo

    Optional<Student> findByAadharno(String aadharno);

    @Modifying
@Transactional
@Query("UPDATE Student s SET s.hasVoted = true, s.candidateVoted = ?2 WHERE s.aadharno = ?1 AND s.hasVoted = false")
int updateVotingStatus(String aadharno, String candidateVoted);

}
