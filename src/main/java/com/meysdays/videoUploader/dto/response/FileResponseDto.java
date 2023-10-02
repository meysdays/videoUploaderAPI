package com.meysdays.videoUploader.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDto {

    private String fileId;
    private String filePath;
    private LocalDateTime createdAt;
}
