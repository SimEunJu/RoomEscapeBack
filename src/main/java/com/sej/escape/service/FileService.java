package com.sej.escape.service;

import com.sej.escape.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FileService {

    private FileRepository fileRepository;

}
