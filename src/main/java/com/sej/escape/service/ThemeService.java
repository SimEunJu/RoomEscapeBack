package com.sej.escape.service;

import com.sej.escape.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ThemeService {

    private ThemeRepository themeRepository;


}
