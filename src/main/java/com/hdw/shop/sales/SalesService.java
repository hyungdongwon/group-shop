package com.hdw.shop.sales;

import com.hdw.shop.item.Item;
import com.hdw.shop.member.CustomUser;
import com.hdw.shop.member.Member;
import com.hdw.shop.member.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final SalesRepository salesRepository;

    public void salesWrite(Sales sales, Authentication auth,Long itemid){
        CustomUser user = (CustomUser)auth.getPrincipal();
        Member memberid  = new Member();
        memberid.setId(user.id);
        sales.setMember(memberid);

        Item itemId = new Item();
        itemId.setId(itemid);
        sales.setItem(itemId);

        salesRepository.save(sales);
    }

    public List<SalesDto> orderlist(){
        List<Sales> orderlist = salesRepository.customFindAll();
        List<SalesDto> dtoList = new ArrayList<>();
        for(int i=0; i<orderlist.size(); i++){
            dtoList.add(new SalesDto());
            dtoList.get(i).setCount(orderlist.get(i).getCount());
            dtoList.get(i).setUsername(orderlist.get(i).getMember().getUsername());
            dtoList.get(i).setTitle(orderlist.get(i).getItem().getTitle());
            dtoList.get(i).setPrice(orderlist.get(i).getItem().getPrice());
        }
        return dtoList;
    }

}

@ToString
@Getter
@Setter
class SalesDto{
     private int count;
     private String username;
     private String title;
     private int price;
}