package com.qiushop.qiushopbackend.controller;

import com.qiushop.qiushopbackend.items.composite.ProductComposite;
import com.qiushop.qiushopbackend.pojo.ProductItem;
import com.qiushop.qiushopbackend.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//商品类目查询接口
@RestController
@RequestMapping("/product")
public class ProductItemController {

    @Autowired
    private ProductItemService productItemService;

    //功能一: 前端获取商品类目信息
    @PostMapping("/fetchAllItems")
    public ProductComposite fetchAllItems() {
        return productItemService.fetchAllItems();
    }

    //业务部门添加商品类目的接口
    @PostMapping("/addItems")
    public ProductComposite addItems(@RequestBody ProductItem item) {
        return productItemService.addItems(item);
    }

    //业务部门删除商品类目的接口
    @PostMapping("/delItems")
    public ProductComposite delItems(@RequestBody ProductItem item) {
        return productItemService.delItems(item);
    }
}
