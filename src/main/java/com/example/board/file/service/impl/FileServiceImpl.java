package com.example.board.file.service.impl;

import com.example.board.file.persistence.repository.FileEntityRepository;
import com.example.board.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileEntityRepository fileEntityRepository;

    @Override
    public void upload() {

    }

    @Override
    public void download() {

    }
}
