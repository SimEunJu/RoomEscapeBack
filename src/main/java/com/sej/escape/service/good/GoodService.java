package com.sej.escape.service.good;

import com.sej.escape.repository.good.StoreGoodRepository;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoodService {

    private final StoreGoodRepository storeZimRepository;
    private final MemberMapper memberMapper;

}
