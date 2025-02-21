package com.example.board.file.service;

import com.example.board.file.controller.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public FileResponse upload(MultipartFile file);

    public void download();
}
