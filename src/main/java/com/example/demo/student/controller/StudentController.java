package com.example.demo.student.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.model.Student;
import com.example.demo.student.model.Voter;
import com.example.demo.student.repository.VoterRepositorye;
import com.example.demo.student.service.EmailService;
import com.example.demo.student.service.LoginoService;
import com.example.demo.student.service.StudentService;
import com.example.demo.student.service.VoterServicee;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;
    private final  VoterServicee voterServicee;


    @Autowired
    private LoginoService loginoService; // ✅ Added for OTP verification

    private LoginoService loginoservice;
    private EmailService emailService;
    private VoterRepositorye voterrepositorye;
    private VoterRepositorye voterRepositorye;
   

    @Autowired
    public StudentController(StudentService studentService,LoginoService loginoservice,EmailService emailService,VoterServicee voterServicee,VoterRepositorye voterRepositorye) {
        this.studentService = studentService;
        this.loginoservice= loginoservice;
        this.emailService= emailService;
        this.voterServicee =voterServicee;
        this.voterRepositorye=voterRepositorye;
    }
    @Autowired
private HttpSession session;

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudent();
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    // ✅ Login using both password and OTP
    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
    String aadharno = loginData.get("aadharno");
    String voterId = loginData.get("voterId");
    String password = loginData.get("password");
    String otp = loginData.get("otp");

    if ((aadharno == null || aadharno.isEmpty()) && (voterId == null || voterId.isEmpty())) {
        return ResponseEntity.status(400).body(Map.of("success", false, "message", "Aadhaar number or Voter ID is required"));
    }
    if (password == null || otp == null) {
        return ResponseEntity.status(400).body(Map.of("success", false, "message", "Missing credentials"));
    }

    // Step 1: Verify the password using Aadhaar number (since password is only stored in student table)
    Optional<Student> student = studentService.loginUser(aadharno, password);
    if (student.isEmpty()) {
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid Aadhaar number or password"));
    }

    // Step 2: Verify OTP using the Aadhaar number (we'll use Aadhaar number or Voter ID here)
    boolean isOtpValid = loginoService.verifyOtp(aadharno != null ? aadharno : voterId, otp);
    if (!isOtpValid) {
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid OTP"));
    }

    // Step 3: Fetch voter details (photo and Voter ID) from voter table using Aadhaar number or Voter ID
    Optional<Voter> voter = Optional.empty();
    if (aadharno != null && !aadharno.isEmpty()) {
        voter = voterServicee.getVoterByAadhaar(aadharno);  // Fetch using Aadhaar number
    } else if (voterId != null && !voterId.isEmpty()) {
        voter = voterServicee.getVoterByVoterId(voterId);  // Fetch using Voter ID
    }

    if (voter.isEmpty()) {
        return ResponseEntity.status(404).body(Map.of("success", false, "message", "Voter details not found"));
    }
    session.setAttribute("aadharno", aadharno);

    // Step 4: Get photo from the voter and encode it to Base64
    byte[] photoData = voter.get().getPhotoData(); // Assuming getPhotoData() returns the byte array
        if (photoData == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Photo not available"));
        }

        String photoBase64 = Base64.getEncoder().encodeToString(photoData);
        String photoDataUrl = "data:image/jpeg;base64," + photoBase64; // Assuming the photo is a JPEG image
        String firstName = voter.get().getFirstName();  // Using the getter method
String lastName = voter.get().getLastName();    // Using the getter method


        // Return the login success response with user details
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Login successful!",
            "firstName", firstName,
    "lastName", lastName,

            "photo", photoDataUrl,
            "voterId", voter.get().getVoterId() // Include Voter ID
        ));
    }
    @PostMapping("/login/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody Map<String, String> data) {
        String aadharno = data.get("aadharno");

        if (aadharno == null || aadharno.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Aadhaar number is required"));
        }

        boolean exists = loginoService.checkUserExists(aadharno);
        if (!exists) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Aadhaar not registered"));
        }

        loginoService.generateAndSendOtp(aadharno);

        return ResponseEntity.ok(Map.of("success", true, "message", "OTP sent to your registered email"));
}




    // Check if login is by Aadhaar number or Voter ID
   

    @PostMapping("/cast")

    public ResponseEntity<String> castVote(@RequestBody Map<String, String> requestData) {
        String aadharno = requestData.get("aadharno");

        String candidateVoted = requestData.get("candidateVoted");
    
        boolean success = studentService.castVote(aadharno, candidateVoted);

        if (success) {
            // Get the voter's email from the database using aadharno
            String email = studentService.getEmailByAadhar(aadharno); // You need to implement this method
    
            // Prepare the message
            String subject = "🗳️ Vote Confirmation";
            String message = "You have successfully cast your vote for " + candidateVoted + ". Thank you for voting!";
    
            // Send the email
            emailService.sendEmail(email, subject, message);
    
            return ResponseEntity.ok("Vote cast successfully for " + candidateVoted + "!");
        } else {
            return ResponseEntity.badRequest().body("You have already voted or invalid Aadhaar number.");
        }
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(HttpSession session) {
        // Retrieve aadharno from the session
        String aadharno = (String) session.getAttribute("aadharno");
    
        if (aadharno == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "User is not logged in"));
        }
    
        // Fetch voter details using the stored aadharno
        Optional<Voter> voterOpt = voterRepositorye.findByAadharno(aadharno);
        if (voterOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Voter not found"));
        }
    
        Voter voter = voterOpt.get();
        // Prepare the response with user's profile details
        Map<String, Object> profileInfo = new HashMap<>();
        profileInfo.put("firstName", voter.getFirstName());
        profileInfo.put("lastName", voter.getLastName());
        profileInfo.put("voterId", voter.getVoterId());
    
        // Convert photo to base64 if it exists
        byte[] photoData = voter.getPhotoData();
        if (photoData != null) {
            String photoBase64 = Base64.getEncoder().encodeToString(photoData);
            profileInfo.put("profilePhoto", "data:image/jpeg;base64," + photoBase64);
        }
    
        return ResponseEntity.ok(profileInfo);
    }
    




    



   
}