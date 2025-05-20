package com.example.demo.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.student.model.FaceImagePair;

@Repository
public interface FaceImageRepository extends JpaRepository<FaceImagePair, Long> {

    FaceImagePair findByAadhaarNumber(String aadhaarNumber);

    
}

