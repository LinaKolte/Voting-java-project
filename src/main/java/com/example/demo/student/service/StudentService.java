package com.example.demo.student.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final VoteService voteService;

    @Autowired
    public StudentService(StudentRepository studentRepository, VoteService voteService) { // ✅ Inject VoteService
        this.studentRepository = studentRepository;
        this.voteService = voteService;
    }

    public List<Student> getStudent() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("This email is already taken.");
        }
        studentRepository.save(student);
    }
    

    public Optional<Student> loginUser(String aadharno, String password) {
        Optional<Student> student = studentRepository.findByAadharno(aadharno);
        if (student.isPresent() && student.get().getPassword().equals(password)) {
            return student;
        }
        return Optional.empty();
    }
    public boolean castVote(String aadharno, String candidateVoted) {
        Optional<Student> studentOpt = studentRepository.findByAadharno(aadharno);
    
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (!student.isHasVoted()) { // Ensure the user hasn't voted before
                studentRepository.updateVotingStatus(aadharno, candidateVoted);
                voteService.syncVotesFromStudentTable(); // Update voting status
                return true; // Vote cast successfully
            }
        }
        return false; // Already voted or Aadhaar not found
    }
    

    
    
    

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist."));

        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }
        if (email != null && email.length() > 0) {
            Optional<Student> studentOptional = studentRepository.findStudentByByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("Email is already taken.");
            }
            student.setEmail(email);
        }
    }
    public String getEmailByAadhar(String aadharno) {
        Optional<Student> studentOpt = studentRepository.findByAadharno(aadharno);
        return studentOpt.map(Student::getEmail).orElse(null);
    }
}


