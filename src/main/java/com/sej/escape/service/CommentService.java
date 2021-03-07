package com.sej.escape.service;

import com.sej.escape.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
}
