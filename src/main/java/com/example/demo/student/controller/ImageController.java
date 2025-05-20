package com.example.demo.student.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.model.FaceImagePair;
import com.example.demo.student.repository.FaceImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private FaceImageRepository FaceImageRepository;

    // Save Aadhaar + both images to DB
    @PostMapping("/save-images")
    public ResponseEntity<String> saveFaceImages(@RequestBody FaceImagePair request) {
        try {
            byte[] image1Data = Base64.getDecoder().decode(request.getImage1Name());
            byte[] image2Data = Base64.getDecoder().decode(request.getImage2Name());

            FaceImagePair faceImagePair = new FaceImagePair();
            faceImagePair.setAadhaarNumber(request.getAadhaarNumber());
            faceImagePair.setImage1Name("captured_" + UUID.randomUUID() + ".png");
            faceImagePair.setImage2Name("uploaded_" + UUID.randomUUID() + ".png");
            faceImagePair.setImage1Data(image1Data);
            faceImagePair.setImage2Data(image2Data);

            FaceImageRepository.save(faceImagePair);

            return ResponseEntity.ok("Images saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save images.");
        }
    }

    // Compare images using external Python script
    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareFaces(@RequestBody FaceImagePair request) {
        Map<String, Object> response = new HashMap<>();

        try {
            byte[] image1Bytes = Base64.getDecoder().decode(request.getImage1Name());
            byte[] image2Bytes = Base64.getDecoder().decode(request.getImage2Name());

            // Save to temp RGB JPG files
            String tempDir = System.getProperty("java.io.tmpdir");
            File imageFile1 = new File(tempDir, "img1_" + UUID.randomUUID() + ".jpg");
            File imageFile2 = new File(tempDir, "img2_" + UUID.randomUUID() + ".jpg");

            saveImageAsJPG(image1Bytes, imageFile1);
            saveImageAsJPG(image2Bytes, imageFile2);

            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Users\\Lina Kolte\\Downloads\\login page\\.venv311\\Scripts\\python.exe",
                    "C:\\Users\\Lina Kolte\\Downloads\\login page\\login-page\\scripts\\face_matcher.py",
                    imageFile1.getAbsolutePath(),
                    imageFile2.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            InputStream is = process.getInputStream();
            Scanner s = new Scanner(is).useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";
            int exitCode = process.waitFor();

            // Clean up temp files
            imageFile1.delete();
            imageFile2.delete();

            System.out.println("Python script output:\n" + output);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resultMap = objectMapper.readValue(output, Map.class);

            String result = (String) resultMap.get("result");

            if ("match".equals(result)) {
                response.put("success", true);
                response.put("match", true);
                response.put("confidence", resultMap.get("distance"));
            } else if ("no_match".equals(result)) {
                response.put("success", true);
                response.put("match", false);
                response.put("confidence", resultMap.get("distance"));
            } else {
                response.put("success", false);
                response.put("error", resultMap.get("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Exception: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

// 🔧 Add this helper method in the same class
    private void saveImageAsJPG(byte[] imageBytes, File file) throws IOException {
        InputStream in = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(in);

        if (originalImage == null) {
            throw new IOException("Invalid image format or unreadable image.");
        }

        BufferedImage rgbImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        rgbImage.getGraphics().drawImage(originalImage, 0, 0, null);

        ImageIO.write(rgbImage, "jpg", file);
    }
}
