package com.meysdays.videoUploader.service;

import com.meysdays.videoUploader.dto.response.ApiResponseDto;
import com.meysdays.videoUploader.dto.response.FileResponseDto;
import com.meysdays.videoUploader.entity.FileEntity;
import com.meysdays.videoUploader.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Value("${upload.directory}")
    private  String uploadDirectory;

    private final Map<String, FileOutputStream> videoStreams = new HashMap<>();
    private LocalDateTime localDateTime;
    @Override
    public ApiResponseDto<FileResponseDto> startRecording() throws FileNotFoundException {

        String sessionId = UUID.randomUUID().toString();

        //FileEntity fileEntity = new FileEntity();
        //fileEntity.setSessionId(sessionId);


            File directory = new File(uploadDirectory);
            if (!directory.exists()){
                directory.mkdirs();
            }
            String videoFileName = sessionId + "_recorded_video.mp4";
           // fileEntity.setFile(videoFileName);
            File videoFile = new File(directory.getAbsolutePath() + File.separator + videoFileName);

            FileOutputStream videoOutputStream = new FileOutputStream(videoFile);

            videoStreams.put(sessionId, videoOutputStream);

            FileEntity fileEntity = FileEntity.builder()
                    .file(videoFileName)
                    .localDateTime(LocalDateTime.now())
                    .sessionId(sessionId)
                    .build();

            fileRepository.save(fileEntity);

            return new ApiResponseDto<>("record started", sessionId);
    }

    @Override
    public ApiResponseDto<FileResponseDto> uploadVideo(String sessionId, MultipartFile multipartFile) throws IOException {
        if (!videoStreams.containsKey(sessionId)){
            return new ApiResponseDto<>("Recording session not found",null);
        }
        FileOutputStream videoOutputStream = videoStreams.get(sessionId);
        InputStream inputStream = multipartFile.getInputStream();

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            videoOutputStream.write(buffer, 0, bytesRead);
        }
        return new ApiResponseDto<>("upload successfully", sessionId);
    }

    @Override
    public ApiResponseDto<FileResponseDto> getRecordingById(String sessionId) {
        Optional<FileEntity> optional = Optional.ofNullable(fileRepository.findBySessionId(sessionId));
        if (optional.isEmpty()){
            return new ApiResponseDto<>("no sessionId", null);
        }
        return new ApiResponseDto<>("file path", optional.get().getFile());
    }

    @Override
    public ApiResponseDto<FileResponseDto> stopRecording(String sessionId) throws IOException {
        if (!videoStreams.containsKey(sessionId)) {
            return new ApiResponseDto<>("Recording session not found.", null);
        }

        FileOutputStream videoOutputStream = videoStreams.get(sessionId);
        videoOutputStream.close();
        videoStreams.remove(sessionId);

        return new ApiResponseDto<>("recording stopped", sessionId);
    }
}
