package com.hdw.shop.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales,Long> {
    @Query(value = "select s from Sales s join fetch s.member join fetch s.item" )
    List<Sales> customFindAll();
}
