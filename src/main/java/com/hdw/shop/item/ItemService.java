package com.hdw.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final com.hdw.shop.item.itemRepository itemRepository;

    public Page<Item> listItem(int page){
        Page<Item> result =  itemRepository.findPageBy(PageRequest.of(page-1,3));
        System.out.println("페이지수: "+result.getTotalPages());
        return result;
    }

    public void saveItem(@ModelAttribute Item item,Authentication auth){
        item.setUsername(auth.getName());
        itemRepository.save(item);
    }

    public Optional<Item> selectItem(long id){
        Optional<Item> item = itemRepository.findById(id);
        return item;
    }

    public void update(@ModelAttribute Item item){
        itemRepository.save(item);
    }

    public void delete(long id){
        itemRepository.deleteById(id);
    }

    public List<Item> search(String search){
        List<Item> list = itemRepository.rawQuery1(search);
        return list;
    }

}
