package com.sej.escape.service.zim;

import com.sej.escape.dto.comment.CommentReqDto;
import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.Zim;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToLongFunction;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimRepository storeZimRepository;
    private final MemberMapper memberMapper;

}
