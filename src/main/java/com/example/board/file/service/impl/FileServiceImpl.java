package com.example.board.file.service.impl;

import com.example.board.common.exception.EmptyFileException;
import com.example.board.common.exception.FileException;
import com.example.board.file.controller.response.FileResponse;
import com.example.board.file.persistence.entity.FileEntity;
import com.example.board.file.persistence.repository.FileEntityRepository;
import com.example.board.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.directory}")
    private String filePath;

    private final FileEntityRepository fileEntityRepository;

    @Override
    public FileResponse upload(MultipartFile file) {

        if(file == null || file.isEmpty()) {
            throw new EmptyFileException();
        }

        try{
            Path path = Paths.get(filePath);

            if(Files.exists(path)) {
                Files.createDirectories(path);
            }

            String originalFilename = file.getOriginalFilename();
            String spaceRemoveFileName = originalFilename.replaceAll(" ", "_");

            int index = originalFilename.lastIndexOf(".");
            String extension = originalFilename.substring(index + 1);

            String newFileName = UUID.randomUUID().toString() + spaceRemoveFileName;

            Path filePath = path.resolve(newFileName);
            file.transferTo(filePath.toFile());

            FileEntity entity = FileEntity.builder()
                    .originalFileName(originalFilename)
                    .fileName(newFileName)
                    .extension(extension)
                    .savePath(filePath.toString())
                    .build();

            FileEntity saveEntity = fileEntityRepository.save(entity);

            return FileResponse.builder()
                    .id(saveEntity.getId())
                    .originalFileName(saveEntity.getOriginalFileName())
                    .fileName(saveEntity.getFileName())
                    .path(saveEntity.getSavePath())
                    .extension(saveEntity.getExtension())
                    .createdAt(saveEntity.getCreatedAt())
                    .updatedAt(saveEntity.getUpdatedAt())
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException("파일 업로드에 실패하였습니다.");
        }

    }

    @Override
    public void download() {

    }
}
