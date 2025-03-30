package com.itda.moamoa.domain.spec.controller;

import com.itda.moamoa.domain.spec.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SimpleUploadController {
    
    @Autowired
    private S3Service s3Service;
    
    @PostMapping("/simple-upload")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        
        try {
            if (file == null || file.isEmpty()) {
                response.put("error", "파일이 비어있습니다");
                return ResponseEntity.badRequest().body(response);
            }
            
            String url = s3Service.uploadFile(file);
            response.put("success", "true");
            response.put("url", url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 