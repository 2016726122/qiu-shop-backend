package com.qiushop.qiushopbackend.mapper;

import com.qiushop.qiushopbackend.pojo.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {

    //根据 product id 查询商品信息，JPA 自动生成 SQL
    public Products findByProductId(String productId);
}
