

package com.example.demo.student.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.student.model.Voter;
import com.example.demo.student.repository.VoterRepositorye;

@RestController
@RequestMapping("/api/voters")
@CrossOrigin(origins = "*")
public class VoterC {

    @Autowired
    private VoterRepositorye voterRepository;

    @PostMapping("/register")
    public Voter registerVoter(
        @RequestParam String firstName,
        @RequestParam(required = false) String middleName,
        @RequestParam String lastName,
        @RequestParam String dob,
        @RequestParam String gender,

        @RequestParam String address,
        @RequestParam(required = false) String address2,
        @RequestParam String city,
        @RequestParam String state,
        @RequestParam String postal,
        @RequestParam String phone,
        @RequestParam(required = false) String email,

        @RequestParam String idType,
        @RequestParam String aadharno,

        @RequestParam boolean citizen,
        @RequestParam boolean ageEligible,

        @RequestParam boolean prevRegistered,
        @RequestParam(required = false) String prevName,
        @RequestParam(required = false) String prevAddress,
        @RequestParam(required = false) String prevCity,
        @RequestParam(required = false) String prevState,

        @RequestParam MultipartFile photo,
        @RequestParam MultipartFile signature,

        @RequestParam String dateSigned

    ) throws IOException {

        Voter voter = new Voter();

        voter.setFirstName(firstName);
        voter.setMiddleName(middleName);
        voter.setLastName(lastName);
        voter.setDob(LocalDate.parse(dob));
        voter.setGender(gender);

        voter.setAddress(address);
        voter.setAddress2(address2);
        voter.setCity(city);
        voter.setState(state);
        voter.setPostal(postal);
        voter.setPhone(phone);
        voter.setEmail(email);

        voter.setIdType(idType);
        voter.setaadharno(aadharno);

        voter.setCitizen(citizen);
        voter.setAgeEligible(ageEligible);

        voter.setPrevRegistered(prevRegistered);
        voter.setPrevName(prevName);
        voter.setPrevAddress(prevAddress);
        voter.setPrevCity(prevCity);
        voter.setPrevState(prevState);
        voter.setIdType(idType);
        voter.setaadharno(aadharno);
        voter.setCitizen(citizen);
        voter.setAgeEligible(ageEligible);
        voter.setPrevRegistered(prevRegistered);
        voter.setPrevName(prevName);
        voter.setPrevAddress(prevAddress);
        voter.setPrevCity(prevCity);
        voter.setPrevState(prevState);


        voter.setPhotoName(photo.getOriginalFilename());
        voter.setPhotoData(photo.getBytes());

        voter.setSignatureName(signature.getOriginalFilename());
        voter.setSignatureData(signature.getBytes());

        voter.setDateSigned(LocalDate.parse(dateSigned));

        return voterRepository.save(voter);
    }
}
