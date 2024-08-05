package com.hdw.shop.item;

import com.hdw.shop.comment.Comment;
import com.hdw.shop.comment.CommentRepository;
import com.hdw.shop.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor //lombok 문법 DB테이블 입출력하고싶을때 써야함 .
public class ItemController {
    private final ItemService itemService;
    private final S3Service s3Service;
    private final CommentService commentService;

    @GetMapping ("/list/page/{page}") //템플릿 엔진:서버/database의 데이터를 html에 집어넣을수 있음
    String list(Model model,@PathVariable int page){
        Page<Item> list = itemService.listItem(page);
        model.addAttribute("items",list);
        model.addAttribute("totalPage",list.getTotalPages());
        return "list.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @PostMapping("/add")
    String addPost(@ModelAttribute Item item, Authentication auth) {
        itemService.saveItem(item,auth);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}") //url 파라미터
    String detail(Model model,@PathVariable long id,int comment) { //url 파라미터 자리에 입력한값을 가져옴
        Page<Comment> commentPage = commentService.commentPage(id,comment);
        Optional<Item> result = itemService.selectItem(id);
        if(result.isPresent()){ //result 변수에 무언가있다면
            model.addAttribute("result",result.get());
            model.addAttribute("commentList",commentPage);
            model.addAttribute("totalPage",commentPage.getTotalPages());
            return "detail.html";
        }else {
            return "redirect:/list";
        }
    }

    @PostMapping("/update")
    String update(Model model,long id){
        Optional<Item> item = itemService.selectItem(id);
        model.addAttribute("item", item.get());
        return "update.html";
    }

    @PostMapping("/updateProc")
    String updateProc(@ModelAttribute Item item) throws Exception{
        if(item.getTitle().length() >= 100 || item.getPrice() < 0){
            throw new Exception("글자수가 많거나 가격이 음수임");
        }else{
            itemService.update(item);
            return "redirect:/list";
        }
    }
    @DeleteMapping("/delete")
    ResponseEntity<String> delete(@RequestParam long id){
        itemService.delete(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename){
        var result = s3Service.createPresignedUrl("test/"+filename);
        System.out.println(result);
        return result;
    }

    @PostMapping("/search")
    String postSearch(Model model,String searchText){
        List<Item> searchList = itemService.search(searchText);
        model.addAttribute("items",searchList);
        return "search.html";
    }

}
