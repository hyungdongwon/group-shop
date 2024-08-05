package com.hdw.shop.sales;

import com.hdw.shop.item.Item;
import com.hdw.shop.member.Member;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer count;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
                name = "memberId",
                foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Item item;

    @CreationTimestamp
    private LocalDateTime created;

}
