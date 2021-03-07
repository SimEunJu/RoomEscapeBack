package com.sej.escape.service;

import com.sej.escape.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ThemeService {

    private ThemeRepository themeRepository;
}
