package com.hdw.shop.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void write(Comment comment, Authentication auth){
        comment.setUsername(auth.getName());
        commentRepository.save(comment);
    }
    public Page<Comment> commentPage(long id,int page){
        Page<Comment> commentList = commentRepository.findByParentId(id, PageRequest.of(page-1,3));
        return commentList;
    }

}
