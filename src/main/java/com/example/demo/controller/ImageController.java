package com.example.demo.controller;

import com.example.demo.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ImageController {

    private final FileStorageService fileStorageService;

    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/uploads/posts/{id}/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable int id,@PathVariable String filename) {
        System.out.println("!!!!!!!!!!!!!!");
        Resource resource = fileStorageService.loadImage(id,filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/uploads/profilePictures/{filename}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        System.out.println("!!!!!!!!!!!!!!");
        Resource resource = fileStorageService.loadProfilePicture(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
