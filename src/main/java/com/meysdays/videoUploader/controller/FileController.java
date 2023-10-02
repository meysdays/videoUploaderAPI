package com.meysdays.videoUploader.controller;

import com.meysdays.videoUploader.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("api/videos")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/startRecording")
    public ResponseEntity<?> startRecording() throws FileNotFoundException {
        fileService.startRecording();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/uploadVideo/{sessionId}")
    public ResponseEntity<?> uploadVideo(@PathVariable String sessionId, @RequestParam("file")MultipartFile multipartFile) throws IOException {
        fileService.uploadVideo(sessionId, multipartFile);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/stopRecording/{sessionId}")
    public ResponseEntity<?> stopRecording(@PathVariable String sessionId) throws IOException {
        fileService.stopRecording(sessionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getRecordingById/{sessionId}")
    public  ResponseEntity<?> getRecordingById(@PathVariable String sessionId){
        fileService.getRecordingById(sessionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
