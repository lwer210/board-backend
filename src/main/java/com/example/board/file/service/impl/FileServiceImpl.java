package com.example.board.file.service.impl;

import com.example.board.common.custom.CustomUserDetails;
import com.example.board.common.exception.EmptyFileException;
import com.example.board.common.exception.FileException;
import com.example.board.common.exception.FileNotFoundException;
import com.example.board.common.exception.UnauthorizedException;
import com.example.board.file.controller.dto.DownloadFileDto;
import com.example.board.file.controller.response.FileResponse;
import com.example.board.file.persistence.entity.FileEntity;
import com.example.board.file.persistence.repository.FileEntityRepository;
import com.example.board.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
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
    public FileResponse upload(MultipartFile file, CustomUserDetails customUserDetails) {
        if(customUserDetails == null){
            throw new UnauthorizedException();
        }

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
    public DownloadFileDto download(Long fileSeq, CustomUserDetails customUserDetails) {
        FileEntity file = fileEntityRepository.findById(fileSeq)
                .orElseThrow(FileNotFoundException::new);

        String path = file.getSavePath();
        Path filePath = Paths.get(path).normalize();

        if(!Files.exists(filePath)) {
            throw new FileNotFoundException();
        }

        Resource resource = null;
        try{
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // TODO 예외 교체 필요
        }

        String fileName = file.getOriginalFileName().replaceAll(" ", "_");
        try{
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e); // TODO 예외 교체 필요
        }

        String contentDisposition = "attachment; filename=\"" + fileName + "\"";

        return DownloadFileDto.builder()
                .contentDisposition(contentDisposition)
                .resource(resource)
                .build();
    }
}
