package com.example.demo.student.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "face_images")
public class FaceImagePair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aadhaar_number", nullable = false)
    private String aadhaarNumber;

    @Column(name = "image1_name")
    private String image1Name;

    @Column(name = "image2_name")
    private String image2Name;

    @Lob
    @Column(name = "image1_data", columnDefinition = "LONGBLOB")
    private byte[] image1Data;

    @Lob
    @Column(name = "image2_data", columnDefinition = "LONGBLOB")
    private byte[] image2Data;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getImage1Name() {
        return image1Name;
    }

    public void setImage1Name(String image1Name) {
        this.image1Name = image1Name;
    }

    public String getImage2Name() {
        return image2Name;
    }

    public void setImage2Name(String image2Name) {
        this.image2Name = image2Name;
    }

    public byte[] getImage1Data() {
        return image1Data;
    }

    public void setImage1Data(byte[] image1Data) {
        this.image1Data = image1Data;
    }

    public byte[] getImage2Data() {
        return image2Data;
    }

    public void setImage2Data(byte[] image2Data) {
        this.image2Data = image2Data;
    }
}