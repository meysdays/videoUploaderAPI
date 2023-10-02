package com.meysdays.videoUploader.service;

import com.meysdays.videoUploader.dto.response.ApiResponseDto;
import com.meysdays.videoUploader.dto.response.FileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    ApiResponseDto<FileResponseDto> startRecording() throws FileNotFoundException;
    ApiResponseDto<FileResponseDto> uploadVideo(String sessionId, MultipartFile multipartFile) throws IOException;
    ApiResponseDto<FileResponseDto> getRecordingById(String sessionId);
    ApiResponseDto<FileResponseDto> stopRecording(String sessionId) throws IOException;
}
