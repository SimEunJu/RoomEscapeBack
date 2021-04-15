package com.sej.escape.service.zim;

import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimRepository storeZimRepository;
    private final MemberMapper memberMapper;

}
