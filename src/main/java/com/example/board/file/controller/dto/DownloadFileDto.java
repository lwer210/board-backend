package com.example.board.file.controller.dto;

import com.example.board.file.controller.response.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadFileDto {

    private String contentDisposition;

    private Resource resource;
}
