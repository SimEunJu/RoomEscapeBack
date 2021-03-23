package com.sej.escape.service;

import com.sej.escape.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreService {

    private StoreRepository storeRepository;

    public List<StoreDto> getStores(){}


}
