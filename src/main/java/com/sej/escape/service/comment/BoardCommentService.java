package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.CommentModifyReqDto;
import com.sej.escape.dto.comment.CommentResDto;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.NoticeBoardComment;
import com.sej.escape.entity.comment.ReqBoardComment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.comment.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardCommentRepository<NoticeBoardComment> noticeBoardCommentRepository;
    private final BoardCommentRepository<ReqBoardComment> reqBoardCommentRepository;
    private final CommentMapper commentMapper;

    public CommentResDto addComment(CommentModifyReqDto reqDto) {
        String type = reqDto.getAncestor().getSubTypes().get(0);
        BoardCommentRepository repository = getRepositoryByType(type);
        Comment comment = commentMapper.mapReqDtoToEntity(reqDto, getEntity(type));
        comment = (Comment) repository.save(comment);

        if(reqDto.getParComment() == null) {
            comment.setParId(comment.getId());
        }else{
            Optional<? extends Comment> parCommentOpt = repository.findById(reqDto.getParComment().getId());
            Comment parComment = parCommentOpt.orElseThrow(
                            () -> new NoSuchResourceException(
                                    String.format("[%l]에 해당하는 Comment가 없습니다.",reqDto.getId() ))
                    );
            comment.setParId(parComment.getParId());
        }
        repository.save(comment);

        return commentMapper.mapEntityToDto(comment, CommentResDto.class);
    }

    private Class<? extends Comment> getEntity(String type){
        switch (type){
            case "notice": return NoticeBoardComment.class;
            case "req": return ReqBoardComment.class;
            default: throw new UnDefinedConstantException(String.format("[%s] 정의되지 않은 comment type입니다.", type));
        }
    }

    private BoardCommentRepository getRepositoryByType(String type){
        switch (type){
            case "notice": return noticeBoardCommentRepository;
            case "req": return reqBoardCommentRepository;
            default: throw new UnDefinedConstantException(String.format("[%s] 정의되지 않은 comment type입니다.", type));
        }
    }
}
