package com.sej.escape.service;

import com.sej.escape.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardService {

    private BoardRepository boardRepository;
}
