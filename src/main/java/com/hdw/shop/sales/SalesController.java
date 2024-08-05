package com.hdw.shop.sales;

import com.hdw.shop.member.Member;
import com.hdw.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {
    private final SalesService salesService;
    private final MemberRepository memberRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sales")
    String sales(@ModelAttribute Sales sales, Authentication auth,Long itemId){
        salesService.salesWrite(sales,auth,itemId);
        return "redirect:/list/page/1";
    }

    @GetMapping("/order/all")
    String getOrder(Model model){
        List<SalesDto> orderList =  salesService.orderlist();
        model.addAttribute("orderList",orderList);
        var result = memberRepository.findById(1L);
        System.out.println("result: "+result.get().getSales());
        return "sales.html";
    }


}
