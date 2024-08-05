package com.hdw.shop.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/commentWrite")
    String commentWrite(@ModelAttribute Comment comment, Authentication auth, int page){
        commentService.write(comment,auth);
        return "redirect:/detail/"+comment.getParentId()+"?comment="+page;
    }

}
