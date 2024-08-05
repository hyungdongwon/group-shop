package com.hdw.shop.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString  //이제 객체값 꺼내올때 오브젝트 주소로말고 안에있는값이 바로보이게하는 lombok 기능
@Table(indexes =  @Index(columnList = "title", name="작명"))
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;

    private Integer price;

    // New field
    @Column(name = "username")
    private String username ;



}
