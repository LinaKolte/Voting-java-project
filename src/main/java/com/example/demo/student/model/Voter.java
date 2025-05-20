

package com.example.demo.student.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "voters")
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // At the top with other fields
private String voterId;

// Add these getter and setter methods
public String getVoterId() {
    return voterId;
}

public void setVoterId(String voterId) {
    this.voterId = voterId;
}

    // Personal Information
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String gender;

    // Contact Information
    private String address;
    private String address2;
    private String city;
    private String state;
    private String postal;
    private String phone;
    private String email;
    @Column(name = "aadharno", unique = true, nullable = false)
    private String aadharno;

    // Identification
    private String idType;
    

    // Eligibility
    private boolean isCitizen;
    private boolean isAgeEligible;

    // Previous Registration
    private boolean prevRegistered;
    private String prevName;
    private String prevAddress;
    private String prevCity;
    private String prevState;

    // Photo and Signature
    private String photoName;

    @Lob
    private byte[] photoData;

    private String signatureName;

    @Lob
    private byte[] signatureData;

    private LocalDate dateSigned;

    // Constructors
    public Voter() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPostal() { return postal; }
    public void setPostal(String postal) { this.postal = postal; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }

    public String getaadharno() { return aadharno; }
    public void setaadharno(String aadharno) { this.aadharno = aadharno; }

    public boolean isCitizen() { return isCitizen; }
    public void setCitizen(boolean citizen) { isCitizen = citizen; }

    public boolean isAgeEligible() { return isAgeEligible; }
    public void setAgeEligible(boolean ageEligible) { isAgeEligible = ageEligible; }

    public boolean isPrevRegistered() { return prevRegistered; }
    public void setPrevRegistered(boolean prevRegistered) { this.prevRegistered = prevRegistered; }

    public String getPrevName() { return prevName; }
    public void setPrevName(String prevName) { this.prevName = prevName; }

    public String getPrevAddress() { return prevAddress; }
    public void setPrevAddress(String prevAddress) { this.prevAddress = prevAddress; }

    public String getPrevCity() { return prevCity; }
    public void setPrevCity(String prevCity) { this.prevCity = prevCity; }

    public String getPrevState() { return prevState; }
    public void setPrevState(String prevState) { this.prevState = prevState; }

    public String getPhotoName() { return photoName; }
    public void setPhotoName(String photoName) { this.photoName = photoName; }

    public byte[] getPhotoData() { return photoData; }
    public void setPhotoData(byte[] photoData) { this.photoData = photoData; }

    public String getSignatureName() { return signatureName; }
    public void setSignatureName(String signatureName) { this.signatureName = signatureName; }

    public byte[] getSignatureData() { return signatureData; }
    public void setSignatureData(byte[] signatureData) { this.signatureData = signatureData; }

    public LocalDate getDateSigned() { return dateSigned; }
    public void setDateSigned(LocalDate dateSigned) { this.dateSigned = dateSigned; }

    
    

// Add these getter and setter methods


}
