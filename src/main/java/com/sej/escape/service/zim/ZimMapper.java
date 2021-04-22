package com.sej.escape.service.zim;

import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.Zim;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ZimMapper {

    private final ModelMapper modelMapper;
}
